package com.harshit.mongodb.basic;

import com.harshit.mongodb.config.DatabaseConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Example 1: MongoDB Connection
 * 
 * SIMPLE EXPLANATION:
 * This example shows how to connect to MongoDB database.
 * Think of it like connecting to WiFi - you need the right address and credentials.
 * 
 * TECHNICAL EXPLANATION:
 * We use MongoClient to establish a connection to MongoDB server.
 * The connection string (URI) specifies where MongoDB is running.
 * We use try-with-resources for proper resource management.
 * 
 * INTERVIEW POINT:
 * - Always close connections after use to prevent memory leaks
 * - Use connection pooling for production applications
 * - Handle connection errors gracefully
 */
public class Example1Connection {
    
    private static final Logger logger = LoggerFactory.getLogger(Example1Connection.class);
    
    public static void main(String[] args) {
        MongoClient client = null;
        
        try {
            logger.info("🔄 Attempting to connect to MongoDB...\n");
            
            // Connect to database
            MongoDatabase db = DatabaseConfig.connectToDatabase();
            client = DatabaseConfig.getClient();
            
            // Get database name
            String dbName = db.getName();
            logger.info("📊 Connected to database: {}\n", dbName);
            
            // List all collections in the database
            logger.info("📁 Collections in database:");
            java.util.List<String> collectionNames = db.listCollectionNames().into(new java.util.ArrayList<>());
            if (collectionNames.isEmpty()) {
                logger.info("   (No collections yet - this is normal for a new database)\n");
            } else {
                collectionNames.forEach(collectionName -> {
                    logger.info("   - {}", collectionName);
                });
                logger.info("");
            }
            
            // Test: Create a test collection and insert a document
            logger.info("🧪 Testing connection with a sample operation...");
            var testCollection = db.getCollection("connection_test");
            
            // Insert a test document
            Document testDoc = new Document()
                    .append("message", "Connection successful!")
                    .append("timestamp", new Date())
                    .append("test", true);
            
            testCollection.insertOne(testDoc);
            ObjectId insertedId = testDoc.getObjectId("_id");
            logger.info("✅ Test document inserted with ID: {}\n", insertedId);
            
            // Find the document we just inserted
            Document foundDoc = testCollection.find(new Document("_id", insertedId)).first();
            if (foundDoc != null) {
                logger.info("📄 Found document:");
                logger.info("{}", foundDoc.toJson());
                logger.info("");
            }
            
            // Note: Keeping test document in database for viewing in MongoDB Compass
            // Uncomment below to clean up test document
            // testCollection.deleteOne(new Document("_id", insertedId));
            // logger.info("🧹 Test document cleaned up\n");
            
            logger.info("✅ Connection test completed successfully!");
            logger.info("💡 Tip: Check MongoDB Compass to see the test document in 'connection_test' collection");
            
        } catch (Exception error) {
            logger.error("❌ Error: {}", error.getMessage(), error);
            logger.info("\n💡 Troubleshooting tips:");
            logger.info("   1. Make sure MongoDB is running");
            logger.info("   2. Check connection string in config/DatabaseConfig.java");
            logger.info("   3. For local MongoDB: mongod should be running");
            logger.info("   4. For Atlas: Check your connection string and IP whitelist");
        } finally {
            // Always close the connection
            if (client != null) {
                DatabaseConfig.closeConnection();
            }
            // Give MongoDB driver threads more time to fully terminate
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Force JVM exit to prevent thread cleanup warnings with Maven exec plugin
            System.exit(0);
        }
    }
}

