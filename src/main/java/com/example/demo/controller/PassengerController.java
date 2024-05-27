package com.example.demo.controller;


import com.example.demo.TrainFactory.TrainFactory;
import com.example.demo.dto.CancelDTO;
import com.example.demo.dto.PassengerDTO;
import com.example.demo.dto.ResponseStatus;
import com.example.demo.model.Passenger;
import com.example.demo.model.Train;
import com.example.demo.service.trainImpl.TrainService;
import com.example.demo.service.booking_and_cancelling.BookingAndCancellingServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passenger")
public class PassengerController {

    @Autowired
    private BookingAndCancellingServiceImpl passengerService;

    @Autowired
    private TrainFactory trainFactory;


    @Operation(summary = "Ticket Booking", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/book")
    public ResponseEntity<?> bookPassenger(@RequestBody PassengerDTO passengerDTO){
        try {
            Passenger passenger = passengerService.bookPassenger(passengerDTO);
            return ResponseEntity.ok(new ResponseStatus("Ticket Booked Successfully"));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseStatus("Server Error , Ticket not booked"));
        }
    }

    @Operation(summary = "Ticket Cancelling",security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelBooking(@RequestBody CancelDTO cancelDTO){
        try {
            String res = passengerService.cancelBookingForPassenger(cancelDTO);
            return ResponseEntity.ok(new ResponseStatus("Ticket Cancelled Successfully"));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseStatus("Error int cancelling ticket"));
        }
    }

    @Operation(summary = "Get all the trains by region",security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/viewAllTheTrainsByRegion")
    public ResponseEntity<?> getAllTrainsByRegions(@RequestHeader("region") String region){
        try {
            TrainService trainService = trainFactory.getTrainService(region);
            List<Train> trains = trainService.getAllTrains();
            return new ResponseEntity<>(trains,HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseStatus("Server Error , Failed to fetch all the trains"));
        }
    }
}