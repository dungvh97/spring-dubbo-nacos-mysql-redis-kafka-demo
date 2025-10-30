package com.demo.common.api.product;

import com.demo.common.api.ApiResponse;
import com.demo.common.api.PageResult;
import com.demo.common.exception.ProductNotFoundException;

import java.util.List;

public interface ProductFacade {
    ProductDTO getProductById(Long id) throws ProductNotFoundException;
    List<ProductDTO> getAllProducts();
}
