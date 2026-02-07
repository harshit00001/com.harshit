package com.harshit.mongodb.basic;

import com.harshit.mongodb.config.DatabaseConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example 3: Query Basics
 * 
 * SIMPLE EXPLANATION:
 * Queries are like asking questions to your database.
 * "Show me all users from New York" or "Find users older than 25"
 * 
 * TECHNICAL EXPLANATION:
 * MongoDB queries use a JSON-like syntax to specify conditions.
 * find() method accepts a query filter object.
 * You can chain methods like sort(), limit(), skip() for result manipulation.
 * 
 * INTERVIEW POINT:
 * - Empty query {} returns all documents
 * - find() returns a FindIterable, use into() to convert to list
 * - Use forEach() to iterate through results
 * - sort() takes Sorts.ascending() or Sorts.descending()
 */
public class Example3Query {
    
    private static final Logger logger = LoggerFactory.getLogger(Example3Query.class);
    
    public static void main(String[] args) {
        MongoClient client = null;
        
        try {
            logger.info("🔄 Connecting to MongoDB...\n");
            MongoDatabase db = DatabaseConfig.connectToDatabase();
            client = DatabaseConfig.getClient();
            
            MongoCollection<Document> productsCollection = db.getCollection("products");
            
            // Insert sample data
            logger.info("📝 Inserting sample products...\n");
            List<Document> products = Arrays.asList(
                    new Document("name", "Laptop").append("category", "Electronics")
                            .append("price", 999.99).append("stock", 50).append("rating", 4.5),
                    new Document("name", "Smartphone").append("category", "Electronics")
                            .append("price", 699.99).append("stock", 100).append("rating", 4.7),
                    new Document("name", "Headphones").append("category", "Electronics")
                            .append("price", 149.99).append("stock", 200).append("rating", 4.3),
                    new Document("name", "Desk Chair").append("category", "Furniture")
                            .append("price", 299.99).append("stock", 30).append("rating", 4.2),
                    new Document("name", "Coffee Table").append("category", "Furniture")
                            .append("price", 199.99).append("stock", 25).append("rating", 4.0),
                    new Document("name", "Bookshelf").append("category", "Furniture")
                            .append("price", 149.99).append("stock", 40).append("rating", 4.4),
                    new Document("name", "Running Shoes").append("category", "Sports")
                            .append("price", 89.99).append("stock", 150).append("rating", 4.6),
                    new Document("name", "Yoga Mat").append("category", "Sports")
                            .append("price", 29.99).append("stock", 300).append("rating", 4.1),
                    new Document("name", "Dumbbells").append("category", "Sports")
                            .append("price", 79.99).append("stock", 80).append("rating", 4.5)
            );
            
            productsCollection.insertMany(products);
            logger.info("✅ Sample products inserted\n");
            
            // ============================================
            // BASIC QUERIES
            // ============================================
            logger.info("🔍 === BASIC QUERIES ===\n");
            
            // 1. Find all documents
            logger.info("1. Finding all products:");
            List<Document> allProducts = productsCollection.find().into(new ArrayList<>());
            logger.info("   Total products: {}\n", allProducts.size());
            
            // 2. Find by exact field value
            logger.info("2. Finding products in Electronics category:");
            List<Document> electronics = productsCollection.find(Filters.eq("category", "Electronics"))
                    .into(new ArrayList<>());
            electronics.forEach(product -> {
                logger.info("   - {} (${})", product.getString("name"), product.getDouble("price"));
            });
            logger.info("");
            
            // 3. Find by multiple conditions (implicit AND)
            logger.info("3. Finding Electronics products under $200:");
            List<Document> cheapElectronics = productsCollection.find(
                    Filters.and(
                            Filters.eq("category", "Electronics"),
                            Filters.lt("price", 200.0)
                    )
            ).into(new ArrayList<>());
            cheapElectronics.forEach(product -> {
                logger.info("   - {} (${})", product.getString("name"), product.getDouble("price"));
            });
            logger.info("");
            
            // ============================================
            // SORTING
            // ============================================
            logger.info("📊 === SORTING ===\n");
            
            // Sort by price (ascending)
            logger.info("1. Products sorted by price (low to high):");
            List<Document> sortedByPrice = productsCollection.find()
                    .sort(Sorts.ascending("price"))
                    .into(new ArrayList<>());
            for (int i = 0; i < sortedByPrice.size(); i++) {
                Document product = sortedByPrice.get(i);
                logger.info("   {}. {} - ${}", i + 1, product.getString("name"), product.getDouble("price"));
            }
            logger.info("");
            
            // Sort by rating (descending)
            logger.info("2. Products sorted by rating (high to low):");
            List<Document> sortedByRating = productsCollection.find()
                    .sort(Sorts.descending("rating"))
                    .into(new ArrayList<>());
            int topCount = Math.min(5, sortedByRating.size());
            for (int i = 0; i < topCount; i++) {
                Document product = sortedByRating.get(i);
                logger.info("   {}. {} - Rating: {}", i + 1, product.getString("name"), product.getDouble("rating"));
            }
            logger.info("");
            
            // Sort by multiple fields
            logger.info("3. Products sorted by category, then price:");
            List<Document> sortedByCategoryAndPrice = productsCollection.find()
                    .sort(Sorts.orderBy(Sorts.ascending("category"), Sorts.ascending("price")))
                    .into(new ArrayList<>());
            sortedByCategoryAndPrice.forEach(product -> {
                logger.info("   {}: {} - ${}", product.getString("category"), 
                        product.getString("name"), product.getDouble("price"));
            });
            logger.info("");
            
            // ============================================
            // LIMITING AND SKIPPING
            // ============================================
            logger.info("📄 === LIMITING AND SKIPPING ===\n");
            
            // Limit results
            logger.info("1. Top 3 most expensive products:");
            List<Document> top3Expensive = productsCollection.find()
                    .sort(Sorts.descending("price"))
                    .limit(3)
                    .into(new ArrayList<>());
            for (int i = 0; i < top3Expensive.size(); i++) {
                Document product = top3Expensive.get(i);
                logger.info("   {}. {} - ${}", i + 1, product.getString("name"), product.getDouble("price"));
            }
            logger.info("");
            
            // Skip and limit (pagination)
            logger.info("2. Pagination example (page 2, 3 items per page):");
            List<Document> page2 = productsCollection.find()
                    .sort(Sorts.ascending("name"))
                    .skip(3)  // Skip first 3 items
                    .limit(3) // Get next 3 items
                    .into(new ArrayList<>());
            for (int i = 0; i < page2.size(); i++) {
                Document product = page2.get(i);
                logger.info("   {}. {}", i + 1, product.getString("name"));
            }
            logger.info("");
            
            // ============================================
            // PROJECTION (Select specific fields)
            // ============================================
            logger.info("🎯 === PROJECTION (Selecting Fields) ===\n");
            
            // Get only name and price
            logger.info("1. Products with only name and price:");
            List<Document> nameAndPrice = productsCollection.find()
                    .projection(Projections.fields(
                            Projections.include("name", "price"),
                            Projections.excludeId()
                    ))
                    .into(new ArrayList<>());
            nameAndPrice.forEach(product -> {
                logger.info("   {}: ${}", product.getString("name"), product.getDouble("price"));
            });
            logger.info("");
            
            // Exclude specific fields
            logger.info("2. Products without stock field:");
            List<Document> withoutStock = productsCollection.find()
                    .projection(Projections.exclude("stock"))
                    .limit(3)
                    .into(new ArrayList<>());
            withoutStock.forEach(product -> {
                logger.info("   {} - Has stock field: {}", product.getString("name"), 
                        product.containsKey("stock"));
            });
            logger.info("");
            
            // ============================================
            // COUNT DOCUMENTS
            // ============================================
            logger.info("🔢 === COUNTING ===\n");
            
            long totalProducts = productsCollection.countDocuments(new Document());
            logger.info("Total products: {}", totalProducts);
            
            long electronicsCount = productsCollection.countDocuments(Filters.eq("category", "Electronics"));
            logger.info("Electronics products: {}", electronicsCount);
            
            long expensiveCount = productsCollection.countDocuments(Filters.gt("price", 500.0));
            logger.info("Products over $500: {}\n", expensiveCount);
            
            // Cleanup
            logger.info("🧹 Cleaning up test data...");
            productsCollection.deleteMany(new Document());
            logger.info("✅ Cleanup complete\n");
            
            logger.info("✅ Query examples completed successfully!");
            
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

