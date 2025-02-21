package com.api_gateway.Exception;

public class InvalidUsername extends RuntimeException {
    public InvalidUsername(String message) {
        super(message);
    }
}
