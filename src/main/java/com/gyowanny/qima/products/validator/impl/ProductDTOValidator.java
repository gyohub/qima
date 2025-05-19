package com.gyowanny.qima.products.validator.impl;

import com.gyowanny.qima.products.dto.ProductDTO;

import com.gyowanny.qima.products.exception.ValidationException;
import com.gyowanny.qima.products.validator.Validator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOValidator implements Validator<ProductDTO> {

  @Override
  public void validate(ProductDTO dto) {
    List<String> errors = new ArrayList<>();

    if (dto.name() == null || dto.name().trim().isEmpty()) {
      errors.add("Product name is required.");
    }

    if (dto.price() == null) {
      errors.add("Product price is required.");
    } else if (dto.price().compareTo(BigDecimal.ZERO) < 0) {
      errors.add("Product price must be zero or positive.");
    }

    if (dto.categoryId() == null) {
      errors.add("Product category must be selected.");
    }

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }
}
