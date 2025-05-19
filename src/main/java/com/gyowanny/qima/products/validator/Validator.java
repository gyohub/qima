package com.gyowanny.qima.products.validator;

import com.gyowanny.qima.products.exception.ValidationException;

public interface Validator<T> {
  void validate(T target) throws ValidationException;
}
