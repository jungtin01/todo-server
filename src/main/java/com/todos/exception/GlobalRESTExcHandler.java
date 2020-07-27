package com.todos.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalRESTExcHandler {

    @ExceptionHandler(RESTEntityNotFoundException.class)
    public ResponseEntity<RESTExceptionResponse> entityNotFoundExcHandler(RESTEntityNotFoundException e) {
        final RESTExceptionResponse response =
            new RESTExceptionResponse(NOT_FOUND, e);
    
        return ResponseEntity.status(NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(RESTEntityDuplicatedException.class)
    public ResponseEntity<RESTExceptionResponse> entityDuplicateExcHandler(RESTEntityDuplicatedException e) {
        final RESTExceptionResponse response =
            new RESTExceptionResponse(HttpStatus.BAD_REQUEST, e);
        
        return ResponseEntity.badRequest().body(response);
    }
}
