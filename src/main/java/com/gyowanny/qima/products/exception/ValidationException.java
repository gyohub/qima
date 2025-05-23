package com.gyowanny.qima.products.exception;

import java.util.List;

public class ValidationException extends RuntimeException {
  private final List<String> errors;

  public ValidationException(List<String> errors) {
    super("Product validation failed");
    this.errors = errors;
  }

  public List<String> getErrors() {
    return errors;
  }
}