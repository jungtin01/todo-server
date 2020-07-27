package com.todos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Can't found entity")
public class RESTEntityNotFoundException extends RuntimeException {
    
    public RESTEntityNotFoundException() {
    }
    
    public RESTEntityNotFoundException(String message) {
        super(message);
    }
}
