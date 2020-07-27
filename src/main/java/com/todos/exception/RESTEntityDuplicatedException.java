package com.todos.exception;

public class RESTEntityDuplicatedException extends RuntimeException {
    
    public RESTEntityDuplicatedException() {
    }
    
    public RESTEntityDuplicatedException(String message) {
        super(message);
    }
}
