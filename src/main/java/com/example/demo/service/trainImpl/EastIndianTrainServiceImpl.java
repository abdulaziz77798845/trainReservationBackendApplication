package com.example.demo.service.trainImpl;

import com.example.demo.exception.TrainAlreadyExistsException;
import com.example.demo.exception.TrainNotExistException;
import com.example.demo.model.Passenger;
import com.example.demo.model.Train;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class EastIndianTrainServiceImpl implements TrainService {


    @Qualifier("eastTemplate")
    @Autowired
    private MongoTemplate eastMongoTemplate;

    @Override
    public Train addTrain(Train train) {

        try {
            Train existingTrain = getTrainByName(train.getTrainName());
            if (existingTrain!=null){
                throw new TrainAlreadyExistsException("Train with the name : "+train.getTrainName()+" already exists");
            }
            eastMongoTemplate.insert(train);
            return train;
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to add the train",exp);
        }
    }

    @Override
    public Train getTrainByName(String trainName) {
        try {
            Query query = Query.query(Criteria.where("trainName").is(trainName));
            return eastMongoTemplate.findOne(query,Train.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to get the train",exp);
        }
    }

    @Override
    public List<Passenger> getPassengersByTrainName(String trainName) {
        try {
            Query query = Query.query(Criteria.where("trainName").is(trainName));
            Train train = eastMongoTemplate.findOne(query, Train.class);
            return train!=null ? train.getBookedPassengers() : new ArrayList<>();
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to get the passengers by train",exp);
        }
    }

    @Override
    public Train updateTrain(Train train) {
        try {
            Train existingTrain = getTrainByName(train.getTrainName());
            if (existingTrain!=null){
                throw new TrainNotExistException("Train doesn't exist");
            }
            return eastMongoTemplate.save(train,"Train");
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to update the train",exp);
        }
    }

    @Override
    public List<Train> getAllTrains() {
        try {
            return eastMongoTemplate.findAll(Train.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to get the trains",exp);
        }
    }

    @Override
    public Optional<Train> findTrainById(String trainID) {
        try {
            return Optional.ofNullable(eastMongoTemplate.findById(trainID, Train.class));
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to get the train by train ID",exp);
        }
    }

    @Override
    public void deleteTrain(String trainID) {
        try {
            Query query = Query.query(Criteria.where("_id").is(new ObjectId(trainID)));

            Train existingTrain = eastMongoTemplate.findOne(query, Train.class);

            if (existingTrain == null){
                throw new TrainNotExistException("Train with ID "+trainID+" does not exist");
            }
            eastMongoTemplate.remove(query, Train.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to delete the train by train ID",exp);
        }
    }

    @Override
    public void saveTrain(Train train){
        try {
            eastMongoTemplate.save(train);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to save the train",exp);
        }
    }

    /*
    //@Qualifier("eastTemplate")
    @Autowired
    private EastIndianRailwaysRepo eastIndianRailwaysRepo;


    //@Qualifier("eastTemplate")
    @Autowired
    private EastPassengerRepo eastPassengerRepo;

    @Override
    public Train addTrain(Train train) {
        return eastIndianRailwaysRepo.save(train);
    }
    @Override
    public Train getTrainByName(String trainName) {
        return eastIndianRailwaysRepo.findByTrainName(trainName);
    }
    @Override
    public List<Passenger> getPassengersByTrainName(String trainName) {
        return eastPassengerRepo.findAll();
    }

    @Override
    public Train updateTrain(Train train) {
        return eastIndianRailwaysRepo.save(train);
    }

    @Override
    public List<Train> getAllTrains() {
        return eastIndianRailwaysRepo.findAll();
    }

    @Override
    public Optional<Train> findTrainById(String trainID) {
        return eastIndianRailwaysRepo.findById(trainID);
    }

    @Override
    public void deleteTrain(String trainID) {
        eastIndianRailwaysRepo.deleteById(trainID);
    }*/





}