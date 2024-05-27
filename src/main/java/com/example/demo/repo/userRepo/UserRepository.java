package com.example.demo.repo.userRepo;

import com.example.demo.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User,String> {
    @Query("{'userName': ?0}")
    Optional<User> findByUsername(String userName);
}
