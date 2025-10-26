package com.demo.product.controller;

import com.demo.product.entity.Product;
import com.demo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable("id") Long id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        productService.delete(id);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable("id") Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        return product;
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }
}
