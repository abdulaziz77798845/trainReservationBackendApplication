package com.example.demo.exception;

public class InvalidDetailsException  extends RuntimeException{
    public InvalidDetailsException(String message) {
        super(message);
    }
}