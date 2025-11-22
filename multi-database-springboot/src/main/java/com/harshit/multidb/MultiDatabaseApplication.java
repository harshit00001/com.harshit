package com.harshit.multidb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Multi-Database Spring Boot Application
 * 
 * This application demonstrates how to connect to multiple databases
 * in a single Spring Boot application.
 * 
 * Features:
 * - Primary Database (MySQL): User management
 * - Secondary Database (PostgreSQL): Order management
 * - Separate Entity Managers and Transaction Managers
 * - Independent database operations
 */
@SpringBootApplication
public class MultiDatabaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiDatabaseApplication.class, args);
        System.out.println("\n=========================================");
        System.out.println("Multi-Database Application Started!");
        System.out.println("=========================================");
        System.out.println("Primary DB (MySQL): User Management");
        System.out.println("Secondary DB (PostgreSQL): Order Management");
        System.out.println("\nAPI Endpoints:");
        System.out.println("POST /api/users - Create user (Primary DB)");
        System.out.println("GET /api/users - Get all users (Primary DB)");
        System.out.println("POST /api/orders - Create order (Secondary DB)");
        System.out.println("GET /api/orders - Get all orders (Secondary DB)");
        System.out.println("POST /api/transactions - Create user and order (Both DBs)");
        System.out.println("=========================================\n");
    }
}

