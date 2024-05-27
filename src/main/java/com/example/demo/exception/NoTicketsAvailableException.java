package com.example.demo.exception;

public class NoTicketsAvailableException extends RuntimeException{
    public NoTicketsAvailableException(String message) {
        super(message);
    }
}