package com.example.demo.service.paymentImpl;


import com.example.demo.model.Payment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface PaymentService{
    Payment addPayment(Payment payment);
    Optional<Payment> findPaymentById(String payID);
    void deletePaymentByID(String payID);
    void delete(Payment payment);
    List<Payment> getAllPaymentInTheRegion();
    void savePayment(Payment payment);
}