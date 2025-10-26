package com.demo.product;

import com.demo.common.api.product.ProductDTO;
import com.demo.common.api.product.ProductFacade;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class ProductFacadeTest {

    @DubboReference(check = false)
    private ProductFacade productFacade;

//    @Test
//    void testListProducts() {
//        List<ProductDTO> list = productFacade.getAllProducts();
//        Assertions.assertNotNull(list);
//    }
}
