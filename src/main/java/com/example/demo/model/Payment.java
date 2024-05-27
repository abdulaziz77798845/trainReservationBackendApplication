package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Payment")
public class Payment {
    @Id
    private String id;
    @Indexed(unique = true)
    private String trainName;
    private String trainId;
    private String passengerName;
    private String passengerId;
    private String berthPreference;
    private int seatNumber;
    private double amount;

    public Payment(String trainName, String trainId, String passengerName, String passengerId, String berthPreference, int seatNumber, double amount) {
        this.trainName = trainName;
        this.trainId = trainId;
        this.passengerName = passengerName;
        this.passengerId = passengerId;
        this.berthPreference = berthPreference;
        this.seatNumber = seatNumber;
        this.amount = amount;
    }
}