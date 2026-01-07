package com.harshit.kafka.interview.q1;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * INTERVIEW QUESTION 1: How do you create a basic Kafka Producer in Java?
 * 
 * ANSWER:
 * To create a Kafka Producer, you need to:
 * 1. Configure producer properties (bootstrap servers, serializers)
 * 2. Create a KafkaProducer instance
 * 3. Create ProducerRecord with topic, key (optional), and value
 * 4. Send the record using send() method
 * 5. Handle the response (synchronous or asynchronous)
 * 6. Close the producer when done
 * 
 * KEY CONCEPTS:
 * - Bootstrap servers: Initial Kafka broker addresses
 * - Serializers: Convert objects to bytes (StringSerializer for strings)
 * - ProducerRecord: The message to send
 * - send() method: Asynchronous by default, returns Future
 * - flush() and close(): Ensure all messages are sent
 * 
 * STEPS TO REPRODUCE:
 * 1. Start Zookeeper: bin/zookeeper-server-start.sh config/zookeeper.properties
 * 2. Start Kafka: bin/kafka-server-start.sh config/server.properties
 * 3. Create topic: bin/kafka-topics.sh --create --topic basic-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
 * 4. Run this class: java BasicProducerExample
 * 5. Verify messages: bin/kafka-console-consumer.sh --topic basic-topic --from-beginning --bootstrap-server localhost:9092
 */
public class BasicProducerExample {
    
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "basic-topic";
    
    public static void main(String[] args) {
        // Step 1: Configure Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
            StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
            StringSerializer.class.getName());
        
        // Optional: Configure reliability
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // Wait for all replicas
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, "3"); // Retry failed sends
        
        // Step 2: Create KafkaProducer Instance
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        
        try {
            // Step 3: Send Messages
            for (int i = 1; i <= 10; i++) {
                // Create ProducerRecord
                ProducerRecord<String, String> record = new ProducerRecord<>(
                    TOPIC_NAME, 
                    "key-" + i, 
                    "Message " + i + " from Kafka Producer"
                );
                
                // Step 4: Send Message (Asynchronous)
                Future<RecordMetadata> future = producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        System.out.println("Message sent successfully!");
                        System.out.println("Topic: " + metadata.topic());
                        System.out.println("Partition: " + metadata.partition());
                        System.out.println("Offset: " + metadata.offset());
                        System.out.println("Timestamp: " + metadata.timestamp());
                    } else {
                        System.err.println("Error sending message: " + exception.getMessage());
                    }
                });
                
                // Optional: Wait for completion (synchronous)
                // RecordMetadata metadata = future.get();
                
                Thread.sleep(500); // Small delay between messages
            }
            
            // Step 5: Flush and Close
            producer.flush(); // Ensure all messages are sent
            System.out.println("\nAll messages sent successfully!");
            
        } catch (Exception e) {
            System.err.println("Error in producer: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Step 6: Close Producer
            producer.close();
        }
    }
}

