package com.empresa.msaccountmovements.infrastructure.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", Instant.now());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("message", "Error de validación");

        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("field", fieldError.getField());
                    error.put("message", fieldError.getDefaultMessage());
                    return error;
                })
                .collect(Collectors.toList());

        errorDetails.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", Instant.now());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetails.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NoSuchElementException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", Instant.now());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        errorDetails.put("message", ex.getMessage() != null ? ex.getMessage() : "No encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }
}
