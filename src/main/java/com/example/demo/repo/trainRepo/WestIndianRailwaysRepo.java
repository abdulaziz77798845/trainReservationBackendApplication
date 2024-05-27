package com.example.demo.repo.trainRepo;

import com.example.demo.model.Train;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface WestIndianRailwaysRepo extends MongoRepository<Train,String> {
    @Query("{'trainName':?0}")
    Train findByTrainName(String trainName);
}
