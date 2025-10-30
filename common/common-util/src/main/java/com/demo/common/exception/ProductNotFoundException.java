package com.demo.common.exception;

public class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException(Long productId) {
        super(404, "Product not found: " + productId);
    }
}
