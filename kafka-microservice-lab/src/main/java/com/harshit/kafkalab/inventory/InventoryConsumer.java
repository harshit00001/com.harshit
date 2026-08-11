package com.harshit.kafkalab.inventory;

import com.harshit.kafkalab.model.OrderEvent;
import com.harshit.kafkalab.support.KafkaRecordDebug;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.harshit.kafkalab.config.KafkaConfig.ORDER_TOPIC;

/**
 * Inventory microservice — consumer group {@code inventory-service}.
 */
@Service
public class InventoryConsumer {

    public static final String GROUP_ID = "inventory-service";

    @KafkaListener(topics = ORDER_TOPIC, groupId = GROUP_ID)
    public void onOrder(ConsumerRecord<String, OrderEvent> record) {
        // DEBUG: breakpoint here — compare offset with BillingConsumer for same event
        KafkaRecordDebug.logConsumerRecord("InventoryConsumer", GROUP_ID, record);
        simulateReserveStock(record.value());
    }

    private void simulateReserveStock(OrderEvent event) {
        if (event == null) {
            return;
        }
    }
}
