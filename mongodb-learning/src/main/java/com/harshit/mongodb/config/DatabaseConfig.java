package com.harshit.mongodb.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MongoDB Database Connection Configuration
 * 
 * This class handles the connection to MongoDB database.
 * You can use either:
 * 1. Local MongoDB: mongodb://localhost:27017/mongodb_learning
 * 2. MongoDB Atlas: mongodb+srv://username:password@cluster.mongodb.net/mongodb_learning
 */
public class DatabaseConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    
    // Connection URI
    // For local MongoDB (default)
    private static final String LOCAL_URI = "mongodb://localhost:27017/mongodb_learning";
    
    // For MongoDB Atlas (uncomment and update with your credentials)
    // private static final String ATLAS_URI = "mongodb+srv://username:password@cluster.mongodb.net/mongodb_learning";
    
    // Use local by default, change to ATLAS_URI if using Atlas
    private static final String URI = LOCAL_URI;
    
    // Database name
    private static final String DB_NAME = "mongodb_learning";
    
    // MongoClient instance
    private static MongoClient client;
    
    /**
     * Connect to MongoDB
     * @return Database object
     */
    public static MongoDatabase connectToDatabase() {
        try {
            // Create MongoClient with connection string
            client = MongoClients.create(URI);
            
            logger.info("✅ Connected to MongoDB successfully!");
            
            // Get database
            MongoDatabase db = client.getDatabase(DB_NAME);
            
            return db;
        } catch (Exception error) {
            logger.error("❌ Error connecting to MongoDB: {}", error.getMessage(), error);
            throw new RuntimeException("Failed to connect to MongoDB", error);
        }
    }
    
    /**
     * Get the MongoClient instance
     * @return MongoClient instance
     */
    public static MongoClient getClient() {
        if (client == null) {
            connectToDatabase();
        }
        return client;
    }
    
    /**
     * Close MongoDB connection
     */
    public static void closeConnection() {
        try {
            if (client != null) {
                client.close();
                logger.info("✅ MongoDB connection closed.");
                // Give background threads more time to clean up
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception error) {
            logger.error("❌ Error closing MongoDB connection: {}", error.getMessage(), error);
            throw new RuntimeException("Failed to close MongoDB connection", error);
        }
    }
}

