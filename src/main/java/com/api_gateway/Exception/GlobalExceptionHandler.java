package com.api_gateway.Exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFound.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFound ex) {
        log.error("Username not found: {}", ex.getMessage());
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidPassword.class)
    public ResponseEntity<String> handleInvalidPassword(InvalidPassword ex) {
        log.error("Invalid Password: {}", ex.getMessage());
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidUsername.class)
    public ResponseEntity<String> handleInvalidUsername(InvalidUsername ex) {
        log.error("Invalid Username: {}", ex.getMessage());
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler(InternalServer.class)
    public ResponseEntity<String> handleInternalServer(InternalServer ex) {
        log.error("Server Error: {}", ex.getMessage());
        return ResponseEntity.status(500).body(ex.getMessage());
    }

}
