package com.example.demo.config;


import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfiguration {

    @Primary
    @Bean(name = "northDBProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.north")
    public MongoProperties getNorthProps() throws Exception{
        return new MongoProperties();
    }

    @Bean(name = "southDBProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.south")
    public MongoProperties getSouthProps() throws Exception {
        return new MongoProperties();
    }

    @Bean(name = "westDBProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.west")
    public MongoProperties getWestProps() throws Exception {
        return new MongoProperties();
    }


    @Bean(name = "eastDBProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.east")
    public MongoProperties getEastProps() throws Exception {
        return new MongoProperties();
    }

    @Bean(name = "userDBProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.user")
    public MongoProperties getUserProps() throws Exception{
        return new MongoProperties();
    }





    @Primary
    @Bean(name = "northTemplate")
    public MongoTemplate northMongoTemplate() throws Exception{
        return new MongoTemplate(northMongoDatabaseFactory(getNorthProps()));
    }

    @Bean(name = "southTemplate")
    public MongoTemplate southMongoTemplate() throws Exception{
        return new MongoTemplate(southMongoDatabaseFactory(getSouthProps()));
    }

    @Bean(name = "westTemplate")
    public MongoTemplate westMongoTemplate() throws Exception{
        return new MongoTemplate(westMongoDatabaseFactory(getWestProps()));
    }

    @Bean(name = "eastTemplate")
    public MongoTemplate eastMongoTemplate() throws Exception{
        return new MongoTemplate(eastMongoDatabaseFactory(getEastProps()));
    }


    @Bean(name = "userTemplate")
    public MongoTemplate userMongoTemplate() throws Exception {
        return new MongoTemplate(userMongoDatabaseFactory(getUserProps()));
    }

    @Primary
    @Bean
    public MongoDatabaseFactory northMongoDatabaseFactory(MongoProperties mongoProperties) throws Exception{
        return new SimpleMongoClientDatabaseFactory(
                mongoProperties.getUri()
        );
    }

    @Bean
    public MongoDatabaseFactory southMongoDatabaseFactory(MongoProperties mongoProperties) throws Exception{
        return new SimpleMongoClientDatabaseFactory(
                mongoProperties.getUri()
        );
    }

    @Bean
    public MongoDatabaseFactory westMongoDatabaseFactory(MongoProperties mongoProperties) throws Exception{
        return new SimpleMongoClientDatabaseFactory(
                mongoProperties.getUri()
        );
    }

    @Bean
    public MongoDatabaseFactory eastMongoDatabaseFactory(MongoProperties mongoProperties) throws Exception{
        return new SimpleMongoClientDatabaseFactory(
                mongoProperties.getUri()
        );
    }


    @Bean
    public MongoDatabaseFactory userMongoDatabaseFactory(MongoProperties mongoProperties) throws Exception{
        return new SimpleMongoClientDatabaseFactory(
                mongoProperties.getUri()
        );
    }

}
