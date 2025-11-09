package com.demo.order.kafka;

import com.demo.order.entity.OrderEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final String TOPIC = "order_created";

    public void sendOrderCreated(OrderEntity order) throws Exception {
        String orderJson = objectMapper.writeValueAsString(order);
        kafkaTemplate.send(TOPIC, orderJson);
    }
}