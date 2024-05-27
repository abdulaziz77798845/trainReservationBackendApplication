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

class EastPassengerServiceTest {

    @Qualifier("eastTemplate")
    @Mock
    private MongoTemplate eastPassengerMongoTemplate;

    @InjectMocks
    private EastPassengerService eastPassengerService;

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

        when(eastPassengerMongoTemplate.insert(passenger)).thenReturn(passenger);

        Passenger savedPassenger = eastPassengerService.addPassenger(passenger);

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
    void addPassenger_exception_is_train_id_empty(){
        Passenger passenger = new Passenger();
        passenger.setPassengerTrainId("");

        when(eastPassengerMongoTemplate.insert(passenger)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> eastPassengerService.addPassenger(passenger));
        assertEquals("Failed to add passenger", exception.getMessage());
    }

    @Test
    void addPassenger_exception_berth_preference_is_empty(){
        Passenger passenger = new Passenger();
        passenger.setBerthPreference("");

        when(eastPassengerMongoTemplate.insert(passenger)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> eastPassengerService.addPassenger(passenger));
        assertEquals("Failed to add passenger", exception.getMessage());
    }

    @Test
    void addPassenger_exception_berth_preference_is_null(){
        Passenger passenger = new Passenger();
        passenger.setBerthPreference(null);

        when(eastPassengerMongoTemplate.insert(passenger)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> eastPassengerService.addPassenger(passenger));
        assertEquals("Failed to add passenger", exception.getMessage());
    }

    @Test
    void addPassenger_exception_is_train_id_null(){
        Passenger passenger = new Passenger();
        passenger.setPassengerTrainId(null);

        when(eastPassengerMongoTemplate.insert(passenger)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> eastPassengerService.addPassenger(passenger));
        assertEquals("Failed to add passenger", exception.getMessage());
    }

    @Test
    void findPassengerById() {
        Passenger passenger = new Passenger();
        passenger.setId("1");

        when(eastPassengerMongoTemplate.findById("1", Passenger.class)).thenReturn(passenger);

        Optional<Passenger> foundPassenger = eastPassengerService.findPassengerById(passenger.getId());

        assertNotNull(foundPassenger);
        assertEquals(foundPassenger.get().getPassengerTrainId(), passenger.getPassengerTrainId());
    }

    @Test
    void findPassengerById_exception_passID_is_empty(){
        when(eastPassengerMongoTemplate.findById("", Passenger.class)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> eastPassengerService.findPassengerById(""));
        assertEquals("Failed to find passenger by ID", exception.getMessage());
    }

    @Test
    void findPassengerById_exception_passID_is_null(){
        when(eastPassengerMongoTemplate.findById(null, Passenger.class)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> eastPassengerService.findPassengerById(null));
        assertEquals("Failed to find passenger by ID", exception.getMessage());
    }

    @Test
    void deletePassengerByID() {
        String passID="6630ba4534a44e7b63c63895";
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(passID)));
        when(eastPassengerMongoTemplate.remove(query, Passenger.class)).thenReturn(null);

        eastPassengerService.deletePassengerByID(passID);
        verify(eastPassengerMongoTemplate, times(1)).remove(query, Passenger.class);
    }

    @Test
    void deletePassengerByID_exception(){
        String passID = "6630ba4534a44e7b63c63895";
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(passID)));
        doThrow(new DataAccessException("...") {}).when(eastPassengerMongoTemplate).remove(query, Passenger.class);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eastPassengerService.deletePassengerByID(passID);
        });

        assertEquals("Failed to delete passenger by ID", exception.getMessage());
        verify(eastPassengerMongoTemplate, times(1)).remove(query, Passenger.class);
    }


    @Test
    void delete() {
        Passenger passenger = new Passenger();
        passenger.setId("6630ba4534a44e7b63c63891");

        when(eastPassengerMongoTemplate.findById("6630ba4534a44e7b63c63891", Passenger.class)).thenReturn(null);


        eastPassengerService.delete(passenger);
        verify(eastPassengerMongoTemplate,times(1)).remove(new Query(Criteria.where("_id").is(passenger.getId())), Passenger.class);
    }

    @Test
    public void delete_exception(){
        Passenger passenger = new Passenger();
        passenger.setId(null);
        Query query = Query.query(Criteria.where("_id").is(passenger.getId()));
        doThrow(new DataAccessException("...") {}).when(eastPassengerMongoTemplate).remove(query, Passenger.class);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eastPassengerService.delete(passenger);
        });

        // Additional assertions can be made here
        assertEquals("Failed to delete passenger", exception.getMessage());
        verify(eastPassengerMongoTemplate, times(1)).remove(query, Passenger.class);
    }

    @Test
    void getAllPassengersInTheRegion() {
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(new Passenger());
        passengers.add(new Passenger());

        when(eastPassengerMongoTemplate.findAll(Passenger.class)).thenReturn(passengers);

        List<Passenger> foundPassengers = eastPassengerService.getAllPassengersInTheRegion();
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

        when(eastPassengerMongoTemplate.save(passenger)).thenReturn(passenger);

        Passenger savedPassenger = eastPassengerService.addPassenger(passenger);

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
        when(eastPassengerMongoTemplate.findAll(Passenger.class)).thenThrow(new DataAccessException("...") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> eastPassengerService.getAllPassengersInTheRegion());

        assertEquals("Failed to find all passengers in region", exception.getMessage());
    }

    @Test
    public void savaPassenger_exception_passId_is_null() {
        Passenger passenger = new Passenger();
        passenger.setId(null);
        when(eastPassengerMongoTemplate.save(passenger)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> eastPassengerService.savePassenger(passenger));

        assertEquals("Failed to save passenger", exception.getMessage());
    }

    @Test
    public void savaPassenger_exception_passId_is_empty() {
        Passenger passenger = new Passenger();
        passenger.setId("");
        when(eastPassengerMongoTemplate.save(passenger)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> eastPassengerService.savePassenger(passenger));

        assertEquals("Failed to save passenger", exception.getMessage());
    }

    @Test
    public void savaPassenger_exception_pass_berth_preference_is_empty() {
        Passenger passenger = new Passenger();
        passenger.setBerthPreference("");
        when(eastPassengerMongoTemplate.save(passenger)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> eastPassengerService.savePassenger(passenger));

        assertEquals("Failed to save passenger", exception.getMessage());
    }

    @Test
    public void savaPassenger_exception_pass_berth_preference_is_null() {
        Passenger passenger = new Passenger();
        passenger.setBerthPreference(null);
        when(eastPassengerMongoTemplate.save(passenger)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,() -> eastPassengerService.savePassenger(passenger));

        assertEquals("Failed to save passenger", exception.getMessage());
    }

}