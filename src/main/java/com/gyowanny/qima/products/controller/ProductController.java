package com.gyowanny.qima.products.controller;

import com.gyowanny.qima.products.dto.ProductDTO;
import com.gyowanny.qima.products.entity.Product;
import com.gyowanny.qima.products.service.ProductService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping
  public List<ProductDTO> all() {
    return service.findAll();
  }

  @PostMapping
  public Product create(@RequestBody Product product) {
    return service.save(product);
  }

  @PutMapping("/{id}")
  public Product update(@PathVariable Long id, @RequestBody Product product) {
    product.setId(id);
    return service.save(product);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}