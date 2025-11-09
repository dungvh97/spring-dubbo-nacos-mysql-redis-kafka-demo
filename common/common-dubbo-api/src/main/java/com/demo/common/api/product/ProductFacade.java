package com.demo.common.api.product;

import java.util.List;

public interface ProductFacade {
    ProductDTO getProductById(Long id);
    List<ProductDTO> getAllProducts();

    /**
     * Decrease stock for a product.
     *
     * @param productId the product id
     * @param qty quantity to decrease
     * @return true if successful, false if insufficient stock or other failure
     */
    boolean decreaseStock(Long productId, int qty);
}
