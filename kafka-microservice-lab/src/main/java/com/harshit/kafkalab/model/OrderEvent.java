package com.harshit.kafkalab.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Domain event published when an order is created (Order microservice).
 */
public record OrderEvent(
        String orderId,
        String customerId,
        String item,
        BigDecimal amount,
        Instant createdAt
) {
    public static OrderEvent create(String orderId, String customerId, String item, BigDecimal amount) {
        return new OrderEvent(orderId, customerId, item, amount, Instant.now());
    }
}
