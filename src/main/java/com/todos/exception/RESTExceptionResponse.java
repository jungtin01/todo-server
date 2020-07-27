package com.todos.exception;

import java.time.LocalDate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Data
public class RESTExceptionResponse {
    private final LocalDate timestamp;
    private final int status;
    private final String error;
    private final String message;
    
    public RESTExceptionResponse(HttpStatus httpStatus, Exception e) {
        this.timestamp = LocalDate.now();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = e.getMessage();
    }
}
