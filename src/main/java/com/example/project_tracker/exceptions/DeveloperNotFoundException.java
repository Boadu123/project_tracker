package com.example.project_tracker.exceptions;

public class DeveloperNotFoundException extends RuntimeException {
    public DeveloperNotFoundException(String message) {
        super(message);
    }
}
