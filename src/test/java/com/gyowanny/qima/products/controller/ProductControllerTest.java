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
import com.gyowanny.qima.products.dto.ProductDTO;
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
@AutoConfigureMockMvc(addFilters = false)
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
    ProductDTO dto1 = new ProductDTO(1L, "MacBook Pro", "Apple laptop",
        new BigDecimal("1999.99"), true, "Electronics > Laptops", 2L);
    ProductDTO dto2 = new ProductDTO(2L, "iPhone", "Latest iPhone",
        new BigDecimal("999.99"), true, "Electronics > Phones", 3L);

    given(productService.findAll()).willReturn(List.of(dto1, dto2));

    mockMvc.perform(get("/api/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("MacBook Pro")))
        .andExpect(jsonPath("$[1].name", is("iPhone")));
  }

  @Test
  void shouldCreateProduct() throws Exception {
    ProductDTO requestDto = new ProductDTO(null, "New Product", "Test",
        new BigDecimal("123.45"), true, null, 1L);
    ProductDTO savedDto = new ProductDTO(10L, "New Product", "Test",
        new BigDecimal("123.45"), true, "Electronics", 1L);

    given(productService.save(Mockito.any(ProductDTO.class))).willReturn(savedDto);

    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(10)))
        .andExpect(jsonPath("$.name", is("New Product")));
  }

  @Test
  void shouldUpdateProduct() throws Exception {
    ProductDTO input = new ProductDTO(null, "Updated", "Updated Desc",
        new BigDecimal("888.00"), false, null, 2L);
    ProductDTO result = new ProductDTO(5L, "Updated", "Updated Desc",
        new BigDecimal("888.00"), false, "Phones", 2L);

    given(productService.save(Mockito.any(ProductDTO.class))).willReturn(result);

    mockMvc.perform(put("/api/products/5")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input)))
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
      return Mockito.mock(ProductService.class);
    }
  }
}