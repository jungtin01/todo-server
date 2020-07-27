package com.todos.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRESTValidationException extends ResponseEntityExceptionHandler {
    
    @Autowired
    private MessageSource messageSource;
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
    
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", status.name());
    
        final List<Map<String, Object>> errors = errorsTranslate(ex.getBindingResult().getFieldErrors(),
            ex.getBindingResult().getGlobalErrors(), request.getLocale());
        body.put("errors", errors);
        
        return new ResponseEntity<>(body, headers, status);
    }
    
    private List<Map<String, Object>> errorsTranslate(List<FieldError> fieldErrors,
        List<ObjectError> objectErrors, Locale locale) {
        List<Map<String, Object>> errors = new ArrayList<>();
        
        fieldErrors.forEach(err -> {
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("object", err.getObjectName());
            tempMap.put("field", err.getField());
            tempMap.put("defaultMessage", getErrorMessage(err, locale));
            tempMap.put("rejectValue", err.getRejectedValue());
            errors.add(tempMap);
        });
        
        objectErrors.forEach(err -> {
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("object", err.getObjectName());
            tempMap.put("defaultMessage", err.getDefaultMessage());
            errors.add(tempMap);
        });
        return errors;
    }
    
    private String getErrorMessage(FieldError err, Locale locale) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(messageSource.getMessage(err.getField(), null, locale));
            builder.append(" ");
            builder.append(messageSource.getMessage(err.getObjectName(), null, locale));
            builder.append(" ");
            builder.append(messageSource.getMessage(err.getCode(), null, locale));
    
            return builder.toString();
        } catch (NoSuchMessageException e) {
            return err.getDefaultMessage();
        }
    }
}
