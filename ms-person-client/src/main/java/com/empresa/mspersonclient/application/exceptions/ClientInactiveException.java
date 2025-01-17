package com.empresa.mspersonclient.application.exceptions;

public class ClientInactiveException extends RuntimeException {
    public ClientInactiveException(String message) {
        super(message);
    }
}
