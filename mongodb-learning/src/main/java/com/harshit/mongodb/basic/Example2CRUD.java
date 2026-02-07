package com.harshit.mongodb.basic;

import com.harshit.mongodb.config.DatabaseConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Example 2: CRUD Operations (Create, Read, Update, Delete)
 * 
 * SIMPLE EXPLANATION:
 * CRUD stands for the four basic operations you can do with data:
 * - CREATE: Add new data
 * - READ: Get/view data
 * - UPDATE: Change existing data
 * - DELETE: Remove data
 * 
 * TECHNICAL EXPLANATION:
 * MongoDB provides methods for each CRUD operation:
 * - Create: insertOne(), insertMany()
 * - Read: findOne(), find()
 * - Update: updateOne(), updateMany(), replaceOne()
 * - Delete: deleteOne(), deleteMany()
 * 
 * INTERVIEW POINT:
 * - insertOne returns InsertOneResult with insertedId
 * - find() returns a FindIterable, use into() to convert to list
 * - updateOne updates first match, updateMany updates all matches
 * - Always use $set operator in updates to avoid replacing entire document
 */
public class Example2CRUD {
    
    private static final Logger logger = LoggerFactory.getLogger(Example2CRUD.class);
    
    public static void main(String[] args) {
        MongoClient client = null;
        
        try {
            logger.info("🔄 Connecting to MongoDB...\n");
            MongoDatabase db = DatabaseConfig.connectToDatabase();
            client = DatabaseConfig.getClient();
            
            MongoCollection<Document> usersCollection = db.getCollection("users");
            
            // ============================================
            // CREATE OPERATIONS
            // ============================================
            logger.info("📝 === CREATE OPERATIONS ===\n");
            
            // Insert a single document
            logger.info("1. Inserting a single user...");
            Document newUser = new Document()
                    .append("name", "John Doe")
                    .append("email", "john.doe@example.com")
                    .append("age", 30)
                    .append("city", "New York")
                    .append("interests", Arrays.asList("coding", "reading", "traveling"))
                    .append("createdAt", new Date());
            
            usersCollection.insertOne(newUser);
            ObjectId johnId = newUser.getObjectId("_id");
            logger.info("   ✅ User inserted with ID: {}\n", johnId);
            
            // Insert multiple documents
            logger.info("2. Inserting multiple users...");
            List<Document> multipleUsers = Arrays.asList(
                    new Document()
                            .append("name", "Jane Smith")
                            .append("email", "jane.smith@example.com")
                            .append("age", 25)
                            .append("city", "Los Angeles")
                            .append("interests", Arrays.asList("music", "dancing"))
                            .append("createdAt", new Date()),
                    new Document()
                            .append("name", "Bob Johnson")
                            .append("email", "bob.johnson@example.com")
                            .append("age", 35)
                            .append("city", "Chicago")
                            .append("interests", Arrays.asList("sports", "cooking"))
                            .append("createdAt", new Date()),
                    new Document()
                            .append("name", "Alice Williams")
                            .append("email", "alice.williams@example.com")
                            .append("age", 28)
                            .append("city", "New York")
                            .append("interests", Arrays.asList("art", "photography", "traveling"))
                            .append("createdAt", new Date())
            );
            
            usersCollection.insertMany(multipleUsers);
            logger.info("   ✅ {} users inserted\n", multipleUsers.size());
            
            // ============================================
            // READ OPERATIONS
            // ============================================
            logger.info("📖 === READ OPERATIONS ===\n");
            
            // Find one document
            logger.info("1. Finding one user by email...");
            Document foundUser = usersCollection.find(Filters.eq("email", "john.doe@example.com")).first();
            if (foundUser != null) {
                logger.info("   Found user: {}", foundUser.toJson());
            }
            logger.info("");
            
            // Find all documents
            logger.info("2. Finding all users...");
            List<Document> allUsers = usersCollection.find().into(new ArrayList<>());
            logger.info("   Total users: {}", allUsers.size());
            for (int i = 0; i < allUsers.size(); i++) {
                Document user = allUsers.get(i);
                logger.info("   {}. {} ({})", i + 1, user.getString("name"), user.getString("email"));
            }
            logger.info("");
            
            // Find with filter
            logger.info("3. Finding users from New York...");
            List<Document> nyUsers = usersCollection.find(Filters.eq("city", "New York")).into(new ArrayList<>());
            logger.info("   Found {} users from New York:", nyUsers.size());
            nyUsers.forEach(user -> {
                logger.info("   - {}", user.getString("name"));
            });
            logger.info("");
            
            // Find with condition (age > 30)
            logger.info("4. Finding users older than 30...");
            List<Document> olderUsers = usersCollection.find(Filters.gt("age", 30)).into(new ArrayList<>());
            logger.info("   Found {} users older than 30:", olderUsers.size());
            olderUsers.forEach(user -> {
                logger.info("   - {} (age: {})", user.getString("name"), user.getInteger("age"));
            });
            logger.info("");
            
            // ============================================
            // UPDATE OPERATIONS
            // ============================================
            logger.info("✏️  === UPDATE OPERATIONS ===\n");
            
            // Update one document
            logger.info("1. Updating John's age to 31...");
            usersCollection.updateOne(
                    Filters.eq("email", "john.doe@example.com"),
                    Updates.combine(
                            Updates.set("age", 31),
                            Updates.set("updatedAt", new Date())
                    )
            );
            logger.info("   ✅ Document updated\n");
            
            // Verify update
            Document updatedUser = usersCollection.find(Filters.eq("email", "john.doe@example.com")).first();
            if (updatedUser != null) {
                logger.info("   Updated age: {}\n", updatedUser.getInteger("age"));
            }
            
            // Update many documents (add field to all users)
            logger.info("2. Adding \"status\" field to all users...");
            long modifiedCount = usersCollection.updateMany(
                    new Document(),
                    Updates.set("status", "active")
            ).getModifiedCount();
            logger.info("   ✅ {} documents updated\n", modifiedCount);
            
            // Increment a field
            logger.info("3. Incrementing Bob's age by 1...");
            usersCollection.updateOne(
                    Filters.eq("email", "bob.johnson@example.com"),
                    Updates.inc("age", 1)
            );
            Document bob = usersCollection.find(Filters.eq("email", "bob.johnson@example.com")).first();
            if (bob != null) {
                logger.info("   Bob's new age: {}\n", bob.getInteger("age"));
            }
            
            // ============================================
            // DELETE OPERATIONS
            // ============================================
//            logger.info("🗑️  === DELETE OPERATIONS ===\n");
//
//            // Delete one document
//            logger.info("1. Deleting Alice...");
//            long deletedCount = usersCollection.deleteOne(Filters.eq("email", "alice.williams@example.com")).getDeletedCount();
//            logger.info("   ✅ {} document deleted\n", deletedCount);
//
//            // Verify deletion
//            List<Document> remainingUsers = usersCollection.find().into(new ArrayList<>());
//            logger.info("   Remaining users: {}\n", remainingUsers.size());
//
//            // Delete many documents (cleanup - optional)
//            logger.info("2. Cleaning up test data...");
//            long deletedManyCount = usersCollection.deleteMany(new Document()).getDeletedCount();
//            logger.info("   ✅ {} documents deleted\n", deletedManyCount);
            
            logger.info("✅ CRUD operations completed successfully!");
            
        } catch (Exception error) {
            logger.error("❌ Error: {}", error.getMessage(), error);
        } finally {
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

