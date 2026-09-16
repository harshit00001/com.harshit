package com.harshit.order.service;

import com.harshit.order.api.OrderResponse;
import com.harshit.order.client.InventoryClient;
import com.harshit.order.client.ReserveResponse;
import com.harshit.order.client.StockItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final InventoryClient inventoryClient;
    private final List<OrderResponse> orders = new CopyOnWriteArrayList<>();

    public OrderService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    public OrderResponse place(String sku, int qty) {
        if (sku == null || sku.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sku is required");
        }
        if (qty <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "qty must be > 0");
        }

        // 1) Ask inventory whether the SKU exists and how much is on the shelf.
        StockItem stock = inventoryClient.getStock(sku);
        log.info("inventory GET returned sku={} available={}", stock.sku(), stock.availableQty());
        if (stock.availableQty() < qty) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "not enough stock (available=" + stock.availableQty() + ", requested=" + qty + ")");
        }

        // 2) Ask inventory to actually deduct stock (the write).
        ReserveResponse reserved = inventoryClient.reserve(sku, qty);
        log.info("inventory POST reserve remaining={}", reserved.remainingQty());

        OrderResponse order = new OrderResponse(
                "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                reserved.sku(),
                reserved.reservedQty(),
                "CONFIRMED",
                reserved.remainingQty());
        orders.add(order);
        return order;
    }

    public List<OrderResponse> all() {
        return new ArrayList<>(orders);
    }
}
