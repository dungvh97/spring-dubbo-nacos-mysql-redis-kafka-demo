package com.demo.product.service.impl;

import com.demo.product.entity.Product;
import com.demo.product.repository.ProductRepository;
import com.demo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    @Override
    @CacheEvict(value = "allProducts", allEntries = true)
    @CachePut(value = "products", key = "#result.id")
    public Product create(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        return repo.save(product);
    }

    @Override
    @CacheEvict(value = "allProducts", allEntries = true)
    @CachePut(value = "products", key = "#p1.id")
    public Product update(Long id, Product product) {
        Product existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setUpdatedAt(LocalDateTime.now());
        return repo.save(existing);
    }

    @Override
    @CacheEvict(value = {"products", "allProducts"}, allEntries = true)
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Cacheable(value = "products", key = "#p0")
    public Product getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Cacheable(value = "allProducts")
    public List<Product> getAll() {
        System.out.println("Fetching product from DB");
        return repo.findAll();
    }

    @Override
    @CacheEvict(value = {"products", "allProducts"}, allEntries = true)
    public void reduceStock(Long productId, Integer quantity) {
        Product product = repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        product.setStock(product.getStock() - quantity);
        repo.save(product);
    }
}
