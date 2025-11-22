package com.harshit.kafka.basics;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * BASIC KAFKA CONSUMER - Interview Explanation
 * 
 * This class demonstrates how to create a basic Kafka consumer using the
 * standard Java Kafka client library. Understanding consumers is crucial for
 * building event-driven applications.
 * 
 * Interview Question: "How do you create a Kafka consumer in Java?"
 * 
 * Answer Explanation:
 * 
 * Creating a Kafka consumer involves several important steps:
 * 
 * First, we configure the consumer properties. The bootstrap servers property
 * tells the consumer where to find the Kafka cluster. We also need deserializers
 * for both key and value, which convert the byte arrays back into objects.
 * 
 * The group ID is crucial - it identifies which consumer group this consumer
 * belongs to. Consumers in the same group share the work of processing messages
 * from topics. If you have multiple consumers in the same group, Kafka will
 * distribute partitions among them for parallel processing.
 * 
 * The auto-offset-reset property determines what happens when a consumer starts
 * reading from a topic for the first time. Setting it to "earliest" means
 * start from the beginning of the topic, while "latest" means start from new
 * messages only.
 * 
 * After creating the KafkaConsumer instance, we subscribe to one or more topics.
 * The consumer will then receive messages from all partitions of those topics
 * that are assigned to this consumer based on the consumer group.
 * 
 * The main consumption loop uses poll() to fetch messages. The poll() method
 * returns a batch of records and blocks for the specified duration if no
 * messages are available. We then iterate through the records and process each
 * message.
 * 
 * It's important to commit offsets after processing messages. This tells Kafka
 * that we've successfully processed these messages, so if the consumer crashes,
 * it can resume from where it left off. In this example, we're using manual
 * commit, but you can also use automatic commit.
 */
public class BasicKafkaConsumer {
    
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "basic-topic";
    private static final String GROUP_ID = "basic-consumer-group";
    
    public static void main(String[] args) {
        // Interview Point: Step 1 - Configure Consumer Properties
        Properties properties = new Properties();
        
        // Bootstrap servers - where to find Kafka cluster
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        
        // Group ID - identifies the consumer group
        // Consumers in the same group share partition processing
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        
        // Key deserializer - converts bytes back to String
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName());
        
        // Value deserializer - converts bytes back to String
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName());
        
        // Auto offset reset - what to do when no offset is stored
        // "earliest" = start from beginning, "latest" = start from new messages only
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        // Disable auto-commit - we'll commit manually after processing
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        
        // Interview Point: Step 2 - Create KafkaConsumer Instance
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        
        try {
            // Interview Point: Step 3 - Subscribe to Topics
            // Consumer will receive messages from all partitions assigned to this group
            consumer.subscribe(Collections.singletonList(TOPIC_NAME));
            
            System.out.println("Consumer subscribed to topic: " + TOPIC_NAME);
            System.out.println("Waiting for messages...\n");
            
            // Interview Point: Step 4 - Poll for Messages
            // This is the main consumption loop
            while (true) {
                // poll() returns a batch of records
                // Duration.ofSeconds(1) means wait up to 1 second if no messages available
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                
                // Interview Point: Process each record
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Received message:");
                    System.out.println("  Topic: " + record.topic());
                    System.out.println("  Partition: " + record.partition());
                    System.out.println("  Offset: " + record.offset());
                    System.out.println("  Key: " + record.key());
                    System.out.println("  Value: " + record.value());
                    System.out.println("  Timestamp: " + record.timestamp());
                    System.out.println("---");
                }
                
                // Interview Point: Step 5 - Commit Offsets
                // This tells Kafka we've successfully processed these messages
                // If consumer crashes, it can resume from the last committed offset
                consumer.commitSync();
            }
            
        } catch (Exception e) {
            System.err.println("Error consuming messages: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Interview Point: Step 6 - Close Consumer
            // Always close to release resources and commit final offsets
            consumer.close();
            System.out.println("Consumer closed");
        }
    }
}

