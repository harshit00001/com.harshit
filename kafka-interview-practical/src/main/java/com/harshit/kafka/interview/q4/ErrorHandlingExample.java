package com.harshit.kafka.interview.q4;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * INTERVIEW QUESTION 4: How do you handle errors in Kafka Consumer?
 * 
 * ANSWER:
 * Error handling in Kafka Consumer involves:
 * 1. Handling deserialization errors
 * 2. Handling processing errors (retry, dead letter queue)
 * 3. Handling commit errors
 * 4. Using try-catch blocks appropriately
 * 5. Implementing retry logic
 * 6. Using dead letter topics for failed messages
 * 
 * KEY STRATEGIES:
 * - Deserialization errors: Use ErrorHandlingDeserializer or catch exceptions
 * - Processing errors: Don't commit offset, retry or send to DLQ
 * - Commit errors: Retry commit or log for manual intervention
 * - Poison messages: Send to dead letter topic to avoid blocking
 * 
 * STEPS TO REPRODUCE:
 * 1. Create topics:
 *    bin/kafka-topics.sh --create --topic events-topic --bootstrap-server localhost:9092 --partitions 1
 *    bin/kafka-topics.sh --create --topic events-dlq --bootstrap-server localhost:9092 --partitions 1
 * 2. Run ErrorHandlingProducer to send messages (some will fail)
 * 3. Run this consumer to see error handling in action
 */
public class ErrorHandlingExample {
    
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "events-topic";
    private static final String DLQ_TOPIC = "events-dlq";
    private static final String GROUP_ID = "error-handling-group";
    private static final int MAX_RETRIES = 3;
    
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        
        // Create producer for DLQ (in real app, inject this)
        Properties producerProps = new Properties();
        producerProps.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringSerializer");
        
        org.apache.kafka.clients.producer.KafkaProducer<String, String> dlqProducer = 
            new org.apache.kafka.clients.producer.KafkaProducer<>(producerProps);
        
        try {
            consumer.subscribe(Collections.singletonList(TOPIC_NAME));
            System.out.println("Error Handling Consumer started");
            System.out.println("Topic: " + TOPIC_NAME);
            System.out.println("DLQ Topic: " + DLQ_TOPIC);
            System.out.println("Max Retries: " + MAX_RETRIES + "\n");
            
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("=========================================");
                    System.out.println("Processing message:");
                    System.out.println("  Key: " + record.key());
                    System.out.println("  Value: " + record.value());
                    System.out.println("  Partition: " + record.partition());
                    System.out.println("  Offset: " + record.offset());
                    
                    try {
                        // Strategy 1: Process with retry logic
                        boolean success = processWithRetry(record, MAX_RETRIES);
                        
                        if (success) {
                            System.out.println("  ✓ Message processed successfully");
                            // Commit offset only after successful processing
                            consumer.commitSync();
                        } else {
                            System.out.println("  ✗ Message failed after " + MAX_RETRIES + " retries");
                            // Strategy 2: Send to Dead Letter Queue
                            sendToDLQ(dlqProducer, record);
                            // Still commit to avoid reprocessing
                            consumer.commitSync();
                        }
                        
                    } catch (DeserializationException e) {
                        // Strategy 3: Handle deserialization errors
                        System.err.println("  ✗ Deserialization error: " + e.getMessage());
                        sendToDLQ(dlqProducer, record);
                        consumer.commitSync();
                        
                    } catch (Exception e) {
                        // Strategy 4: Handle unexpected errors
                        System.err.println("  ✗ Unexpected error: " + e.getMessage());
                        e.printStackTrace();
                        // Don't commit - will retry on next poll
                    }
                    
                    System.out.println("=========================================\n");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            consumer.close();
            dlqProducer.close();
        }
    }
    
    /**
     * Process message with retry logic
     */
    private static boolean processWithRetry(ConsumerRecord<String, String> record, int maxRetries) {
        int attempt = 0;
        
        while (attempt < maxRetries) {
            try {
                attempt++;
                System.out.println("  Attempt " + attempt + "/" + maxRetries);
                
                // Simulate processing that might fail
                processMessage(record);
                
                return true; // Success
                
            } catch (ProcessingException e) {
                System.err.println("  ✗ Processing failed: " + e.getMessage());
                
                if (attempt < maxRetries) {
                    try {
                        // Exponential backoff
                        long delay = (long) Math.pow(2, attempt) * 1000;
                        System.out.println("  Waiting " + delay + "ms before retry...");
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }
        
        return false; // Failed after all retries
    }
    
    /**
     * Simulate message processing (may throw exceptions)
     */
    private static void processMessage(ConsumerRecord<String, String> record) throws ProcessingException {
        String value = record.value();
        
        // Simulate deserialization error
        if (value.contains("INVALID_FORMAT")) {
            throw new DeserializationException("Invalid message format");
        }
        
        // Simulate processing error (30% failure rate)
        if (value.contains("FAIL") || Math.random() < 0.3) {
            throw new ProcessingException("Processing failed for: " + value);
        }
        
        // Simulate processing time
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Send failed message to Dead Letter Queue
     */
    private static void sendToDLQ(org.apache.kafka.clients.producer.KafkaProducer<String, String> producer,
                                  ConsumerRecord<String, String> record) {
        try {
            org.apache.kafka.clients.producer.ProducerRecord<String, String> dlqRecord = 
                new org.apache.kafka.clients.producer.ProducerRecord<>(
                    DLQ_TOPIC, 
                    record.key(), 
                    record.value()
                );
            
            producer.send(dlqRecord, (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("  → Sent to DLQ: " + DLQ_TOPIC);
                } else {
                    System.err.println("  ✗ Failed to send to DLQ: " + exception.getMessage());
                }
            });
            
            producer.flush();
        } catch (Exception e) {
            System.err.println("  ✗ Error sending to DLQ: " + e.getMessage());
        }
    }
    
    // Custom Exceptions
    static class ProcessingException extends Exception {
        ProcessingException(String message) {
            super(message);
        }
    }
    
    static class DeserializationException extends Exception {
        DeserializationException(String message) {
            super(message);
        }
    }
}

