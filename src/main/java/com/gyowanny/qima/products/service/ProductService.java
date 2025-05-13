package com.gyowanny.qima.products.service;

import com.gyowanny.qima.products.dto.ProductDTO;
import com.gyowanny.qima.products.entity.Category;
import com.gyowanny.qima.products.entity.Product;
import com.gyowanny.qima.products.repository.ProductRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<ProductDTO> findAll() {
    return productRepository.findAll().stream().map(this::toDTO).toList();
  }

  public ProductDTO toDTO(Product p) {
    return new ProductDTO(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.isAvailable(),
        buildCategoryPath(p.getCategory()));
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

  public Product save(Product p) {
    return productRepository.save(p);
  }

  public void delete(Long id) {
    productRepository.deleteById(id);
  }
}
