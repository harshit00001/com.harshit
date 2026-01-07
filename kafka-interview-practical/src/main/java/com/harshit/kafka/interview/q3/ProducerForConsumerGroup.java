package com.harshit.kafka.interview.q3;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * Producer to send messages for Consumer Group Example
 * 
 * This producer sends messages with keys to demonstrate partitioning.
 * Messages with the same key go to the same partition.
 */
public class ProducerForConsumerGroup {
    
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "orders-topic";
    
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class.getName());
        
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        
        try {
            System.out.println("Sending messages to topic: " + TOPIC_NAME);
            System.out.println("Messages with same key will go to same partition\n");
            
            // Send messages with different keys to demonstrate partitioning
            String[] orderTypes = {"electronics", "clothing", "books", "electronics", "clothing", "books"};
            
            for (int i = 1; i <= 20; i++) {
                String key = orderTypes[i % orderTypes.length];
                String value = "Order #" + i + " - Type: " + key;
                
                ProducerRecord<String, String> record = new ProducerRecord<>(
                    TOPIC_NAME, key, value
                );
                
                producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        System.out.println("Sent: " + value);
                        System.out.println("  → Partition: " + metadata.partition() + 
                                         ", Offset: " + metadata.offset());
                    } else {
                        System.err.println("Error: " + exception.getMessage());
                    }
                });
                
                Thread.sleep(300);
            }
            
            producer.flush();
            System.out.println("\nAll messages sent!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}

