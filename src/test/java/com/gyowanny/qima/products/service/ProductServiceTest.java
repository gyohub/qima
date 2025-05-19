package com.gyowanny.qima.products.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.gyowanny.qima.products.dto.ProductDTO;
import com.gyowanny.qima.products.entity.Category;
import com.gyowanny.qima.products.entity.Product;
import com.gyowanny.qima.products.repository.CategoryRepository;
import com.gyowanny.qima.products.repository.ProductRepository;
import com.gyowanny.qima.products.validator.impl.ProductDTOValidator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

  private ProductRepository productRepository;
  private CategoryRepository categoryRepository;
  private ProductDTOValidator productDtoValidator;
  private ProductService productService;

  @BeforeEach
  void setup() {
    productRepository = mock(ProductRepository.class);
    categoryRepository = mock(CategoryRepository.class);
    productDtoValidator = mock(ProductDTOValidator.class);
    productService = new ProductService(categoryRepository, productRepository, productDtoValidator);
  }

  @Test
  void shouldReturnAllProductsAsDTOs() {
    Category parent = new Category(1L, "Electronics", null);
    Category cat = new Category(2L, "Laptops", parent);
    Product p1 = new Product(1L, "MacBook", "Apple laptop", new BigDecimal("1999.99"), true, cat);
    Product p2 = new Product(2L, "iPhone", "Phone", new BigDecimal("999.99"), true, parent);

    when(productRepository.findAll()).thenReturn(List.of(p1, p2));

    List<ProductDTO> result = productService.findAll();

    assertThat(result).hasSize(2);
    assertThat(result.get(0).categoryPath()).isEqualTo("Electronics > Laptops");
    assertThat(result.get(1).categoryPath()).isEqualTo("Electronics");
  }

  @Test
  void shouldFilterProductsByName() {
    Category cat = new Category(1L, "Phones", null);
    Product match = new Product(3L, "Samsung Galaxy", "Android phone", new BigDecimal("599.99"), true, cat);

    when(productRepository.findByNameContainingIgnoreCase("samsung")).thenReturn(List.of(match));

    List<ProductDTO> result = productService.findByName("samsung");

    assertThat(result).hasSize(1);
    assertThat(result.get(0).name()).isEqualTo("Samsung Galaxy");
  }

  @Test
  void shouldSaveProductFromDTO() {
    Category category = new Category(5L, "Tablets", null);
    ProductDTO dto = new ProductDTO(null, "Tablet", "Android tablet", new BigDecimal("299.99"), true, null, 5L);

    Product savedEntity = new Product(9L, "Tablet", "Android tablet", new BigDecimal("299.99"), true, category);

    when(categoryRepository.findById(5L)).thenReturn(Optional.of(category));
    when(productRepository.save(any(Product.class))).thenReturn(savedEntity);

    ProductDTO result = productService.save(dto);

    verify(productDtoValidator).validate(dto);
    verify(productRepository).save(any(Product.class));
    assertThat(result.id()).isEqualTo(9L);
  }

  @Test
  void shouldDeleteById() {
    productService.delete(42L);
    verify(productRepository).deleteById(42L);
  }

  @Test
  void shouldConvertToDTOWithNestedCategory() {
    Category parent = new Category(1L, "Electronics", null);
    Category child = new Category(2L, "Wearables", parent);
    Product p = new Product(10L, "Watch", "Smartwatch", new BigDecimal("249.99"), true, child);

    ProductDTO dto = productService.toDTO(p);

    assertThat(dto.categoryPath()).isEqualTo("Electronics > Wearables");
    assertThat(dto.categoryId()).isEqualTo(2L);
    assertThat(dto.name()).isEqualTo("Watch");
  }
}