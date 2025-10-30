package com.demo.product;

import com.demo.product.entity.Product;
import com.demo.product.repository.ProductRepository;
import com.demo.product.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

//@SpringBootTest
//@ActiveProfiles("test")
public class ProductServiceTest {

//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Test
//    void testCreateAndFindProduct() {
//        Product p = new Product();
//        p.setName("Gaming Mouse");
//        p.setPrice(BigDecimal.valueOf(59.99));
//        p.setStock(50);
//        productRepository.save(p);
//
//        List<Product> all = productService.getAll();
//        Assertions.assertFalse(all.isEmpty());
//        Assertions.assertEquals("Gaming Mouse", all.get(0).getName());
//    }
}
