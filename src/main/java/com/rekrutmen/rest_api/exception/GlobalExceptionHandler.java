package com.rekrutmen.rest_api.exception;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Prepare detailed validation error messages
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        // Log validation errors
        logger.warn("Validation failed: {}", validationErrors);

        // Create response wrapper
        ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(
                "400",
                "Bad Request",
                validationErrors
        );

        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }
}
