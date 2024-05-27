package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrainDTO {
	private String trainName;
	private String date;
	private String from;
	private String to;
	private int lowerBerthCount;
	private int middleBerthCount;
	private int upperBerthCount;
	private int racCount;
	private int waitingCount;
	private double trainFee;
	private String boardingTime;
	private String droppingTime;
	private String homeRegion;
	private String fromRegion;
	private String toRegion;
}

