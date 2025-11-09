package com.demo.order.controller;

import com.demo.common.api.order.CreateOrderRequest;
import com.demo.common.api.order.OrderDTO;
import com.demo.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest req) throws Exception {
        OrderDTO dto = orderService.createOrder(req);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        // Simple retrieval (optional)
        // implement as needed
        return ResponseEntity.notFound().build();
    }
}
