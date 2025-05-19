package com.gyowanny.qima.products.controller;

import com.gyowanny.qima.products.dto.ProductDTO;
import com.gyowanny.qima.products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Operations related to products")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(
      summary = "List all products",
      description = "Returns a list of products with optional filtering by name (case-insensitive)"
  )
  public List<ProductDTO> all(
      @Parameter(
          in = ParameterIn.QUERY,
          description = "Optional name filter (partial match)",
          example = "MacBook"
      )
      @RequestParam(required = false) String name
  ) {
    if (name != null && !name.isBlank()) {
      return service.findByName(name);
    }
    return service.findAll();
  }

  @PostMapping
  @Operation(summary = "Create a product", description = "Creates a new product and returns the saved entity")
  @ApiResponse(responseCode = "200", description = "Product created",
      content = @Content(schema = @Schema(implementation = ProductDTO.class)))
  public ProductDTO create(@RequestBody ProductDTO dto) {
    return service.save(dto);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a product", description = "Updates an existing product by ID")
  @ApiResponse(responseCode = "200", description = "Product updated",
      content = @Content(schema = @Schema(implementation = ProductDTO.class)))
  public ProductDTO update(@PathVariable Long id, @RequestBody ProductDTO dto) {
    return service.save(
        new ProductDTO(id, dto.name(), dto.description(), dto.price(), dto.available(),
            dto.categoryPath(), dto.categoryId()));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a product", description = "Deletes a product by ID")
  @ApiResponse(responseCode = "204", description = "Product deleted")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}