package com.example.demo.service.passImpl;

import com.example.demo.model.Passenger;

import java.util.List;
import java.util.Optional;

public interface PassengerService {
    Passenger addPassenger(Passenger passenger);
    Optional<Passenger> findPassengerById(String passID);
    void deletePassengerByID(String passID);
    void delete(Passenger passenger);
    List<Passenger> getAllPassengersInTheRegion();
    void savePassenger(Passenger passenger);
}
