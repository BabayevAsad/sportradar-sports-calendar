package com.AsadBabayev.sportradar_sports_calendar.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError<String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        String type = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        Object value = ex.getValue();

        String hint = "";
        if (type.equals("LocalDate")) {
            hint = " Expected format: YYYY-MM-DD.";
        } else if (type.equals("SportType")) {
            hint = " Expected values: FOOTBALL, BASKETBALL, TENNIS, ICE_HOCKEY.";
        }

        String message = String.format("Parameter '%s' with value '%s' could not be converted to type '%s'.%s",
                name, value, type, hint);

        return buildResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError<Map<String, List<String>>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String key = (error instanceof FieldError fieldError) ? fieldError.getField() : error.getObjectName();
            String message = error.getDefaultMessage();
            errorsMap.computeIfAbsent(key, k -> new ArrayList<>()).add(message);
        });

        return buildResponse(errorsMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError<String>> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError<Map<String, String>>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errorsMap = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            errorsMap.put(fieldName, violation.getMessage());
        });

        return buildResponse(errorsMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError<String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Database constraint violation occurred.";

        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("null value")) {
                message = "A required field is missing and cannot be null.";
            } else if (ex.getMessage().contains("duplicate key")) {
                message = "A record with this unique property already exists.";
            }
        }

        return buildResponse(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError<String>> handleAllOtherExceptions(Exception ex) {
        return buildResponse("An unexpected internal error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private <T> ResponseEntity<ApiError<T>> buildResponse(T errors, HttpStatus status) {
        ApiError<T> apiError = new ApiError<>(
                UUID.randomUUID().toString(),
                Instant.now(),
                status.value(),
                errors
        );
        return new ResponseEntity<>(apiError, status);
    }
}