package com.example.demo.repo.paymentRepo;

import com.example.demo.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EastPaymentRepo extends MongoRepository<Payment,String> {
}
