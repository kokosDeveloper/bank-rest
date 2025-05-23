package com.example.bankcards.exception;

public class AlreadyProcessedException extends RuntimeException {
    public AlreadyProcessedException(String message) {
        super(message);
    }
}
