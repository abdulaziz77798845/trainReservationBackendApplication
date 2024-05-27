package com.example.demo.service.trainImpl;

import com.example.demo.model.Passenger;
import com.example.demo.model.Train;
import com.mongodb.client.model.UpdateOptions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NorthIndianTrainServiceImplTest {

    @InjectMocks
    private NorthIndianTrainServiceImpl northIndianTrainServiceImpl;


    @Qualifier("northTemplate")
    @Mock
    private MongoTemplate northMongoTemplate;

    private Train train;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        Passenger passenger1 = new Passenger();
        passenger1.setId("6630805684580e7584542e1a");
        passenger1.setPassengerTrainName("Delhi Express");
        passenger1.setAllowedPreference("L");

        Passenger passenger2 = new Passenger();
        passenger2.setId("6630805684580e7584542e1b");
        passenger2.setPassengerTrainName("Delhi Express");
        passenger2.setAllowedPreference("U");

        Passenger passenger3 = new Passenger();
        passenger3.setId("6630805684580e7584542e1c");
        passenger3.setPassengerTrainName("Delhi Express");
        passenger3.setAllowedPreference("M");


       train = new Train();
       train.setId("6630805684580e7584542e1a");
       train.setTrainName("Delhi Express");
       train.setDate("14/04/24");
       train.setFromRegion("Delhi");
       train.setToRegion("Chennai");
       train.getBookedPassengers().add(passenger1);
       train.getBookedPassengers().add(passenger2);
       train.getBookedPassengers().add(passenger3);
       train.setUpperBerthCount(1);
       train.setLowerBerthCount(1);
       train.setMiddleBerthCount(1);
       train.setHomeRegion("NR");
       train.setFrom("NR");
       train.setToRegion("SR");
       train.setTrainFee(500.88);
    }

    @Test
    void addTrain() {

        when(northMongoTemplate.insert(train)).thenReturn(train);

        Train result = northIndianTrainServiceImpl.addTrain(train);

        assertEquals(train, result);
        verify(northMongoTemplate,times(1)).insert(any(Train.class));
    }

    @Test
    void getTrainByName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("trainName").is("Delhi Express"));

        when(northMongoTemplate.findOne(query, Train.class)).thenReturn(train);

        Train result = northIndianTrainServiceImpl.getTrainByName("Delhi Express");

        assertEquals(train, result);
        verify(northMongoTemplate,times(1)).findOne(query, Train.class);
    }

    @Test
    void getPassengersByTrainName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("trainName").is("Delhi Express"));

        when(northMongoTemplate.findOne(query, Train.class)).thenReturn(train);

        List<Passenger> actualPassengers = northIndianTrainServiceImpl.getPassengersByTrainName("Delhi Express");

        assertEquals(train.getBookedPassengers(), actualPassengers);
        verify(northMongoTemplate,times(1)).findOne(query, Train.class);
    }

    @Test
    void updateTrain() {

        when(northMongoTemplate.save(train,"Train")).thenReturn(train);
        Train result = northIndianTrainServiceImpl.updateTrain(train);
        assertEquals(train, result);
        verify(northMongoTemplate,times(1)).save(train,"Train");
    }

    @Test
    void getAllTrains() {
        List<Train> expectedtrainList = new ArrayList<>();
        expectedtrainList.add(train);

        when(northMongoTemplate.findAll(Train.class)).thenReturn(expectedtrainList);

        List<Train> actualTrainList = northIndianTrainServiceImpl.getAllTrains();

        assertEquals(expectedtrainList, actualTrainList);
        verify(northMongoTemplate,times(1)).findAll(Train.class);
    }

    @Test
    void findTrainById() {
        when(northMongoTemplate.findById("6630805684580e7584542e1a", Train.class)).thenReturn(train);

        Optional<Train> resultTrain = northIndianTrainServiceImpl.findTrainById("6630805684580e7584542e1a");

        assertTrue(resultTrain.isPresent());
        assertEquals(train, resultTrain.get());
        verify(northMongoTemplate,times(1)).findById("6630805684580e7584542e1a",Train.class);
    }

    @Test
    void deleteTrain() {
        northIndianTrainServiceImpl.deleteTrain("6630805684580e7584542e1a");
        verify(northMongoTemplate,times(1)).remove(any(Query.class), eq(Train.class));
    }

    @Test
    void saveTrain() {
        northIndianTrainServiceImpl.saveTrain(train);
        verify(northMongoTemplate,times(1)).save(train);
    }
}