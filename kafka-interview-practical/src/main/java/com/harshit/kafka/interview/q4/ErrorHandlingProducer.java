package com.harshit.kafka.interview.q4;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * Producer for Error Handling Example
 * Sends mix of valid and invalid messages
 */
public class ErrorHandlingProducer {
    
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "events-topic";
    
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class.getName());
        
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        
        try {
            System.out.println("Sending test messages (some will fail processing)...\n");
            
            String[] messages = {
                "Valid message 1",
                "FAIL - This will fail processing",
                "Valid message 2",
                "INVALID_FORMAT - Deserialization error",
                "Valid message 3",
                "FAIL - Another failure",
                "Valid message 4",
                "Valid message 5"
            };
            
            for (int i = 0; i < messages.length; i++) {
                ProducerRecord<String, String> record = new ProducerRecord<>(
                    TOPIC_NAME, 
                    "event-" + (i + 1), 
                    messages[i]
                );
                
                producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        System.out.println("Sent: " + messages[Integer.parseInt(metadata.key().split("-")[1]) - 1]);
                    } else {
                        System.err.println("Error: " + exception.getMessage());
                    }
                });
                
                Thread.sleep(500);
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

