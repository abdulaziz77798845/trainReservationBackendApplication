package com.example.demo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.example.demo.repo"},mongoTemplateRef = UserDBConfig.MONGO_TEMPLATE)
public class UserDBConfig {
    protected static final String MONGO_TEMPLATE = "userTemplate";
}
