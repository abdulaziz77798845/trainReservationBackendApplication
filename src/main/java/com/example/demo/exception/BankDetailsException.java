package com.example.demo.exception;

public class BankDetailsException extends RuntimeException{
    public BankDetailsException(String message) {
        super(message);
    }
}