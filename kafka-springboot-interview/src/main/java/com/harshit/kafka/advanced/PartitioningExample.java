package com.harshit.kafka.advanced;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * KAFKA PARTITIONING - Interview Explanation
 * 
 * This class demonstrates how partitioning works in Kafka and why it's important.
 * Understanding partitioning is crucial for building scalable Kafka applications.
 * 
 * Interview Question: "What are Kafka partitions and why are they important?"
 * 
 * Answer Explanation:
 * 
 * Partitions are the fundamental unit of parallelism in Kafka. A topic is divided
 * into one or more partitions, and each partition is an ordered, immutable sequence
 * of messages. Partitions allow Kafka to scale horizontally and process messages
 * in parallel.
 * 
 * Key concepts:
 * 
 * 1. Message Ordering: Messages within a partition are ordered, but there's no
 *    ordering guarantee across partitions. If you need ordering for related messages,
 *    they must go to the same partition, which is achieved by using the same key.
 * 
 * 2. Parallelism: Multiple consumers in a consumer group can process different
 *    partitions simultaneously, increasing throughput.
 * 
 * 3. Scalability: You can add more partitions to a topic to increase parallelism,
 *    but you cannot decrease partitions (messages are distributed based on partition count).
 * 
 * 4. Key-Based Partitioning: Messages with the same key always go to the same
 *    partition, ensuring ordering for that key.
 */
@Slf4j
@Service
public class PartitioningExample {
    
    /**
     * Interview Question: "How does Kafka distribute messages across partitions?"
     * 
     * This listener shows how messages are distributed. Messages with the same key
     * will always go to the same partition, while messages without keys are
     * distributed in a round-robin fashion.
     */
    @KafkaListener(topics = "partitioned-topic", groupId = "partition-group")
    public void consumeFromPartition(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        
        log.info("📬 Message received from partition: {}", partition);
        log.info("   Key: {}", key);
        log.info("   Message: {}", message);
        
        // Interview Point: Messages with same key always come from same partition
        // This ensures ordering for messages with the same key
        if (key != null) {
            log.info("   Messages with key '{}' will always come from partition {}", key, partition);
        }
    }
    
    /**
     * Interview Question: "How do you ensure message ordering in Kafka?"
     * 
     * To ensure ordering, you need to:
     * 1. Use the same key for related messages
     * 2. Have only one consumer per partition in a consumer group
     * 3. Process messages sequentially within a partition
     */
    @KafkaListener(topics = "ordered-topic", groupId = "ordered-group")
    public void consumeOrderedMessages(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.OFFSET) long offset) {
        
        log.info("🔢 Ordered message:");
        log.info("   Partition: {}", partition);
        log.info("   Offset: {}", offset);
        log.info("   Key: {}", key);
        log.info("   Message: {}", message);
        
        // Interview Point: Within this partition, messages are processed in order
        // Messages with the same key will maintain their order
    }
    
    /**
     * Interview Question: "What happens when you have multiple consumers in a group?"
     * 
     * Kafka distributes partitions among consumers in the same group. If you have
     * 3 partitions and 3 consumers, each consumer gets one partition. If you have
     * more consumers than partitions, some consumers will be idle.
     */
    @KafkaListener(topics = "scalable-topic", groupId = "scalable-group")
    public void demonstrateConsumerGroupScaling(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        
        log.info("⚡ Processing message from partition {} in scalable-group", partition);
        log.info("   Message: {}", message);
        
        // Interview Point: This consumer might be processing partition 0
        // Another consumer in the same group might be processing partition 1
        // This allows parallel processing across partitions
    }
}

