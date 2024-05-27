package com.example.demo.service.trainImpl;

//import com.example.demo.repo.trainRepo.SouthIndianRailwaysRepo;
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
public class SouthIndianTrainServiceImpl implements TrainService {

    @Qualifier("southTemplate")
    @Autowired
    private MongoTemplate southMongoTemplate;

    @Override
    public Train addTrain(Train train) {

        try {
            Train existingTrain = getTrainByName(train.getTrainName());
            if (existingTrain!=null){
                throw new TrainAlreadyExistsException("Train with the name : "+train.getTrainName()+" already exists");
            }
            southMongoTemplate.insert(train);
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
            return southMongoTemplate.findOne(query,Train.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to get the train",exp);
        }
    }

    @Override
    public List<Passenger> getPassengersByTrainName(String trainName) {
        try {
            Query query = Query.query(Criteria.where("trainName").is(trainName));
            Train train = southMongoTemplate.findOne(query, Train.class);
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
            return southMongoTemplate.save(train,"Train");
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to update the train",exp);
        }
    }

    @Override
    public List<Train> getAllTrains() {
        try {
            return southMongoTemplate.findAll(Train.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to get the trains",exp);
        }
    }

    @Override
    public Optional<Train> findTrainById(String trainID) {
        try {
            return Optional.ofNullable(southMongoTemplate.findById(trainID, Train.class));
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to get the train by train ID",exp);
        }
    }

    @Override
    public void deleteTrain(String trainID) {
        try {
            Query query = Query.query(Criteria.where("_id").is(new ObjectId(trainID)));

            Train existingTrain = southMongoTemplate.findOne(query, Train.class);

            if (existingTrain == null){
                throw new TrainNotExistException("Train with ID "+trainID+" does not exist");
            }
            southMongoTemplate.remove(query, Train.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to delete the train by train ID",exp);
        }
    }

    @Override
    public void saveTrain(Train train){
        try {
            southMongoTemplate.save(train);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Failed to save the train",exp);
        }
    }
}