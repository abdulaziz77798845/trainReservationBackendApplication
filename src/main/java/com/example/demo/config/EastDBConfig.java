package com.example.demo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.example.demo.repo"},mongoTemplateRef = EastDBConfig.MONGO_TEMPLATE)
public class EastDBConfig {
    protected static final String MONGO_TEMPLATE = "eastTemplate";
}