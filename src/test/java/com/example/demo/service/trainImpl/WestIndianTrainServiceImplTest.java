package com.example.demo.service.trainImpl;

import com.example.demo.model.Passenger;
import com.example.demo.model.Train;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class WestIndianTrainServiceImplTest {

    @InjectMocks
    private WestIndianTrainServiceImpl westIndianTrainServiceImpl;


    @Qualifier("westTemplate")
    @Mock
    private MongoTemplate westMongoTemplate;

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

        when(westMongoTemplate.insert(train)).thenReturn(train);

        Train result = westIndianTrainServiceImpl.addTrain(train);

        assertEquals(train, result);
        verify(westMongoTemplate,times(1)).insert(any(Train.class));
    }

    @Test
    void getTrainByName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("trainName").is("Delhi Express"));

        when(westMongoTemplate.findOne(query, Train.class)).thenReturn(train);

        Train result = westIndianTrainServiceImpl.getTrainByName("Delhi Express");

        assertEquals(train, result);
        verify(westMongoTemplate,times(1)).findOne(query, Train.class);
    }

    @Test
    void getPassengersByTrainName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("trainName").is("Delhi Express"));

        when(westMongoTemplate.findOne(query, Train.class)).thenReturn(train);

        List<Passenger> actualPassengers = westIndianTrainServiceImpl.getPassengersByTrainName("Delhi Express");

        assertEquals(train.getBookedPassengers(), actualPassengers);
        verify(westMongoTemplate,times(1)).findOne(query, Train.class);
    }

    @Test
    void updateTrain() {

        when(westMongoTemplate.save(train,"Train")).thenReturn(train);
        Train result = westIndianTrainServiceImpl.updateTrain(train);
        assertEquals(train, result);
        verify(westMongoTemplate,times(1)).save(train,"Train");
    }

    @Test
    void getAllTrains() {
        List<Train> expectedtrainList = new ArrayList<>();
        expectedtrainList.add(train);

        when(westMongoTemplate.findAll(Train.class)).thenReturn(expectedtrainList);

        List<Train> actualTrainList = westIndianTrainServiceImpl.getAllTrains();

        assertEquals(expectedtrainList, actualTrainList);
        verify(westMongoTemplate,times(1)).findAll(Train.class);
    }

    @Test
    void findTrainById() {
        when(westMongoTemplate.findById("6630805684580e7584542e1a", Train.class)).thenReturn(train);

        Optional<Train> resultTrain = westIndianTrainServiceImpl.findTrainById("6630805684580e7584542e1a");

        assertTrue(resultTrain.isPresent());
        assertEquals(train, resultTrain.get());
        verify(westMongoTemplate,times(1)).findById("6630805684580e7584542e1a",Train.class);
    }

    @Test
    void deleteTrain() {
        westIndianTrainServiceImpl.deleteTrain("6630805684580e7584542e1a");
        verify(westMongoTemplate,times(1)).remove(any(Query.class), eq(Train.class));
    }

    @Test
    void saveTrain() {
        westIndianTrainServiceImpl.saveTrain(train);
        verify(westMongoTemplate,times(1)).save(train);
    }
}