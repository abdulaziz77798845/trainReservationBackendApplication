package com.example.demo.controller;

import java.util.List;

import com.example.demo.TrainFactory.TrainFactory;
import com.example.demo.dto.ResponseStatus;
import com.example.demo.exception.TrainAlreadyExistsException;
import com.example.demo.exception.TrainNotExistException;
import com.example.demo.model.Train;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.TrainDTO;
import com.example.demo.mapper.Mapper;
import com.example.demo.service.trainImpl.TrainService;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    @Autowired
    private TrainFactory trainFactory;


    @Operation(summary = "Add the train to the region ",security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add")
    public ResponseEntity<?> addTrain(@RequestBody TrainDTO trainDTO){
        try {
            TrainService trainService = trainFactory.getTrainService(trainDTO.getHomeRegion());
            Train train = Mapper.convertToTrain(trainDTO);
            Train addedTrain = trainService.addTrain(train);
            return new ResponseEntity<>(addedTrain,HttpStatus.CREATED);
        }
        catch (TrainAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseStatus("Failed , Train already exists"));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseStatus("Failed to add the train"));
        }
    }

    @Operation(summary = "Update the train in the region",security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody TrainDTO trainDTO){
        try {

            TrainService trainService = trainFactory.getTrainService(trainDTO.getHomeRegion());
            Train train = Mapper.convertToTrain(trainDTO);
            Train updatedTrain = trainService.updateTrain(train);
            return new ResponseEntity<>(updatedTrain,HttpStatus.OK);
        }
        catch (TrainNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseStatus("Failed to update , Train not found"));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseStatus("Failed to update the train"));
        }
    }


    @Operation(summary = "Get all trains in the region",security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/getAllTrainsInTheRegion")
    public ResponseEntity<?> getAllTrainsByRegions(@RequestHeader("region") String region){
        try {
            TrainService trainService = trainFactory.getTrainService(region);
            List<Train> trains = trainService.getAllTrains();
            return new ResponseEntity<>(trains,HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseStatus("Failed to fetch all the trains from the region"));
        }
    }

    @Operation(summary ="Delete train in the region by ID" ,security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/deleteById/{trainName}")
    public ResponseEntity<?> deleteTrain(@PathVariable("trainName") String trainName, @RequestHeader("region") String region){
        try {
            TrainService trainService = trainFactory.getTrainService(region);
            trainService.deleteTrain(trainName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (TrainNotExistException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseStatus("Failed to delete the train , Train not found"));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}