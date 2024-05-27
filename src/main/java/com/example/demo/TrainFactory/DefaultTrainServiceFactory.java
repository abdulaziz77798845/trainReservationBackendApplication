package com.example.demo.TrainFactory;

import com.example.demo.service.trainImpl.TrainService;
import com.example.demo.service.trainImpl.EastIndianTrainServiceImpl;
import com.example.demo.service.trainImpl.NorthIndianTrainServiceImpl;
import com.example.demo.service.trainImpl.SouthIndianTrainServiceImpl;
import com.example.demo.service.trainImpl.WestIndianTrainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTrainServiceFactory implements TrainFactory{

    @Autowired
    private NorthIndianTrainServiceImpl northIndianTrainService;

    @Autowired
    private SouthIndianTrainServiceImpl southIndianTrainService;
    
    @Autowired
    private EastIndianTrainServiceImpl eastIndianTrainService;

    @Autowired
    private WestIndianTrainServiceImpl westIndianTrainService;
    @Override
    public TrainService getTrainService(String trainRegion) {
        if ("NR".equals(trainRegion)){
            return northIndianTrainService;
        }
        else if ("SR".equals(trainRegion)){
            return southIndianTrainService;
        }
        else if ("ER".equals(trainRegion)){
            return eastIndianTrainService;
        }
        else if ("WR".equals(trainRegion)){
            return westIndianTrainService;
        }
        else {
            throw new IllegalArgumentException("Invalid region : "+trainRegion);
        }
    }
}