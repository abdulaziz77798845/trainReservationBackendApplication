package com.example.demo.mapper;

import com.example.demo.dto.PassengerDTO;
import com.example.demo.dto.TrainDTO;
import com.example.demo.model.Account;
import com.example.demo.model.Passenger;
import com.example.demo.model.Train;
import org.springframework.stereotype.Component;

public class Mapper {

	public static TrainDTO convertToTrainDTO(Train train) {
		TrainDTO trainDTO=new TrainDTO();
		trainDTO.setTrainName(train.getTrainName());
		trainDTO.setTrainFee(train.getTrainFee());
		trainDTO.setDate(train.getDate());
		trainDTO.setFrom(train.getFrom());
		trainDTO.setTo(train.getTo());
		trainDTO.setBoardingTime(train.getBoardingTime());
		trainDTO.setDroppingTime(train.getDroppingTime());
		trainDTO.setLowerBerthCount(train.getLowerBerthCount());
		trainDTO.setMiddleBerthCount(train.getMiddleBerthCount());
		trainDTO.setUpperBerthCount(train.getUpperBerthCount());
		trainDTO.setRacCount(train.getRacCount());
		trainDTO.setWaitingCount(train.getWaitingCount());
		trainDTO.setHomeRegion(train.getHomeRegion());
		trainDTO.setFromRegion(train.getFromRegion());
		trainDTO.setToRegion(train.getToRegion());
		return trainDTO;
	}
	
	
	public static PassengerDTO convertToPassengerDTO(Passenger passenger) {
		PassengerDTO passengerDTO = new PassengerDTO();
		passengerDTO.setPassengerTrainId(passenger.getPassengerTrainId());
		passengerDTO.setPassengerName(passenger.getPassengerName());
		passengerDTO.setAge(passenger.getAge());
		passengerDTO.setDate(passenger.getDate());
		passengerDTO.setFrom(passenger.getFrom());
		passengerDTO.setTo(passenger.getTo());
		passengerDTO.setBerthPreference(passenger.getBerthPreference());
		passengerDTO.setEmailId(passenger.getEmailId());

		return passengerDTO;	
	}
	
	public static Train convertToTrain(TrainDTO trainDTO) {
		Train train = new Train();
		train.setTrainName(trainDTO.getTrainName());
		train.setTrainFee(trainDTO.getTrainFee());
		train.setDate(trainDTO.getDate());
		train.setFrom(trainDTO.getFrom());
		train.setTo(trainDTO.getTo());
		train.setBoardingTime(trainDTO.getBoardingTime());
		train.setDroppingTime(trainDTO.getDroppingTime());
		train.setLowerBerthCount(trainDTO.getLowerBerthCount());
		train.setUpperBerthCount(trainDTO.getUpperBerthCount());
		train.setMiddleBerthCount(trainDTO.getMiddleBerthCount());
		train.setRacCount(trainDTO.getRacCount());
		train.setWaitingCount(trainDTO.getWaitingCount());
		train.setHomeRegion(trainDTO.getHomeRegion());
		train.setToRegion(trainDTO.getToRegion());
		train.setFromRegion(trainDTO.getFromRegion());
		return train;
	}
	
	public static Passenger convertToPassenger(PassengerDTO passengerDTO) {
		Passenger passenger = new Passenger();
		passenger.setPassengerTrainId(passengerDTO.getPassengerTrainId());
		passenger.setPassengerName(passengerDTO.getPassengerName());
		passenger.setAge(passengerDTO.getAge());
		passenger.setDate(passengerDTO.getDate());
		passenger.setFrom(passengerDTO.getFrom());
		passenger.setTo(passenger.getTo());
		passenger.setBerthPreference(passengerDTO.getBerthPreference());
		passenger.setEmailId(passengerDTO.getEmailId());

		Account account = new Account();
		account.setId(passengerDTO.getAccount().getId());
		account.setAccountNumber(passengerDTO.getAccount().getAccountNumber());
		account.setBalance(passengerDTO.getAccount().getBalance());
		account.setCVV(passengerDTO.getAccount().getCVV());
		account.setAccountHolderName(passengerDTO.getAccount().getAccountHolderName());
		passenger.setAccount(account);
		return passenger;
	}

}
