package com.example.demo.service.paymentImpl;

import com.example.demo.model.Passenger;
import com.example.demo.model.Payment;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SouthPaymentServiceTest {

    @Mock
    @Qualifier("southTemplate")
    private MongoTemplate southPaymentMongoTemplate;

    @InjectMocks
    private SouthPaymentService southPaymentService;


    private Payment payment;
    private String paymentId;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        payment = new Payment();
        paymentId = new ObjectId().toString();
        payment.setId(paymentId);
    }

    @Test
    public void addPayment(){

        when(southPaymentMongoTemplate.save(payment)).thenReturn(payment);

        Payment expectedPayment = southPaymentService.addPayment(payment);

        assertEquals(expectedPayment, payment);
        verify(southPaymentMongoTemplate,times(1)).insert(payment);
    }

    @Test
    public void findPaymentById(){
        when(southPaymentMongoTemplate.findById(paymentId, Payment.class)).thenReturn(payment);

        Optional<Payment> expectedPayment = southPaymentService.findPaymentById(paymentId);

        assertTrue(expectedPayment.isPresent());
        assertEquals(expectedPayment.get(), payment);
        verify(southPaymentMongoTemplate,times(1)).findById(paymentId, Payment.class);
    }

    @Test
    public void deletePaymentById(){

        when(southPaymentMongoTemplate.remove(any(Query.class), eq(Payment.class))).thenReturn(null);

        southPaymentService.deletePaymentByID(paymentId);

        verify(southPaymentMongoTemplate,times(1)).remove(any(Query.class),eq(Payment.class));
    }

    @Test
    public void deletePayment(){
        when(southPaymentMongoTemplate.remove(any(Query.class), eq(Payment.class))).thenReturn(null);

        southPaymentService.delete(payment);
        verify(southPaymentMongoTemplate,times(1)).remove(any(Query.class),eq(Payment.class));
    }

    @Test
    public void getAllPaymentsByRegion(){
        List<Payment> actualPayments = Arrays.asList(payment);
        when(southPaymentMongoTemplate.findAll(Payment.class)).thenReturn(actualPayments);

        List<Payment> expectedPayments = southPaymentService.getAllPaymentInTheRegion();

        assertEquals(actualPayments, expectedPayments);
        verify(southPaymentMongoTemplate,times(1)).findAll(Payment.class);
    }

    @Test
    public void getAllPaymentsByRegion_exception(){

        when(southPaymentMongoTemplate.findAll(Payment.class)).thenThrow(new DataAccessException("...") {});

        RuntimeException runtimeException = assertThrows(RuntimeException.class,()-> southPaymentService.getAllPaymentInTheRegion());
        assertEquals("Unable to find payments in region",runtimeException.getMessage());
        verify(southPaymentMongoTemplate,times(1)).findAll(Payment.class);
    }

    @Test
    public void savePayment(){
        southPaymentService.savePayment(payment);
        verify(southPaymentMongoTemplate,times(1)).save(payment);
    }

    @Test
    public void savePayment_exception(){
        when(southPaymentMongoTemplate.save(payment)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,()-> southPaymentService.savePayment(payment));
        assertEquals("Unable to save payment",exception.getMessage());
        verify(southPaymentMongoTemplate,times(1)).save(payment);
    }

    @Test
    public void delete_exception(){
        payment.setId(null);
        Query query = Query.query(Criteria.where("_id").is(payment.getId()));
        doThrow(new DataAccessException("...") {}).when(southPaymentMongoTemplate).remove(query, Payment.class);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            southPaymentService.delete(payment);
        });


        assertEquals("Unable to delete payment", exception.getMessage());
        verify(southPaymentMongoTemplate, times(1)).remove(query, Payment.class);
    }

    @Test
    void deletePassengerByID_exception(){
        String passID = "6630ba4534a44e7b63c63895";
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(passID)));
        doThrow(new DataAccessException("...") {}).when(southPaymentMongoTemplate).remove(query, Payment.class);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            southPaymentService.deletePaymentByID(passID);
        });

        assertEquals("Unable to delete payment by ID", exception.getMessage());
        verify(southPaymentMongoTemplate, times(1)).remove(query, Payment.class);
    }
}