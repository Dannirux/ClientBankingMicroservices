package com.empresa.mspersonclient.infrastructure.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
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
        errorDetails.put("message", ex.getMessage() != null ? ex.getMessage() : "Cliente no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleUniqueConstraintViolation(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            String constraintMessage = ex.getCause().getMessage();
            String fieldName = extractFieldNameFromConstraint(constraintMessage);

            Map<String, String> response = new HashMap<>();
            response.put("timestamp", String.valueOf(Instant.now()));
            response.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
            response.put("message", "El valor del campo único ya está registrado.");
            response.put("field", fieldName);
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor."));
    }

    private String extractFieldNameFromConstraint(String constraintMessage) {
        if (constraintMessage.contains("PUBLIC")) {
            String[] parts = constraintMessage.split(" ON ");
            if (parts.length > 1) {
                String tableAndField = parts[1].split("\\(")[1];
                return tableAndField.replace(")", "").trim();
            }
        }
        return "campo_desconocido"; // Si no se puede determinar
    }
}
