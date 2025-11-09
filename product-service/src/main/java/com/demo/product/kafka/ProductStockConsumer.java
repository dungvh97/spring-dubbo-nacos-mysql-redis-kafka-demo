package com.demo.product.kafka;

import com.demo.common.api.order.OrderDTO;
import com.demo.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductStockConsumer {

    @Autowired
    private ProductService productService;

    @KafkaListener(topics = "order_created", groupId = "product-stock-group")
    public void handleOrderCreated(String orderJson) throws JsonProcessingException {
        // parse orderJson to get productId and quantity
        OrderDTO order = new ObjectMapper().readValue(orderJson, OrderDTO.class);
        productService.reduceStock(order.getProductId(), order.getQuantity());
    }
}
