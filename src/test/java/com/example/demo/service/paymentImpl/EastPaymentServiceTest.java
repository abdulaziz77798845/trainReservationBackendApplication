package com.example.demo.service.paymentImpl;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EastPaymentServiceTest {
    @Mock
    @Qualifier("eastTemplate")
    private MongoTemplate eastPaymentMongoTemplate;

    @InjectMocks
    private EastPaymentService eastPaymentService;


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

        when(eastPaymentMongoTemplate.save(payment)).thenReturn(payment);

        Payment expectedPayment = eastPaymentService.addPayment(payment);

        assertEquals(expectedPayment, payment);
        verify(eastPaymentMongoTemplate,times(1)).insert(payment);
    }

    @Test
    public void findPaymentById(){
        when(eastPaymentMongoTemplate.findById(paymentId, Payment.class)).thenReturn(payment);

        Optional<Payment> expectedPayment = eastPaymentService.findPaymentById(paymentId);

        assertTrue(expectedPayment.isPresent());
        assertEquals(expectedPayment.get(), payment);
        verify(eastPaymentMongoTemplate,times(1)).findById(paymentId, Payment.class);
    }

    @Test
    public void deletePaymentById(){

        when(eastPaymentMongoTemplate.remove(any(Query.class), eq(Payment.class))).thenReturn(null);

        eastPaymentService.deletePaymentByID(paymentId);

        verify(eastPaymentMongoTemplate,times(1)).remove(any(Query.class),eq(Payment.class));
    }

    @Test
    public void deletePayment(){
        when(eastPaymentMongoTemplate.remove(any(Query.class), eq(Payment.class))).thenReturn(null);

        eastPaymentService.delete(payment);
        verify(eastPaymentMongoTemplate,times(1)).remove(any(Query.class),eq(Payment.class));
    }

    @Test
    public void getAllPaymentsByRegion(){
        List<Payment> actualPayments = Arrays.asList(payment);
        when(eastPaymentMongoTemplate.findAll(Payment.class)).thenReturn(actualPayments);

        List<Payment> expectedPayments = eastPaymentService.getAllPaymentInTheRegion();

        assertEquals(actualPayments, expectedPayments);
        verify(eastPaymentMongoTemplate,times(1)).findAll(Payment.class);
    }

    @Test
    public void getAllPaymentsByRegion_exception(){

        when(eastPaymentMongoTemplate.findAll(Payment.class)).thenThrow(new DataAccessException("...") {});

        RuntimeException runtimeException = assertThrows(RuntimeException.class,()-> eastPaymentService.getAllPaymentInTheRegion());
        assertEquals("Unable to find payments in region",runtimeException.getMessage());
        verify(eastPaymentMongoTemplate,times(1)).findAll(Payment.class);
    }

    @Test
    public void savePayment(){
        eastPaymentService.savePayment(payment);
        verify(eastPaymentMongoTemplate,times(1)).save(payment);
    }

    @Test
    public void savePayment_exception(){
        when(eastPaymentMongoTemplate.save(payment)).thenThrow(new DataAccessException("...") {});

        RuntimeException exception = assertThrows(RuntimeException.class,()-> eastPaymentService.savePayment(payment));
        assertEquals("Unable to save payment",exception.getMessage());
        verify(eastPaymentMongoTemplate,times(1)).save(payment);
    }

    @Test
    public void delete_exception(){
        payment.setId(null);
        Query query = Query.query(Criteria.where("_id").is(payment.getId()));
        doThrow(new DataAccessException("...") {}).when(eastPaymentMongoTemplate).remove(query, Payment.class);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eastPaymentService.delete(payment);
        });


        assertEquals("Unable to delete payment", exception.getMessage());
        verify(eastPaymentMongoTemplate, times(1)).remove(query, Payment.class);
    }

    @Test
    void deletePassengerByID_exception(){
        String passID = "6630ba4534a44e7b63c63895";
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(passID)));
        doThrow(new DataAccessException("...") {}).when(eastPaymentMongoTemplate).remove(query, Payment.class);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eastPaymentService.deletePaymentByID(passID);
        });

        assertEquals("Unable to delete payment by ID", exception.getMessage());
        verify(eastPaymentMongoTemplate, times(1)).remove(query, Payment.class);
    }
}