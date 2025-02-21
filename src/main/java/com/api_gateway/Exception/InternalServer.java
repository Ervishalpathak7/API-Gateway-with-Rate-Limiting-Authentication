package com.api_gateway.Exception;

public class InternalServer extends RuntimeException {
    public InternalServer(String message) {
        super(message);
    }

}
