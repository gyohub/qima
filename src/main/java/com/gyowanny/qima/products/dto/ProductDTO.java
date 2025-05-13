package com.gyowanny.qima.products.dto;

import java.math.BigDecimal;

public record ProductDTO(Long id, String name, String description, BigDecimal price,
                         boolean available, String categoryPath) {

}