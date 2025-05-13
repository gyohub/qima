package com.gyowanny.qima.products.service;

import com.gyowanny.qima.products.entity.Category;
import com.gyowanny.qima.products.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

  private CategoryRepository categoryRepository;
  private CategoryService categoryService;

  @BeforeEach
  void setUp() {
    categoryRepository = mock(CategoryRepository.class);
    categoryService = new CategoryService(categoryRepository);
  }

  @Test
  void shouldReturnAllCategories() {
    // Given
    Category electronics = new Category(1L, "Electronics", null);
    Category phones = new Category(2L, "Phones", electronics);
    when(categoryRepository.findAll()).thenReturn(List.of(electronics, phones));

    // When
    List<Category> result = categoryService.findAll();

    // Then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getName()).isEqualTo("Electronics");
    assertThat(result.get(1).getParent()).isEqualTo(electronics);
    verify(categoryRepository).findAll();
  }
}