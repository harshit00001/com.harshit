package com.harshit.inventory.store;

import com.harshit.inventory.api.ReserveResponse;
import com.harshit.inventory.api.StockItem;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory stock so the demo has no database. Thread-safe because two order-service
 * calls can hit the same SKU at once.
 */
@Component
public class InventoryStore {

    private final Map<String, MutableStock> stock = new ConcurrentHashMap<>();

    public InventoryStore() {
        put("SKU-PHONE", "Pixel-class phone", 10);
        put("SKU-LAPTOP", "Java-dev laptop", 4);
        put("SKU-CABLE", "USB-C cable", 50);
    }

    public Collection<StockItem> all() {
        return stock.values().stream().map(MutableStock::snapshot).toList();
    }

    public StockItem get(String sku) {
        return required(sku).snapshot();
    }

    public ReserveResponse reserve(String sku, int qty) {
        if (qty <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "qty must be > 0");
        }
        MutableStock item = required(sku);
        synchronized (item) {
            if (item.availableQty < qty) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "not enough stock for " + sku + " (have " + item.availableQty + ", need " + qty + ")");
            }
            item.availableQty -= qty;
            return new ReserveResponse(sku, qty, item.availableQty, true);
        }
    }

    private MutableStock required(String sku) {
        MutableStock item = stock.get(sku.toUpperCase());
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "unknown sku " + sku);
        }
        return item;
    }

    private void put(String sku, String name, int qty) {
        stock.put(sku, new MutableStock(sku, name, qty));
    }

    private static final class MutableStock {
        private final String sku;
        private final String name;
        private int availableQty;

        private MutableStock(String sku, String name, int availableQty) {
            this.sku = sku;
            this.name = name;
            this.availableQty = availableQty;
        }

        private StockItem snapshot() {
            return new StockItem(sku, name, availableQty);
        }
    }
}
