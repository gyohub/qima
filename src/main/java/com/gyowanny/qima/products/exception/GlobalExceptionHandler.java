package com.gyowanny.qima.products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> handleValidationException(ValidationException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("message", ex.getMessage());
    body.put("errors", ex.getErrors());
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}