package com.gyowanny.qima.products.service;

import com.gyowanny.qima.products.dto.ProductDTO;
import com.gyowanny.qima.products.entity.Category;
import com.gyowanny.qima.products.entity.Product;
import com.gyowanny.qima.products.repository.CategoryRepository;
import com.gyowanny.qima.products.repository.ProductRepository;
import com.gyowanny.qima.products.validator.impl.ProductDTOValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final ProductDTOValidator productDtoValidator;

  @Autowired
  public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository,
      ProductDTOValidator productDtoValidator) {
    this.categoryRepository = categoryRepository;
    this.productRepository = productRepository;
    this.productDtoValidator = productDtoValidator;
  }

  public List<ProductDTO> findAll() {
    return productRepository.findAll().stream().map(this::toDTO).toList();
  }

  public ProductDTO toDTO(Product p) {
    return new ProductDTO(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.isAvailable(),
        buildCategoryPath(p.getCategory()), p.getCategory().getId());
  }

  public List<ProductDTO> findByName(String name) {
    return productRepository.findByNameContainingIgnoreCase(name)
        .stream()
        .map(this::toDTO)
        .toList();
  }

  private String buildCategoryPath(Category category) {
    List<String> path = new ArrayList<>();
    while (category != null) {
      path.add(category.getName());
      category = category.getParent();
    }
    Collections.reverse(path);
    return String.join(" > ", path);
  }

  public ProductDTO save(ProductDTO dto) {
    productDtoValidator.validate(dto);

    Product product = new Product();
    product.setName(dto.name());
    product.setDescription(dto.description());
    product.setPrice(dto.price());
    product.setAvailable(dto.available());
    product.setCategory(
        categoryRepository.findById(dto.categoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"))
    );

    return toDTO(productRepository.save(product));
  }


  public ProductDTO save(Product p) {
    return toDTO(productRepository.save(p));
  }

  public void delete(Long id) {
    productRepository.deleteById(id);
  }
}
