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

class EastIndianTrainServiceImplTest {
    @InjectMocks
    private EastIndianTrainServiceImpl eastIndianTrainServiceImpl;


    @Qualifier("eastTemplate")
    @Mock
    private MongoTemplate eastMongoTemplate;

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

        when(eastMongoTemplate.insert(train)).thenReturn(train);

        Train result = eastIndianTrainServiceImpl.addTrain(train);

        assertEquals(train, result);
        verify(eastMongoTemplate,times(1)).insert(any(Train.class));
    }

    @Test
    void getTrainByName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("trainName").is("Delhi Express"));

        when(eastMongoTemplate.findOne(query, Train.class)).thenReturn(train);

        Train result = eastIndianTrainServiceImpl.getTrainByName("Delhi Express");

        assertEquals(train, result);
        verify(eastMongoTemplate,times(1)).findOne(query, Train.class);
    }

    @Test
    void getPassengersByTrainName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("trainName").is("Delhi Express"));

        when(eastMongoTemplate.findOne(query, Train.class)).thenReturn(train);

        List<Passenger> actualPassengers = eastIndianTrainServiceImpl.getPassengersByTrainName("Delhi Express");

        assertEquals(train.getBookedPassengers(), actualPassengers);
        verify(eastMongoTemplate,times(1)).findOne(query, Train.class);
    }

    @Test
    void updateTrain() {

        when(eastMongoTemplate.save(train,"Train")).thenReturn(train);
        Train result = eastIndianTrainServiceImpl.updateTrain(train);
        assertEquals(train, result);
        verify(eastMongoTemplate,times(1)).save(train,"Train");
    }

    @Test
    void getAllTrains() {
        List<Train> expectedtrainList = new ArrayList<>();
        expectedtrainList.add(train);

        when(eastMongoTemplate.findAll(Train.class)).thenReturn(expectedtrainList);

        List<Train> actualTrainList = eastIndianTrainServiceImpl.getAllTrains();

        assertEquals(expectedtrainList, actualTrainList);
        verify(eastMongoTemplate,times(1)).findAll(Train.class);
    }

    @Test
    void findTrainById() {
        when(eastMongoTemplate.findById("6630805684580e7584542e1a", Train.class)).thenReturn(train);

        Optional<Train> resultTrain = eastIndianTrainServiceImpl.findTrainById("6630805684580e7584542e1a");

        assertTrue(resultTrain.isPresent());
        assertEquals(train, resultTrain.get());
        verify(eastMongoTemplate,times(1)).findById("6630805684580e7584542e1a",Train.class);
    }

    @Test
    void deleteTrain() {
        eastIndianTrainServiceImpl.deleteTrain("6630805684580e7584542e1a");
        verify(eastMongoTemplate,times(1)).remove(any(Query.class), eq(Train.class));
    }

    @Test
    void saveTrain() {
        eastIndianTrainServiceImpl.saveTrain(train);
        verify(eastMongoTemplate,times(1)).save(train);
    }

}