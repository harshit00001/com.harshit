package com.harshit.kafka.interview.q2;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * INTERVIEW QUESTION 2: How do you create a basic Kafka Consumer in Java?
 * 
 * ANSWER:
 * To create a Kafka Consumer, you need to:
 * 1. Configure consumer properties (bootstrap servers, deserializers, group ID)
 * 2. Create a KafkaConsumer instance
 * 3. Subscribe to one or more topics
 * 4. Poll for messages in a loop
 * 5. Process the messages
 * 6. Commit offsets (manual or automatic)
 * 7. Close the consumer when done
 * 
 * KEY CONCEPTS:
 * - Consumer Group: Consumers with same group ID share partition processing
 * - Deserializers: Convert bytes back to objects (StringDeserializer for strings)
 * - poll(): Fetches messages, blocks if no messages available
 * - Offset: Position in partition, tracks what's been consumed
 * - auto-offset-reset: What to do when no offset exists (earliest/latest)
 * 
 * STEPS TO REPRODUCE:
 * 1. Ensure Kafka is running (see BasicProducerExample)
 * 2. Ensure topic exists with messages (run BasicProducerExample first)
 * 3. Run this class: java BasicConsumerExample
 * 4. You should see messages being consumed
 * 5. Run multiple instances to see consumer group behavior
 */
public class BasicConsumerExample {
    
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "basic-topic";
    private static final String GROUP_ID = "basic-consumer-group";
    
    public static void main(String[] args) {
        // Step 1: Configure Consumer Properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName());
        
        // Important: Where to start reading when no offset exists
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        // Disable auto-commit for manual control
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        
        // Step 2: Create KafkaConsumer Instance
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        
        try {
            // Step 3: Subscribe to Topics
            consumer.subscribe(Collections.singletonList(TOPIC_NAME));
            System.out.println("Subscribed to topic: " + TOPIC_NAME);
            System.out.println("Consumer Group: " + GROUP_ID);
            System.out.println("Waiting for messages...\n");
            
            // Step 4: Poll for Messages (infinite loop)
            while (true) {
                // Poll for messages (blocks for up to 1 second)
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                
                // Step 5: Process Messages
                if (records.isEmpty()) {
                    System.out.println("No messages received, continuing to poll...");
                    continue;
                }
                
                System.out.println("Received " + records.count() + " message(s)");
                
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("=========================================");
                    System.out.println("Topic: " + record.topic());
                    System.out.println("Partition: " + record.partition());
                    System.out.println("Offset: " + record.offset());
                    System.out.println("Key: " + record.key());
                    System.out.println("Value: " + record.value());
                    System.out.println("Timestamp: " + record.timestamp());
                    System.out.println("=========================================\n");
                    
                    // Simulate message processing
                    processMessage(record);
                }
                
                // Step 6: Commit Offsets (Manual Commit)
                // This tells Kafka we've successfully processed these messages
                consumer.commitSync();
                System.out.println("Offsets committed successfully\n");
            }
            
        } catch (Exception e) {
            System.err.println("Error in consumer: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Step 7: Close Consumer
            consumer.close();
            System.out.println("Consumer closed");
        }
    }
    
    private static void processMessage(ConsumerRecord<String, String> record) {
        // Simulate business logic processing
        try {
            Thread.sleep(100); // Simulate processing time
            System.out.println("Message processed: " + record.value());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

