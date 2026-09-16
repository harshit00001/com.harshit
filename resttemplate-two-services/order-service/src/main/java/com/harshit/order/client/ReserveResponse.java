package com.harshit.order.client;

public record ReserveResponse(String sku, int reservedQty, int remainingQty, boolean reserved) {
}
