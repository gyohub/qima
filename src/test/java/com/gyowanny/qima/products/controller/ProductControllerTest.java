package com.gyowanny.qima.products.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyowanny.qima.products.entity.Category;
import com.gyowanny.qima.products.entity.Product;
import com.gyowanny.qima.products.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false) // disable Spring Security for test
@Import(ProductControllerTest.Config.class)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ProductService productService;

  @Test
  void shouldReturnAllProducts() throws Exception {
    Product product1 = new Product(1L, "MacBook Pro", "Apple laptop", new BigDecimal("1999.99"),
        true, null);
    Product product2 = new Product(2L, "iPhone", "Latest iPhone", new BigDecimal("999.99"), true,
        null);

    given(productService.findAll()).willReturn(List.of(
        new com.gyowanny.qima.products.dto.ProductDTO(1L, "MacBook Pro", "Apple laptop",
            new BigDecimal("1999.99"), true, "Electronics > Laptops", 2L),
        new com.gyowanny.qima.products.dto.ProductDTO(2L, "iPhone", "Latest iPhone",
            new BigDecimal("999.99"), true, "Electronics > Phones", 3L)
    ));

    mockMvc.perform(get("/api/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("MacBook Pro")))
        .andExpect(jsonPath("$[1].name", is("iPhone")));
  }

  @Test
  void shouldCreateProduct() throws Exception {
    Product input = new Product(null, "New Product", "Test", new BigDecimal("123.45"), true,
        new Category(1L, "Electronics", null));
    Product saved = new Product(10L, "New Product", "Test", new BigDecimal("123.45"), true,
        new Category(1L, "Electronics", null));

    given(productService.save(Mockito.any(Product.class))).willReturn(saved);

    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(10)))
        .andExpect(jsonPath("$.name", is("New Product")));
  }

  @Test
  void shouldUpdateProduct() throws Exception {
    Product updated = new Product(5L, "Updated", "Updated Desc", new BigDecimal("888.00"), false,
        new Category(2L, "Phones", null));

    given(productService.save(Mockito.any(Product.class))).willReturn(updated);

    mockMvc.perform(put("/api/products/5")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(5)))
        .andExpect(jsonPath("$.name", is("Updated")));
  }

  @Test
  void shouldDeleteProduct() throws Exception {
    mockMvc.perform(delete("/api/products/42"))
        .andExpect(status().isOk());

    verify(productService).delete(42L);
  }

  @TestConfiguration
  static class Config {

    @Bean
    public ProductService productService() {
      return org.mockito.Mockito.mock(ProductService.class);
    }
  }
}