package com.harshit.kafkalab.support;

import com.harshit.kafkalab.model.OrderEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shared logging for debug — inspect {@link ConsumerRecord} fields in the debugger.
 */
public final class KafkaRecordDebug {

    private static final Logger log = LoggerFactory.getLogger(KafkaRecordDebug.class);

    private KafkaRecordDebug() {
    }

    public static void logConsumerRecord(String serviceName, String consumerGroup, ConsumerRecord<String, OrderEvent> record) {
        OrderEvent event = record.value();
        log.info("""
                {} | group={}
                  topic={} partition={} offset={} timestamp={} key={}
                  event orderId={} customer={} item={} amount={}
                """,
                serviceName,
                consumerGroup,
                record.topic(),
                record.partition(),
                record.offset(),
                record.timestamp(),
                record.key(),
                event != null ? event.orderId() : null,
                event != null ? event.customerId() : null,
                event != null ? event.item() : null,
                event != null ? event.amount() : null);
    }
}
