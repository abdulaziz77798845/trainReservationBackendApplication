package com.example.demo.TrainFactory;

import com.example.demo.service.trainImpl.TrainService;


public interface TrainFactory {
    TrainService getTrainService(String trainRegion);
}
