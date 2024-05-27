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
public class EastPassengerService implements PassengerService{
    @Qualifier("eastTemplate")
    @Autowired
    private MongoTemplate eastPassTemplate;

    public Passenger addPassenger(Passenger passenger) {
        try {
            eastPassTemplate.insert(passenger);
            return passenger;
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to add passenger", exp);
        }
    }

    public Optional<Passenger> findPassengerById(String passID){
        try {
            return Optional.ofNullable(eastPassTemplate.findById(passID, Passenger.class));
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to find passenger by ID", exp);
        }
    }

    public void deletePassengerByID(String passID){
        try {
            Query query = Query.query(Criteria.where("_id").is(new ObjectId(passID)));
            eastPassTemplate.remove(query, Passenger.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to delete passenger by ID", exp);
        }
    }

    public void delete(Passenger passenger){
        try {
            Query query = Query.query(Criteria.where("_id").is(passenger.getId()));
            eastPassTemplate.remove(query, Passenger.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to delete passenger", exp);
        }
    }

    public List<Passenger> getAllPassengersInTheRegion(){
        try {
            return eastPassTemplate.findAll(Passenger.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to find all passengers in region", exp);
        }
    }

    public void savePassenger(Passenger passenger){
        try {
            eastPassTemplate.save(passenger);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to save passenger", exp);
        }
    }
}