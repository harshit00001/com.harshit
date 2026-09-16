package com.harshit.inventory.api;

import com.harshit.inventory.store.InventoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryStore store;

    public InventoryController(InventoryStore store) {
        this.store = store;
    }

    @GetMapping
    public Collection<StockItem> list() {
        return store.all();
    }

    @GetMapping("/{sku}")
    public StockItem get(@PathVariable String sku) {
        log.info("GET stock sku={}", sku);
        return store.get(sku);
    }

    @PostMapping("/{sku}/reserve")
    public ReserveResponse reserve(@PathVariable String sku, @RequestBody ReserveRequest request) {
        log.info("RESERVE sku={} qty={}", sku, request.qty());
        return store.reserve(sku, request.qty());
    }
}
