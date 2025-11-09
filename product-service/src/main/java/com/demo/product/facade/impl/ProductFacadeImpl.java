package com.demo.product.facade.impl;

import com.demo.common.api.product.ProductDTO;
import com.demo.common.api.product.ProductFacade;
import com.demo.product.repository.ProductRepository;
import com.demo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import com.demo.product.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@DubboService(version = "1.0.0", timeout = 3000)
@RequiredArgsConstructor
public class ProductFacadeImpl implements ProductFacade {

    private final ProductRepository repo;
    private final ProductService productService;

    @Override
    public ProductDTO getProductById(Long id) {
        return repo.findById(id)
                .map(p -> new ProductDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStock()
                ))
                .orElse(null);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return repo.findAll().stream()
                .map(p -> new ProductDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStock()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Decrease stock in a transactional way on product-service side.
     * Uses ProductService to ensure cache invalidation.
     */
    @Override
    @Transactional
    public boolean decreaseStock(Long productId, int qty) {
        // load current product to check stock
        Product p = repo.findById(productId).orElse(null);
        if (p == null) return false;
        if (p.getStock() == null || p.getStock() < qty) {
            return false; // insufficient stock
        }
        // Use ProductService to reduce stock, which will handle cache invalidation
        try {
            productService.reduceStock(productId, qty);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private ProductDTO toDto(Product p) {
        return new ProductDTO(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getStock());
    }
}
