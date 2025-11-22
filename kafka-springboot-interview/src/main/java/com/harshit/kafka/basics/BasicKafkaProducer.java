package com.harshit.kafka.basics;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * BASIC KAFKA PRODUCER - Interview Explanation
 * 
 * This class demonstrates how to create a basic Kafka producer using the
 * standard Java Kafka client library. This is the foundation for understanding
 * how producers work in Kafka.
 * 
 * Interview Question: "How do you create a Kafka producer in Java?"
 * 
 * Answer Explanation:
 * 
 * To create a Kafka producer, we need to follow these steps:
 * 
 * First, we configure the producer properties. The most important property is
 * the bootstrap servers, which tells the producer where to find the Kafka cluster.
 * We also need to specify serializers for both the key and value, because Kafka
 * stores everything as byte arrays. In this example, we're using StringSerializer
 * for both, which means we're sending string messages.
 * 
 * Next, we create a KafkaProducer instance with these properties. The producer
 * is thread-safe, so you can use the same instance across multiple threads.
 * 
 * To send a message, we create a ProducerRecord. This record contains the topic
 * name, an optional key, and the message value. The key is useful for partitioning
 * - messages with the same key will go to the same partition, ensuring ordering
 * for that key.
 * 
 * Finally, we use the send() method to publish the message. The send() method
 * is asynchronous by default, which means it returns immediately and doesn't wait
 * for the message to be acknowledged. If you need to ensure the message was sent
 * successfully, you can use the returned Future object or provide a callback.
 * 
 * It's important to close the producer when you're done, as it ensures all
 * pending messages are sent and resources are properly released.
 */
public class BasicKafkaProducer {
    
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "basic-topic";
    
    public static void main(String[] args) {
        // Interview Point: Step 1 - Configure Producer Properties
        // These properties tell the producer how to connect to Kafka and how to serialize data
        Properties properties = new Properties();
        
        // Bootstrap servers - the initial list of Kafka brokers to connect to
        // The producer will discover all brokers in the cluster from these initial contacts
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        
        // Key serializer - converts the key object to bytes
        // StringSerializer is used when keys are strings
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
            StringSerializer.class.getName());
        
        // Value serializer - converts the message value to bytes
        // StringSerializer is used when message values are strings
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
            StringSerializer.class.getName());
        
        // Interview Point: Step 2 - Create KafkaProducer Instance
        // The producer is thread-safe and can be shared across multiple threads
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        
        try {
            // Interview Point: Step 3 - Create ProducerRecord
            // ProducerRecord contains: topic name, optional key, and message value
            // The key helps with partitioning - messages with same key go to same partition
            ProducerRecord<String, String> record = new ProducerRecord<>(
                TOPIC_NAME,           // Topic name
                "message-key-1",      // Key (optional, can be null)
                "Hello Kafka!"        // Message value
            );
            
            // Interview Point: Step 4 - Send Message
            // send() is asynchronous - it returns immediately
            // The message is sent in the background
            producer.send(record);
            
            System.out.println("Message sent successfully!");
            
            // Interview Point: Sending multiple messages
            for (int i = 1; i <= 5; i++) {
                ProducerRecord<String, String> record2 = new ProducerRecord<>(
                    TOPIC_NAME,
                    "key-" + i,
                    "Message number " + i
                );
                producer.send(record2);
                System.out.println("Sent message: " + i);
            }
            
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Interview Point: Step 5 - Close Producer
            // Always close the producer to ensure all messages are sent and resources are released
            producer.close();
            System.out.println("Producer closed");
        }
    }
}

