package com.gyowanny.qima.products.controller;

import com.gyowanny.qima.products.entity.Category;
import com.gyowanny.qima.products.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@Import(CategoryControllerTest.Config.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CategoryService categoryService;

  @Test
  void shouldReturnListOfCategories() throws Exception {
    Category parent = new Category(1L, "Electronics", null);
    Category child = new Category(2L, "Phones", parent);

    given(categoryService.findAll()).willReturn(List.of(parent, child));

    mockMvc.perform(get("/api/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("Electronics")))
        .andExpect(jsonPath("$[1].name", is("Phones")));
  }

  @TestConfiguration
  static class Config {
    @Bean
    public CategoryService categoryService() {
      return org.mockito.Mockito.mock(CategoryService.class);
    }
  }
}