package com.harshit.kafka.interview.q3;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * INTERVIEW QUESTION 3: How do Consumer Groups work in Kafka?
 * 
 * ANSWER:
 * Consumer Groups allow multiple consumers to work together to process messages from a topic.
 * 
 * KEY CONCEPTS:
 * - Same Group ID = Same Consumer Group
 * - Kafka distributes partitions among consumers in the same group
 * - Each partition is consumed by only ONE consumer in a group
 * - If consumers > partitions, some consumers will be idle
 * - If consumers < partitions, some consumers will handle multiple partitions
 * - Different groups can consume the same messages independently
 * 
 * EXAMPLE SCENARIO:
 * - Topic has 3 partitions (0, 1, 2)
 * - Consumer Group "group-1" has 2 consumers:
 *   - Consumer-1 handles partitions 0 and 1
 *   - Consumer-2 handles partition 2
 * - Consumer Group "group-2" has 1 consumer:
 *   - Consumer-3 handles all partitions 0, 1, 2
 * 
 * STEPS TO REPRODUCE:
 * 1. Create topic with 3 partitions:
 *    bin/kafka-topics.sh --create --topic orders-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
 * 2. Run ProducerExample to send messages
 * 3. Run this class multiple times (different terminal windows) with same GROUP_ID
 * 4. Observe how partitions are distributed among consumers
 * 5. Run with different GROUP_ID to see independent consumption
 */
public class ConsumerGroupExample {
    
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "orders-topic";
    private static final String GROUP_ID = "orders-consumer-group"; // Same group = share partitions
    
    public static void main(String[] args) {
        // Get consumer instance ID from command line or use default
        String consumerId = args.length > 0 ? args[0] : "consumer-1";
        
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        
        // Optional: Set session timeout for faster rebalancing
        properties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        
        try {
            consumer.subscribe(Collections.singletonList(TOPIC_NAME));
            System.out.println("=========================================");
            System.out.println("Consumer ID: " + consumerId);
            System.out.println("Consumer Group: " + GROUP_ID);
            System.out.println("Topic: " + TOPIC_NAME);
            System.out.println("Waiting for messages...");
            System.out.println("=========================================\n");
            
            int messageCount = 0;
            
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                
                if (!records.isEmpty()) {
                    System.out.println("\n[" + consumerId + "] Received " + records.count() + " message(s)");
                    
                    for (ConsumerRecord<String, String> record : records) {
                        messageCount++;
                        System.out.println("[" + consumerId + "] Message #" + messageCount);
                        System.out.println("  Partition: " + record.partition());
                        System.out.println("  Offset: " + record.offset());
                        System.out.println("  Key: " + record.key());
                        System.out.println("  Value: " + record.value());
                        
                        // Simulate processing
                        processOrder(record);
                    }
                    
                    consumer.commitSync();
                    System.out.println("[" + consumerId + "] Offsets committed");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error in consumer [" + consumerId + "]: " + e.getMessage());
            e.printStackTrace();
        } finally {
            consumer.close();
            System.out.println("[" + consumerId + "] Consumer closed");
        }
    }
    
    private static void processOrder(ConsumerRecord<String, String> record) {
        try {
            // Simulate order processing
            Thread.sleep(200);
            System.out.println("  ✓ Order processed successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

