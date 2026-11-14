package com.myFullstack.employee_management.shared;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;

@ControllerAdvice
public class GlobalExceptionResponse {

    /**
     * Handles NoResourceFoundException. 
     * This is typically thrown when a client requests a URL route that does not exist.
     * Returns a 404 NOT_FOUND status.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<GlobalResponse<Void>> handleNoResourceException(NoResourceFoundException ex) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem("Route not found: " + ex.getResourcePath()));
        return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles ResourceNotFoundException.
     * This custom exception is thrown when a specific resource (like an Employee ID) is not found in the database.
     * Returns a 404 NOT_FOUND status.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GlobalResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
        return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles BadRequestException.
     * This custom exception is thrown when the request violates business logic or has invalid parameters.
     * Returns a 400 BAD_REQUEST status.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GlobalResponse<Void>> handleBadRequestException(BadRequestException ex) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
        return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException.
     * This is thrown by Spring when @Valid validation fails on request bodies (e.g., an Employee with a blank name).
     * It maps all field errors to a list of ErrorItems and returns a 400 BAD_REQUEST status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<GlobalResponse.ErrorItem> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(new GlobalResponse.ErrorItem(fieldError.getField() + ": " + fieldError.getDefaultMessage()));
        }
        return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles HttpMessageNotReadableException.
     * This is thrown when the JSON request body is malformed or types don't match (e.g. invalid date format).
     * Returns a 400 BAD_REQUEST status.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem("Malformed JSON request or invalid data type"));
        return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles any other unhandled runtime Exception.
     * Acts as a global catch-all so the user doesn't see a raw server stack trace.
     * Returns a 500 INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalResponse<Void>> handleGeneralException(Exception ex) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem("An unexpected error occurred: " + ex.getMessage()));
        return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

