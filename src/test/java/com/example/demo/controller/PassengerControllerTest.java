package com.example.demo.controller;

import com.example.demo.TrainFactory.TrainFactory;
import com.example.demo.dto.CancelDTO;
import com.example.demo.dto.PassengerDTO;
import com.example.demo.dto.ResponseStatus;
import com.example.demo.model.Passenger;
import com.example.demo.model.Train;
import com.example.demo.service.booking_and_cancelling.BookingAndCancellingServiceImpl;
import com.example.demo.service.passImpl.PassengerService;
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
import static org.mockito.Mockito.*;

class PassengerControllerTest {

    @Mock
    private BookingAndCancellingServiceImpl bookingAndCancellingService;

    @Mock
    private TrainFactory trainFactory;

    @InjectMocks
    private PassengerController passengerController;


    @Mock
    private TrainService trainService;

    private PassengerDTO passengerDTO;

    private CancelDTO cancelDTO;

    @BeforeEach
    void setUp(){

        MockitoAnnotations.initMocks(this);
        passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerTrainId("663085dc88ed6f5fb5d8e99a");
        passengerDTO.setPassengerName("Aziz");
        passengerDTO.setAge(30);
        passengerDTO.setDate("14/04/24");
        passengerDTO.setFrom("Delhi");
        passengerDTO.setTo("Chennai");
        passengerDTO.setBerthPreference("L");
        passengerDTO.setEmailId("abdulaziz2002ece@gmail.com");

        cancelDTO = new CancelDTO();
        cancelDTO.setTrainId("663085dc88ed6f5fb5d8e99a");
        cancelDTO.setPassId("663085dc88ed6f5fb5d8e99c");
        cancelDTO.setFrom("Delhi");
        cancelDTO.setTo("Chennai");
        cancelDTO.setBerth("L");
        cancelDTO.setEmailId("abdulaziz2002ece@gmail.com");
    }


    @Test
    void bookPassenger(){
        Passenger passenger = new Passenger();

        when(bookingAndCancellingService.bookPassenger(passengerDTO)).thenReturn(passenger);


        ResponseEntity<?> response = passengerController.bookPassenger(passengerDTO);

        verify(bookingAndCancellingService,times(1)).bookPassenger(passengerDTO);
        assertSame(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void cancelBooking(){
        String cancelledMessage = "Ticket Cancelled Successfully";

        when(bookingAndCancellingService.cancelBookingForPassenger(cancelDTO)).thenReturn(cancelledMessage);

        ResponseEntity<?> response = passengerController.cancelBooking(cancelDTO);

        verify(bookingAndCancellingService,times(1)).cancelBookingForPassenger(cancelDTO);
        assertSame(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void getAllTrainsByRegions(){
        String region = "NR";
        List<Train> trains = new ArrayList<>();
        when(trainFactory.getTrainService(anyString())).thenReturn(trainService);
        when(trainService.getAllTrains()).thenReturn(trains);

        ResponseEntity<?> response = passengerController.getAllTrainsByRegions(region);

        verify(trainService,times(1)).getAllTrains();
        assertSame(trains,response.getBody());
        assertSame(HttpStatus.OK,response.getStatusCode());
    }
}