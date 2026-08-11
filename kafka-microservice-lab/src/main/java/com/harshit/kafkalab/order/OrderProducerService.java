package com.harshit.kafkalab.order;

import com.harshit.kafkalab.model.OrderEvent;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.harshit.kafkalab.config.KafkaConfig.ORDER_TOPIC;

/**
 * Order microservice — Kafka producer (writes events to topic partitions).
 */
@Service
public class OrderProducerService {

    private static final Logger log = LoggerFactory.getLogger(OrderProducerService.class);

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducerService(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Key = orderId so all events for one order land on the same partition (ordering).
     * Set breakpoints here and in {@link #logSendResult} to inspect metadata in debug.
     */
    public CompletableFuture<SendResult<String, OrderEvent>> publish(OrderEvent event) {
        String key = event.orderId();
        log.info("PRODUCER sending key={} orderId={} to topic={}", key, event.orderId(), ORDER_TOPIC);

        CompletableFuture<SendResult<String, OrderEvent>> future =
                kafkaTemplate.send(ORDER_TOPIC, key, event);

        future.whenComplete((result, ex) -> logSendResult(event, result, ex));
        return future;
    }

    private void logSendResult(OrderEvent event, SendResult<String, OrderEvent> result, Throwable ex) {
        if (ex != null) {
            log.error("PRODUCER failed orderId={}: {}", event.orderId(), ex.getMessage());
            return;
        }
        RecordMetadata meta = result.getRecordMetadata();
        log.info(
                "PRODUCER ack topic={} partition={} offset={} timestamp={} key={}",
                meta.topic(),
                meta.partition(),
                meta.offset(),
                meta.timestamp(),
                event.orderId());
    }
}
