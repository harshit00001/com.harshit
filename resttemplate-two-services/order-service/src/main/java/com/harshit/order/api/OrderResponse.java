package com.harshit.order.api;

public record OrderResponse(String orderId, String sku, int qty, String status, int remainingStock) {
}
