package com.example.demo.exception;

public class TrainNotExistException extends RuntimeException{
    public TrainNotExistException(String message){
        super(message);
    }
}