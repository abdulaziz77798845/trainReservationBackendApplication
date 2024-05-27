package com.example.demo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.example.demo.repo"},mongoTemplateRef = SouthDBConfig.MONGO_TEMPLATE)
public class SouthDBConfig {
    protected static final String MONGO_TEMPLATE = "southTemplate";
}
