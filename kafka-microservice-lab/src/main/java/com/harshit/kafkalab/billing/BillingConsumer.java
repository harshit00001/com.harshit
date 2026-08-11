package com.harshit.kafkalab.billing;

import com.harshit.kafkalab.model.OrderEvent;
import com.harshit.kafkalab.support.KafkaRecordDebug;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.harshit.kafkalab.config.KafkaConfig.ORDER_TOPIC;

/**
 * Billing microservice — consumer group {@code billing-service}.
 * Same topic as Inventory; this group gets its own copy of every message.
 */
@Service
public class BillingConsumer {

    public static final String GROUP_ID = "billing-service";

    @KafkaListener(topics = ORDER_TOPIC, groupId = GROUP_ID, concurrency = "2")
    public void onOrder(ConsumerRecord<String, OrderEvent> record) {
        // DEBUG: breakpoint here — inspect record.partition(), record.offset(), record.key()
        KafkaRecordDebug.logConsumerRecord("BillingConsumer", GROUP_ID, record);
        simulateCharge(record.value());
    }

    private void simulateCharge(OrderEvent event) {
        if (event == null) {
            return;
        }
        // pretend DB / payment gateway
    }
}
