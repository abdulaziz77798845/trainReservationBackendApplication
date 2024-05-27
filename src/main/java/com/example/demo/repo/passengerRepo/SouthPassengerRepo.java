package com.example.demo.repo.passengerRepo;

import com.example.demo.model.Passenger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface SouthPassengerRepo extends MongoRepository<Passenger,String> {
    @Query("{'id':?0}")
    Passenger getPassengerById(String passID);
}
