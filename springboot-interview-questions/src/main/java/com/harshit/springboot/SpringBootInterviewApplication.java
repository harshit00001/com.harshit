package com.harshit.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Spring Boot Interview Questions Application
 * 
 * This application demonstrates various Spring Boot concepts from basic to advanced,
 * with comprehensive code examples and detailed explanations suitable for interviews.
 */
@SpringBootApplication
@EnableAsync  // Enables @Async annotation support
public class SpringBootInterviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootInterviewApplication.class, args);
        System.out.println("\n=========================================");
        System.out.println("Spring Boot Interview Questions App Started!");
        System.out.println("=========================================");
        System.out.println("This application demonstrates:");
        System.out.println("- Dependency Injection (4 types)");
        System.out.println("- @Async annotation");
        System.out.println("- WebClient vs RestTemplate");
        System.out.println("- Spring Data JPA");
        System.out.println("- Multiple database configuration");
        System.out.println("- AOP (Aspect-Oriented Programming)");
        System.out.println("- Batch processing");
        System.out.println("- Exception handling and resilience");
        System.out.println("=========================================\n");
    }
}

