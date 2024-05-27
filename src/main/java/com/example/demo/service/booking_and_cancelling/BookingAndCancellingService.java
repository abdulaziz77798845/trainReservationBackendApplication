package com.example.demo.service.booking_and_cancelling;

import com.example.demo.dto.CancelDTO;
import com.example.demo.dto.PassengerDTO;
import com.example.demo.model.Passenger;
import com.example.demo.model.Train;

import java.util.List;
import java.util.Optional;



public interface BookingAndCancellingService {
    Passenger bookPassenger(PassengerDTO passengerDTO);
    List<Train> viewAllTrains(String region);
    String cancelBookingForPassenger(CancelDTO cancelDTO);
    Optional<Passenger> findPassengerById(String passengerID);
}