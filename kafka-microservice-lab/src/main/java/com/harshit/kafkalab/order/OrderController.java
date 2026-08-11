package com.harshit.kafkalab.order;

import com.harshit.kafkalab.model.OrderEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderProducerService producerService;

    public OrderController(OrderProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createOrder(@RequestBody CreateOrderRequest request) {
        String orderId = request.orderId() != null && !request.orderId().isBlank()
                ? request.orderId()
                : UUID.randomUUID().toString().substring(0, 8);

        OrderEvent event = OrderEvent.create(
                orderId,
                request.customerId(),
                request.item(),
                request.amount());

        return producerService.publish(event).thenApply(sendResult -> {
            var meta = sendResult.getRecordMetadata();
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("message", "Order event published");
            body.put("orderId", orderId);
            body.put("kafkaTopic", meta.topic());
            body.put("kafkaPartition", meta.partition());
            body.put("kafkaOffset", meta.offset());
            body.put("hint", "Set breakpoint in BillingConsumer / InventoryConsumer to inspect ConsumerRecord");
            return ResponseEntity.accepted().body(body);
        });
    }

    @GetMapping("/help")
    public Map<String, String> help() {
        return Map.of(
                "POST /api/orders",
                "Body: {\"customerId\":\"c1\",\"item\":\"SIM\",\"amount\":99.99,\"orderId\":\"optional-same-key-for-partition-demo\"}",
                "kafka-ui",
                "http://localhost:8080 — view topics, partitions, offsets",
                "debug",
                "Breakpoint: OrderProducerService.publish, BillingConsumer.onOrder, InventoryConsumer.onOrder");
    }

    public record CreateOrderRequest(
            String orderId,
            String customerId,
            String item,
            BigDecimal amount) {
    }
}
