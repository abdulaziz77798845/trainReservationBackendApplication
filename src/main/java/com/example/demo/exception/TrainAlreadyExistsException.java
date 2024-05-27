package com.example.demo.exception;

public class TrainAlreadyExistsException extends RuntimeException{
    public TrainAlreadyExistsException(String message){
        super(message);
    }
}