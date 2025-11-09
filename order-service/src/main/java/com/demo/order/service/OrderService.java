package com.demo.order.service;

import com.demo.common.api.order.CreateOrderRequest;
import com.demo.common.api.order.OrderDTO;
import com.demo.common.api.product.ProductFacade;
import com.demo.order.entity.OrderEntity;
import com.demo.order.kafka.OrderProducer;
import com.demo.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // Dubbo reference to product-service
    @DubboReference(version = "1.0.0", timeout = 5000, check = false)
    private ProductFacade productFacade;

    @Autowired
    private OrderProducer orderProducer;

    /**
     * Create an order:
     * 1) Call product-service.decreaseStock(productId, qty)
     * 2) If success: persist order record within the same JVM transaction (local)
     *    (note: the product stock change already occurred in product-service's DB)
     */
    @Transactional
    public OrderDTO createOrder(CreateOrderRequest req) throws Exception {
        boolean ok = productFacade.decreaseStock(req.getProductId(), req.getQuantity());
        if (!ok) {
            throw new RuntimeException("Insufficient stock or product error");
        }

        // For demo: compute totalAmount = product.price * qty
        // Ideally productFacade would return product info including price; we can call getProductById
        var prod = productFacade.getProductById(req.getProductId());
        BigDecimal total = prod.getPrice().multiply(BigDecimal.valueOf(req.getQuantity()));

        OrderEntity order = OrderEntity.builder()
                .userId(req.getUserId())
                .productId(req.getProductId())
                .quantity(req.getQuantity())
                .totalAmount(total)
                .status("CREATED")
                .build();

        order = orderRepository.save(order);

        // async publish to Kafka
        orderProducer.sendOrderCreated(order);

        // build DTO
        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .build();
    }
}
