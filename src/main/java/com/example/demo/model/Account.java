package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Account")
public class Account {
    @Id
    private String id;
    @Indexed(unique = true)
    private String accountHolderName;
    private String accountNumber;
    private String CVV;
    private Double balance;
}