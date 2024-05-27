package com.example.demo.service.paymentImpl;

import com.example.demo.model.Payment;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SouthPaymentService implements PaymentService {
    @Qualifier("southTemplate")
    @Autowired
    private MongoTemplate southPayTemplate;

    public Payment addPayment(Payment payment) {
        try {
            southPayTemplate.insert(payment);
            return payment;
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Unable to add payment",exp);
        }
    }

    public Optional<Payment> findPaymentById(String payID){
        try {
            return Optional.ofNullable(southPayTemplate.findById(payID, Payment.class));
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Unable to find payment by ID",exp);
        }
    }

    public void deletePaymentByID(String payID){
        try {
            Query query = Query.query(Criteria.where("_id").is(new ObjectId(payID)));
            southPayTemplate.remove(query, Payment.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Unable to delete payment by ID",exp);
        }
    }

    public void delete(Payment payment){
        try {
            Query query = Query.query(Criteria.where("_id").is(payment.getId()));
            southPayTemplate.remove(query, Payment.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Unable to delete payment",exp);
        }
    }

    public List<Payment> getAllPaymentInTheRegion(){
        try {
            return southPayTemplate.findAll(Payment.class);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Unable to find payments in region",exp);
        }
    }

    public void savePayment(Payment payment){
        try {
            southPayTemplate.save(payment);
        }
        catch (DataAccessException exp){
            throw new RuntimeException("Unable to save payment",exp);
        }
    }
}
