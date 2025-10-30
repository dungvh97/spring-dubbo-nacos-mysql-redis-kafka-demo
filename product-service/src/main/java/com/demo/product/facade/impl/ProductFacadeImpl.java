package com.demo.product.facade.impl;

import com.demo.common.api.product.ProductDTO;
import com.demo.common.api.product.ProductFacade;
import com.demo.product.repository.ProductRepository;
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
     */
    @Override
    @Transactional
    public boolean decreaseStock(Long productId, int qty) {
        // load current product
        Product p = repo.findById(productId).orElse(null);
        if (p == null) return false;
        if (p.getStock() == null || p.getStock() < qty) {
            return false; // insufficient stock
        }
        p.setStock(p.getStock() - qty);
        repo.save(p);
        return true;
    }

    private ProductDTO toDto(Product p) {
        return new ProductDTO(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getStock());
    }
}
