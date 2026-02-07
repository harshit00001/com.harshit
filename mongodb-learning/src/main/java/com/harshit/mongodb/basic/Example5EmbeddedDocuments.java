package com.harshit.mongodb.basic;

import com.harshit.mongodb.config.DatabaseConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
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
 * Example 5: Embedded Documents and Arrays
 * 
 * SIMPLE EXPLANATION:
 * Embedded documents are like putting a box inside another box.
 * Instead of storing related data in separate tables, you can store
 * it nested inside a single document.
 * 
 * TECHNICAL EXPLANATION:
 * MongoDB allows documents to contain other documents (nested objects)
 * and arrays. This is called embedding and is one of MongoDB's key features.
 * Use dot notation to query nested fields: "address.city"
 * 
 * INTERVIEW POINT:
 * - Embedded documents are stored in the same document (denormalization)
 * - Good for one-to-one or one-to-few relationships
 * - Use dot notation to query nested fields
 * - Use $elemMatch for complex array queries
 * - Consider embedding vs referencing based on access patterns
 */
public class Example5EmbeddedDocuments {
    
    private static final Logger logger = LoggerFactory.getLogger(Example5EmbeddedDocuments.class);
    
    public static void main(String[] args) {
        MongoClient client = null;
        
        try {
            logger.info("🔄 Connecting to MongoDB...\n");
            MongoDatabase db = DatabaseConfig.connectToDatabase();
            client = DatabaseConfig.getClient();
            
            MongoCollection<Document> usersCollection = db.getCollection("users");
            
            // ============================================
            // CREATING DOCUMENTS WITH EMBEDDED DATA
            // ============================================
            logger.info("📝 === CREATING EMBEDDED DOCUMENTS ===\n");
            
            // User with embedded address
            Document user1 = new Document("name", "John Doe")
                    .append("email", "john@example.com")
                    .append("age", 30)
                    .append("address", new Document()
                            .append("street", "123 Main St")
                            .append("city", "New York")
                            .append("state", "NY")
                            .append("zipCode", "10001")
                            .append("country", "USA"))
                    .append("orders", Arrays.asList(
                            new Document("orderId", "ORD001")
                                    .append("date", new Date(1705276800000L)) // 2024-01-15
                                    .append("total", 299.99)
                                    .append("items", Arrays.asList("Laptop", "Mouse")),
                            new Document("orderId", "ORD002")
                                    .append("date", new Date(1708473600000L)) // 2024-02-20
                                    .append("total", 149.99)
                                    .append("items", Arrays.asList("Keyboard", "Headphones"))
                    ))
                    .append("tags", Arrays.asList("premium", "verified", "active"))
                    .append("createdAt", new Date());
            
            // User with nested embedded documents
            Document user2 = new Document("name", "Jane Smith")
                    .append("email", "jane@example.com")
                    .append("age", 28)
                    .append("address", new Document()
                            .append("street", "456 Oak Ave")
                            .append("city", "Los Angeles")
                            .append("state", "CA")
                            .append("zipCode", "90001")
                            .append("country", "USA")
                            .append("coordinates", new Document()
                                    .append("latitude", 34.0522)
                                    .append("longitude", -118.2437)))
                    .append("orders", Arrays.asList(
                            new Document("orderId", "ORD003")
                                    .append("date", new Date(1710028800000L)) // 2024-03-10
                                    .append("total", 599.99)
                                    .append("items", Arrays.asList("Smartphone"))
                                    .append("shipping", new Document()
                                            .append("method", "Express")
                                            .append("carrier", "FedEx")
                                            .append("trackingNumber", "FX123456"))
                    ))
                    .append("tags", Arrays.asList("new", "active"))
                    .append("createdAt", new Date());
            
            Document user3 = new Document("name", "Bob Johnson")
                    .append("email", "bob@example.com")
                    .append("age", 35)
                    .append("address", new Document()
                            .append("street", "789 Pine Rd")
                            .append("city", "Chicago")
                            .append("state", "IL")
                            .append("zipCode", "60601")
                            .append("country", "USA"))
                    .append("orders", new ArrayList<>())
                    .append("tags", Arrays.asList("premium"))
                    .append("createdAt", new Date());
            
            usersCollection.insertMany(Arrays.asList(user1, user2, user3));
            logger.info("✅ Inserted 3 users with embedded documents\n");
            
            // ============================================
            // QUERYING EMBEDDED DOCUMENTS
            // ============================================
            logger.info("🔍 === QUERYING EMBEDDED DOCUMENTS ===\n");
            
            // Query by nested field using dot notation
            logger.info("1. Users from New York:");
            List<Document> nyUsers = usersCollection.find(Filters.eq("address.city", "New York"))
                    .into(new ArrayList<>());
            nyUsers.forEach(user -> {
                Document address = (Document) user.get("address");
                logger.info("   - {} ({}, {})", user.getString("name"), 
                        address.getString("city"), address.getString("state"));
            });
            logger.info("");
            
            // Query by nested field in nested document
            logger.info("2. Users in California:");
            List<Document> caUsers = usersCollection.find(Filters.eq("address.state", "CA"))
                    .into(new ArrayList<>());
            caUsers.forEach(user -> {
                Document address = (Document) user.get("address");
                logger.info("   - {} ({})", user.getString("name"), address.getString("city"));
            });
            logger.info("");
            
            // Query by deeply nested field
            logger.info("3. Users with coordinates (nested in address):");
            List<Document> withCoordinates = usersCollection.find(
                    Filters.exists("address.coordinates")
            ).into(new ArrayList<>());
            withCoordinates.forEach(user -> {
                Document address = (Document) user.get("address");
                Document coords = (Document) address.get("coordinates");
                logger.info("   - {} (lat: {}, lng: {})", user.getString("name"), 
                        coords.getDouble("latitude"), coords.getDouble("longitude"));
            });
            logger.info("");
            
            // ============================================
            // QUERYING ARRAYS
            // ============================================
            logger.info("📋 === QUERYING ARRAYS ===\n");
            
            // Find documents where array contains value
            logger.info("1. Users with \"premium\" tag:");
            List<Document> premiumUsers = usersCollection.find(Filters.eq("tags", "premium"))
                    .into(new ArrayList<>());
            premiumUsers.forEach(user -> {
                @SuppressWarnings("unchecked")
                List<String> tags = (List<String>) user.get("tags");
                logger.info("   - {} (tags: {})", user.getString("name"), String.join(", ", tags));
            });
            logger.info("");
            
            // Find documents with array containing any of specified values
            logger.info("2. Users with \"premium\" OR \"verified\" tag:");
            List<Document> premiumOrVerified = usersCollection.find(
                    Filters.in("tags", Arrays.asList("premium", "verified"))
            ).into(new ArrayList<>());
            premiumOrVerified.forEach(user -> {
                @SuppressWarnings("unchecked")
                List<String> tags = (List<String>) user.get("tags");
                logger.info("   - {} (tags: {})", user.getString("name"), String.join(", ", tags));
            });
            logger.info("");
            
            // Find documents with array containing all specified values
            logger.info("3. Users with BOTH \"premium\" AND \"active\" tags:");
            List<Document> premiumAndActive = usersCollection.find(
                    Filters.all("tags", Arrays.asList("premium", "active"))
            ).into(new ArrayList<>());
            if (premiumAndActive.size() > 0) {
                premiumAndActive.forEach(user -> {
                    @SuppressWarnings("unchecked")
                    List<String> tags = (List<String>) user.get("tags");
                    logger.info("   - {} (tags: {})", user.getString("name"), String.join(", ", tags));
                });
            } else {
                logger.info("   (No users found)");
            }
            logger.info("");
            
            // ============================================
            // QUERYING ARRAYS OF EMBEDDED DOCUMENTS
            // ============================================
            logger.info("📦 === QUERYING ARRAYS OF EMBEDDED DOCUMENTS ===\n");
            
            // Find users with orders
            logger.info("1. Users who have placed orders:");
            List<Document> usersWithOrders = usersCollection.find(
                    Filters.and(
                            Filters.exists("orders"),
                            Filters.ne("orders", Arrays.asList())
                    )
            ).into(new ArrayList<>());
            usersWithOrders.forEach(user -> {
                @SuppressWarnings("unchecked")
                List<Document> orders = (List<Document>) user.get("orders");
                logger.info("   - {} ({} orders)", user.getString("name"), orders.size());
            });
            logger.info("");
            
            // Query array of embedded documents
            logger.info("2. Users with order total > $200:");
            List<Document> highValueOrders = usersCollection.find(
                    Filters.gt("orders.total", 200.0)
            ).into(new ArrayList<>());
            highValueOrders.forEach(user -> {
                @SuppressWarnings("unchecked")
                List<Document> orders = (List<Document>) user.get("orders");
                orders.forEach(order -> {
                    if (order.getDouble("total") > 200) {
                        logger.info("   - {}: Order {} - ${}", user.getString("name"), 
                                order.getString("orderId"), order.getDouble("total"));
                    }
                });
            });
            logger.info("");
            
            // $elemMatch for complex array queries
            logger.info("3. Users with Express shipping (using $elemMatch):");
            List<Document> expressShipping = usersCollection.find(
                    Filters.elemMatch("orders", Filters.eq("shipping.method", "Express"))
            ).into(new ArrayList<>());
            expressShipping.forEach(user -> {
                logger.info("   - {}", user.getString("name"));
            });
            logger.info("");
            
            // ============================================
            // UPDATING EMBEDDED DOCUMENTS
            // ============================================
            logger.info("✏️  === UPDATING EMBEDDED DOCUMENTS ===\n");
            
            // Update nested field
            logger.info("1. Updating John's zip code:");
            usersCollection.updateOne(
                    Filters.eq("email", "john@example.com"),
                    Updates.set("address.zipCode", "10002")
            );
            Document updatedJohn = usersCollection.find(Filters.eq("email", "john@example.com")).first();
            if (updatedJohn != null) {
                Document address = (Document) updatedJohn.get("address");
                logger.info("   Updated zip code: {}\n", address.getString("zipCode"));
            }
            
            // Add new order to array
            logger.info("2. Adding new order to John's orders:");
            Document newOrder = new Document("orderId", "ORD004")
                    .append("date", new Date())
                    .append("total", 99.99)
                    .append("items", Arrays.asList("USB Cable"));
            usersCollection.updateOne(
                    Filters.eq("email", "john@example.com"),
                    Updates.push("orders", newOrder)
            );
            Document johnWithNewOrder = usersCollection.find(Filters.eq("email", "john@example.com")).first();
            if (johnWithNewOrder != null) {
                @SuppressWarnings("unchecked")
                List<Document> orders = (List<Document>) johnWithNewOrder.get("orders");
                logger.info("   John now has {} orders\n", orders.size());
            }
            
            // Add tag to array
            logger.info("3. Adding \"vip\" tag to Jane:");
            usersCollection.updateOne(
                    Filters.eq("email", "jane@example.com"),
                    Updates.addToSet("tags", "vip") // $addToSet prevents duplicates
            );
            Document janeWithTag = usersCollection.find(Filters.eq("email", "jane@example.com")).first();
            if (janeWithTag != null) {
                @SuppressWarnings("unchecked")
                List<String> tags = (List<String>) janeWithTag.get("tags");
                logger.info("   Jane's tags: {}\n", String.join(", ", tags));
            }
            
            // Update nested field in array element
            logger.info("4. Updating order total in array:");
            usersCollection.updateOne(
                    Filters.and(
                            Filters.eq("email", "john@example.com"),
                            Filters.eq("orders.orderId", "ORD001")
                    ),
                    Updates.set("orders.$.total", 349.99) // $ is positional operator
            );
            Document johnUpdated = usersCollection.find(Filters.eq("email", "john@example.com")).first();
            if (johnUpdated != null) {
                @SuppressWarnings("unchecked")
                List<Document> orders = (List<Document>) johnUpdated.get("orders");
                Document ord001 = orders.stream()
                        .filter(o -> "ORD001".equals(o.getString("orderId")))
                        .findFirst()
                        .orElse(null);
                if (ord001 != null) {
                    logger.info("   Order ORD001 new total: ${}\n", ord001.getDouble("total"));
                }
            }
            
            // ============================================
            // PROJECTION WITH EMBEDDED DOCUMENTS
            // ============================================
            logger.info("🎯 === PROJECTION ===\n");
            
            logger.info("Users with only name and address:");
            List<Document> usersWithAddress = usersCollection.find()
                    .projection(Projections.fields(
                            Projections.include("name", "address"),
                            Projections.excludeId()
                    ))
                    .into(new ArrayList<>());
            usersWithAddress.forEach(user -> {
                Document address = (Document) user.get("address");
                logger.info("   {}: {}, {}", user.getString("name"), 
                        address.getString("city"), address.getString("state"));
            });
            logger.info("");
            
            // Cleanup
            logger.info("🧹 Cleaning up test data...");
            usersCollection.deleteMany(new Document());
            logger.info("✅ Cleanup complete\n");
            
            logger.info("✅ Embedded documents examples completed successfully!");
            
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

