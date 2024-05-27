package com.example.demo.service.passImpl;

import com.example.demo.model.Passenger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class WestPassengerService implements PassengerService{

    @Qualifier("westTemplate")
    @Autowired
    private MongoTemplate westPassTemplate;

    public Passenger addPassenger(Passenger passenger) {
        try {
            westPassTemplate.insert(passenger);
            return passenger;
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to add passenger", exp);
        }
    }

    public Optional<Passenger> findPassengerById(String passID){
        try {
            return Optional.ofNullable(westPassTemplate.findById(passID, Passenger.class));
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to find passenger by ID", exp);
        }
    }

    public void deletePassengerByID(String passID){
        try {
            Query query = Query.query(Criteria.where("_id").is(new ObjectId(passID)));
            westPassTemplate.remove(query, Passenger.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to delete passenger by ID", exp);
        }
    }

    public void delete(Passenger passenger){
        try {
            Query query = Query.query(Criteria.where("_id").is(passenger.getId()));
            westPassTemplate.remove(query, Passenger.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to delete passenger", exp);
        }
    }

    public List<Passenger> getAllPassengersInTheRegion(){
        try {
            return westPassTemplate.findAll(Passenger.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to find all passengers in region", exp);
        }
    }

    public void savePassenger(Passenger passenger){
        try {
            westPassTemplate.save(passenger);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to save passenger", exp);
        }
    }
}