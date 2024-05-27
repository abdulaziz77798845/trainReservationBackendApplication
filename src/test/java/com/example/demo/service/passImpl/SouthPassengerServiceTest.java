package com.example.demo.service.passImpl;

import com.example.demo.model.Passenger;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class SouthPassengerServiceTest {

    @Qualifier("southTemplate")
    @Mock
    private MongoTemplate southPassengerMongoTemplate;

    @InjectMocks
    private SouthPassengerService southPassengerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addPassenger() {
        Passenger passenger = new Passenger();
        passenger.setPassengerTrainId("6630805684580e7584542e1a");
        passenger.setPassengerName("Kholi");
        passenger.setAge(35);
        passenger.setDate("21/04/24");
        passenger.setFrom("Delhi");
        passenger.setTo("Chennai");
        passenger.setBerthPreference("L");
        passenger.setEmailId("abdulaziz2002ece@gmail.com");

        when(southPassengerMongoTemplate.insert(passenger)).thenReturn(passenger);

        Passenger savedPassenger = southPassengerService.addPassenger(passenger);

        assertNotNull(savedPassenger);
        assertEquals(savedPassenger.getPassengerTrainId(), passenger.getPassengerTrainId());
        assertEquals(savedPassenger.getPassengerName(), passenger.getPassengerName());
        assertEquals(savedPassenger.getAge(), passenger.getAge());
        assertEquals(savedPassenger.getDate(), passenger.getDate());
        assertEquals(savedPassenger.getFrom(), passenger.getFrom());
        assertEquals(savedPassenger.getTo(), passenger.getTo());
        assertEquals(savedPassenger.getBerthPreference(), passenger.getBerthPreference());
        assertEquals(savedPassenger.getEmailId(), passenger.getEmailId());
    }


    @Test
    void addPassenger_exception_is_train_id_empty() {
        Passenger passenger = new Passenger();
        passenger.setPassengerTrainId("");

        when(southPassengerMongoTemplate.insert(passenger)).thenThrow(new DataAccessException("...") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> southPassengerService.addPassenger(passenger));
        assertEquals("Failed to add passenger", exception.getMessage());
    }

    @Test
    void addPassenger_exception_berth_preference_is_empty() {
        Passenger passenger = new Passenger();
        passenger.setBerthPreference("");

        when(southPassengerMongoTemplate.insert(passenger)).thenThrow(new DataAccessException("...") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> southPassengerService.addPassenger(passenger));
        assertEquals("Failed to add passenger", exception.getMessage());
    }


    @Test
    void addPassenger_exception_berth_preference_is_null() {
        Passenger passenger = new Passenger();
        passenger.setBerthPreference(null);

        when(southPassengerMongoTemplate.insert(passenger)).thenThrow(new DataAccessException("...") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> southPassengerService.addPassenger(passenger));
        assertEquals("Failed to add passenger", exception.getMessage());
    }


    @Test
    void addPassenger_exception_is_train_id_null() {
        Passenger passenger = new Passenger();
        passenger.setPassengerTrainId(null);

        when(southPassengerMongoTemplate.insert(passenger)).thenThrow(new DataAccessException("...") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> southPassengerService.addPassenger(passenger));
        assertEquals("Failed to add passenger", exception.getMessage());
    }


    @Test
    void findPassengerById() {
        Passenger passenger = new Passenger();
        passenger.setId("1");

        when(southPassengerMongoTemplate.findById("1", Passenger.class)).thenReturn(passenger);

        Optional<Passenger> foundPassenger = southPassengerService.findPassengerById(passenger.getId());

        assertNotNull(foundPassenger);
        assertEquals(foundPassenger.get().getPassengerTrainId(), passenger.getPassengerTrainId());
    }


    @Test
    void findPassengerById_exception_passID_is_empty() {
        when(southPassengerMongoTemplate.findById("", Passenger.class)).thenThrow(new DataAccessException("...") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> southPassengerService.findPassengerById(""));
        assertEquals("Failed to find passenger by ID", exception.getMessage());
    }

    @Test
    void findPassengerById_exception_passID_is_null() {
        when(southPassengerMongoTemplate.findById(null, Passenger.class)).thenThrow(new DataAccessException("...") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> southPassengerService.findPassengerById(null));
        assertEquals("Failed to find passenger by ID", exception.getMessage());
    }


    @Test
    void deletePassengerByID() {
        String passID = "6630ba4534a44e7b63c63895";
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(passID)));
        when(southPassengerMongoTemplate.remove(query, Passenger.class)).thenReturn(null);

        southPassengerService.deletePassengerByID(passID);
        verify(southPassengerMongoTemplate, times(1)).remove(query, Passenger.class);
    }


    @Test
    void delete() {
        Passenger passenger = new Passenger();
        passenger.setId("6630ba4534a44e7b63c63891");

        when(southPassengerMongoTemplate.findById("6630ba4534a44e7b63c63891", Passenger.class)).thenReturn(null);


        southPassengerService.delete(passenger);
        verify(southPassengerMongoTemplate, times(1)).remove(new Query(Criteria.where("_id").is(passenger.getId())), Passenger.class);
    }

    @Test
    void getAllPassengersInTheRegion() {
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(new Passenger());
        passengers.add(new Passenger());

        when(southPassengerMongoTemplate.findAll(Passenger.class)).thenReturn(passengers);

        List<Passenger> foundPassengers = southPassengerService.getAllPassengersInTheRegion();
        assertNotNull(foundPassengers);
        assertEquals(foundPassengers.size(), passengers.size());
    }


    @Test
    void savePassenger() {
        Passenger passenger = new Passenger();

        passenger.setPassengerTrainId("6630805684580e7584542e1a");
        passenger.setPassengerName("Dhoni");
        passenger.setAge(35);
        passenger.setDate("21/04/24");
        passenger.setFrom("Delhi");
        passenger.setTo("Chennai");
        passenger.setBerthPreference("U");
        passenger.setEmailId("abdulaziz2002ece@gmail.com");

        when(southPassengerMongoTemplate.save(passenger)).thenReturn(passenger);

        Passenger savedPassenger = southPassengerService.addPassenger(passenger);

        assertNotNull(savedPassenger);
        assertEquals(savedPassenger.getPassengerTrainId(), passenger.getPassengerTrainId());
        assertEquals(savedPassenger.getPassengerName(), passenger.getPassengerName());
        assertEquals(savedPassenger.getAge(), passenger.getAge());
        assertEquals(savedPassenger.getDate(), passenger.getDate());
        assertEquals(savedPassenger.getFrom(), passenger.getFrom());
        assertEquals(savedPassenger.getTo(), passenger.getTo());
        assertEquals(savedPassenger.getBerthPreference(), passenger.getBerthPreference());
        assertEquals(savedPassenger.getEmailId(), passenger.getEmailId());
    }


    @Test
    public void getAllPassengersInTheRegion_exception_passID_is_null() {
        when(southPassengerMongoTemplate.findAll(Passenger.class)).thenThrow(new DataAccessException("...") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> southPassengerService.getAllPassengersInTheRegion());

        assertEquals("Failed to find all passengers in region", exception.getMessage());
    }

    @Test
    public void savaPassenger_exception() {
        Passenger passenger = new Passenger();
        when(southPassengerMongoTemplate.save(passenger)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> southPassengerService.savePassenger(passenger));

        assertEquals("Failed to save passenger", exception.getMessage());
    }

    @Test
    public void delete_exception(){
        Passenger passenger = new Passenger();
        passenger.setId(null);
        Query query = Query.query(Criteria.where("_id").is(passenger.getId()));
        doThrow(new DataAccessException("...") {}).when(southPassengerMongoTemplate).remove(query, Passenger.class);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            southPassengerService.delete(passenger);
        });

        // Additional assertions can be made here
        assertEquals("Failed to delete passenger", exception.getMessage());
        verify(southPassengerMongoTemplate, times(1)).remove(query, Passenger.class);
    }

    @Test
    void deletePassengerByID_exception(){
        String passID = "6630ba4534a44e7b63c63895";
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(passID)));
        doThrow(new DataAccessException("...") {}).when(southPassengerMongoTemplate).remove(query, Passenger.class);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            southPassengerService.deletePassengerByID(passID);
        });

        assertEquals("Failed to delete passenger by ID", exception.getMessage());
        verify(southPassengerMongoTemplate, times(1)).remove(query, Passenger.class);
    }
}