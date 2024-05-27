package com.example.demo.service.user_service;


import com.example.demo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Qualifier("userTemplate")
    @Autowired
    private MongoTemplate userMongoTemplate;
    public void addUser(User user){
        userMongoTemplate.save(user);
    }
    public void removeUser(String userId){
        Query query = new Query(Criteria.where("_id").is(userId));
        userMongoTemplate.remove(query, User.class);
    }

    public void updateUser(User user){
        userMongoTemplate.save(user);
    }

    public void deleteUser(String userId){
        Query query = Query.query(Criteria.where("_id").is(userId));
        userMongoTemplate.remove(query,User.class);
    }

    public Optional<User> findByUserName(String userName){
        Query query = new Query(Criteria.where("username").is(userName));
        User user =userMongoTemplate.findOne(query, User.class);
        return Optional.ofNullable(user);
    }
}
