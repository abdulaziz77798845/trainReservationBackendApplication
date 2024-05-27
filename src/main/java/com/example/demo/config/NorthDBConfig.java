package com.example.demo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.example.demo.repo"},mongoTemplateRef = NorthDBConfig.MONGO_TEMPLATE)
public class NorthDBConfig {
    protected static final String MONGO_TEMPLATE = "northTemplate";
}
