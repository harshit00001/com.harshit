package com.harshit.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * KAFKA SPRING BOOT INTERVIEW APPLICATION
 * 
 * This is the main Spring Boot application class that serves as the entry point
 * for our Kafka interview preparation project. When you run this application,
 * it will start a Spring Boot server that demonstrates various Kafka concepts
 * from basic to advanced level.
 * 
 * The @SpringBootApplication annotation is a convenience annotation that combines
 * three important annotations:
 * 1. @Configuration - Marks this class as a configuration class
 * 2. @EnableAutoConfiguration - Enables Spring Boot's auto-configuration
 * 3. @ComponentScan - Scans for components in the current package and sub-packages
 * 
 * This application demonstrates:
 * - Basic Kafka producer and consumer setup
 * - Spring Kafka integration using @KafkaListener
 * - Advanced concepts like error handling, partitioning, and consumer groups
 * - Real-world use cases and best practices
 */
@SpringBootApplication
public class KafkaInterviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaInterviewApplication.class, args);
        System.out.println("\n=========================================");
        System.out.println("Kafka Interview Application Started!");
        System.out.println("Server running on: http://localhost:8080");
        System.out.println("Make sure Kafka is running on localhost:9092");
        System.out.println("=========================================\n");
    }
}

