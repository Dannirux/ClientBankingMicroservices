package com.empresa.msaccountmovements.application.exceptions;

public class InvalidMovementException extends RuntimeException {
    public InvalidMovementException(String message) {
        super(message);
    }
}
