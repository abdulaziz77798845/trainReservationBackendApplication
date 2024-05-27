package com.example.demo.service.trainImpl;

import com.example.demo.model.Passenger;
import com.example.demo.model.Train;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface TrainService {
	Train addTrain(Train train);
	Train getTrainByName(String trainName);
	List<Passenger> getPassengersByTrainName(String trainName);
	Train updateTrain(Train train);
	List<Train> getAllTrains();
	Optional<Train> findTrainById(String trainID);
	void deleteTrain(String trainID);
	void saveTrain(Train train);
}