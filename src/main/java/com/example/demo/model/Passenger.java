package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Passenger")
public class Passenger {
    @Id
    private String id;
    @Indexed(unique = true)
    private String passengerTrainName;
    private String passengerTrainId;
    private String passengerName;
    private int age;
    private String date;
    private String from;
    private String to;
    private String berthPreference;
    private String allowedPreference;
    private String boardingTime;
    private String droppingTime;
    private int seatNumber;
    private String emailId;
    private Payment bill;
    private Account account;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return id.equals(passenger.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}