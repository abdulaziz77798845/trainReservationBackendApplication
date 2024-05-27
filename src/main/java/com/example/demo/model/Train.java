package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Train")
public class Train {
    @Id
    private String id;
    @Indexed(unique = true)
    private String trainName;
    private String date;
    private String from;
    private String to;
    @DBRef
    private List<Payment> payments = new ArrayList<>();
    @DBRef
    private List<Passenger> bookedPassengers = new ArrayList<>();
    @DBRef
    private List<Passenger> lowerBerth  = new ArrayList<>();
    @DBRef
    private List<Passenger> upperBerth = new ArrayList<>();
    @DBRef
    private List<Passenger> middleBerth = new ArrayList<>();
    @DBRef
    private List<Passenger> racPassengers = new LinkedList<>();
    @DBRef
    private List<Passenger> waitingList = new LinkedList<>();
    private int lowerBerthCount;
    private int upperBerthCount;
    private int middleBerthCount;
    private int racCount;
    private int waitingCount;
    private double trainFee;
    private String boardingTime;
    private String droppingTime;
    private String homeRegion;
    private String fromRegion;
    private String toRegion;
}