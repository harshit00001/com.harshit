package com.harshit.inventory.api;

public record ReserveResponse(String sku, int reservedQty, int remainingQty, boolean reserved) {
}
