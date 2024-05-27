package com.example.demo.dto;

import com.example.demo.model.Account;
import lombok.*;
import org.springframework.stereotype.Component;

@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PassengerDTO {
	private String passengerTrainId;
	private String passengerName;
	private int age;
	private String date;
	private String from;
	private String to;
	private String berthPreference;
	private String emailId;
	private Account account;
}