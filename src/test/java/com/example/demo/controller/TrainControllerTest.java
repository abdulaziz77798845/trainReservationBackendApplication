package com.example.demo.controller;
import com.example.demo.TrainFactory.TrainFactory;

import com.example.demo.dto.TrainDTO;
import com.example.demo.model.Train;
import com.example.demo.service.trainImpl.TrainService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TrainControllerTest {

    @Mock
    private TrainFactory trainFactory;

    @Mock
    private TrainService trainService;

    @InjectMocks
    private TrainController trainController;

    private TrainDTO trainDTO;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        trainDTO = new TrainDTO();
        trainDTO.setTrainName("Delhi Express");
        trainDTO.setDate("14/04/24");
        trainDTO.setFrom("Delhi");
        trainDTO.setTo("Chennai");
        trainDTO.setLowerBerthCount(1);
        trainDTO.setUpperBerthCount(1);
        trainDTO.setMiddleBerthCount(1);
        trainDTO.setRacCount(1);
        trainDTO.setWaitingCount(1);
        trainDTO.setTrainFee(888.89);
        trainDTO.setBoardingTime("5.00 AM");
        trainDTO.setDroppingTime("1.00 AM");
        trainDTO.setHomeRegion("NR");
        trainDTO.setFromRegion("NR");
        trainDTO.setToRegion("SR");
    }

    @Test
    void testAddTrain(){

        Train train = new Train();

        when(trainFactory.getTrainService(anyString())).thenReturn(trainService);
        when(trainService.addTrain(any(Train.class))).thenReturn(train);

        ResponseEntity<?> response = trainController.addTrain(trainDTO);

        verify(trainService,times(1)).addTrain(any(Train.class));
        assertSame(train,response.getBody());
        assertSame(HttpStatus.CREATED,response.getStatusCode());
    }

    @Test
    void testUpdateTrain(){
        Train train = new Train();
        when(trainFactory.getTrainService(anyString())).thenReturn(trainService);
        when(trainService.updateTrain(any(Train.class))).thenReturn(train);


        ResponseEntity<?> response = trainController.update(trainDTO);

        verify(trainService,times(1)).updateTrain(any(Train.class));
        assertSame(train,response.getBody());
        assertSame(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void testGetAllTrainsByRegions(){
        List<Train> trains = new ArrayList<>();
        when(trainFactory.getTrainService(anyString())).thenReturn(trainService);
        when(trainService.getAllTrains()).thenReturn(trains);

        ResponseEntity<?> response = trainController.getAllTrainsByRegions("NR");
        verify(trainService,times(1)).getAllTrains();
        assertSame(trains,response.getBody());
        assertSame(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void testDeleteTrain(){
        when(trainFactory.getTrainService(anyString())).thenReturn(trainService);

        doNothing().when(trainService).deleteTrain(anyString());
        ResponseEntity<?> response = trainController.deleteTrain("663085dc88ed6f5fb5d8e99a","NR");
        verify(trainService,times(1)).deleteTrain(anyString());
        assertSame(HttpStatus.NO_CONTENT,response.getStatusCode());
    }
}