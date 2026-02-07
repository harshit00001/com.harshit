package com.harshit.mongodb.basic;

import com.harshit.mongodb.config.DatabaseConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example 4: MongoDB Operators
 * 
 * SIMPLE EXPLANATION:
 * Operators are special keywords that help you write more powerful queries.
 * Like using "greater than" (>), "less than" (<), "or", "and" in queries.
 * 
 * TECHNICAL EXPLANATION:
 * MongoDB operators start with $ sign.
 * Comparison operators: $gt, $lt, $gte, $lte, $eq, $ne, $in, $nin
 * Logical operators: $and, $or, $not, $nor
 * Array operators: $all, $elemMatch, $size
 * 
 * INTERVIEW POINT:
 * - Operators are prefixed with $ to distinguish from field names
 * - $in is useful for matching any value in an array
 * - $or allows multiple conditions where any can be true
 * - $and is implicit when multiple conditions are in same object
 */
public class Example4Operators {
    
    private static final Logger logger = LoggerFactory.getLogger(Example4Operators.class);
    
    public static void main(String[] args) {
        MongoClient client = null;
        
        try {
            logger.info("🔄 Connecting to MongoDB...\n");
            MongoDatabase db = DatabaseConfig.connectToDatabase();
            client = DatabaseConfig.getClient();
            
            MongoCollection<Document> employeesCollection = db.getCollection("employees");
            
            // Insert sample data
            logger.info("📝 Inserting sample employees...\n");
            List<Document> employees = Arrays.asList(
                    new Document("name", "Alice").append("age", 28).append("salary", 75000)
                            .append("department", "Engineering")
                            .append("skills", Arrays.asList("Java", "Python", "SQL"))
                            .append("active", true),
                    new Document("name", "Bob").append("age", 35).append("salary", 95000)
                            .append("department", "Engineering")
                            .append("skills", Arrays.asList("Java", "JavaScript", "React"))
                            .append("active", true),
                    new Document("name", "Charlie").append("age", 42).append("salary", 110000)
                            .append("department", "Management")
                            .append("skills", Arrays.asList("Leadership", "Strategy"))
                            .append("active", true),
                    new Document("name", "Diana").append("age", 29).append("salary", 80000)
                            .append("department", "Design")
                            .append("skills", Arrays.asList("Photoshop", "Figma", "UI/UX"))
                            .append("active", true),
                    new Document("name", "Eve").append("age", 31).append("salary", 88000)
                            .append("department", "Engineering")
                            .append("skills", Arrays.asList("Python", "Docker", "Kubernetes"))
                            .append("active", false),
                    new Document("name", "Frank").append("age", 26).append("salary", 70000)
                            .append("department", "Marketing")
                            .append("skills", Arrays.asList("SEO", "Content Writing"))
                            .append("active", true),
                    new Document("name", "Grace").append("age", 38).append("salary", 100000)
                            .append("department", "Engineering")
                            .append("skills", Arrays.asList("Java", "Spring", "Microservices"))
                            .append("active", true),
                    new Document("name", "Henry").append("age", 33).append("salary", 92000)
                            .append("department", "Sales")
                            .append("skills", Arrays.asList("Negotiation", "CRM"))
                            .append("active", true)
            );
            
            employeesCollection.insertMany(employees);
            logger.info("✅ Sample employees inserted\n");
            
            // ============================================
            // COMPARISON OPERATORS
            // ============================================
            logger.info("🔍 === COMPARISON OPERATORS ===\n");
            
            // $gt (greater than)
            logger.info("1. Employees older than 30:");
            List<Document> olderThan30 = employeesCollection.find(Filters.gt("age", 30))
                    .into(new ArrayList<>());
            olderThan30.forEach(emp -> {
                logger.info("   - {} (age: {})", emp.getString("name"), emp.getInteger("age"));
            });
            logger.info("");
            
            // $gte (greater than or equal)
            logger.info("2. Employees 30 or older:");
            List<Document> thirtyOrOlder = employeesCollection.find(Filters.gte("age", 30))
                    .into(new ArrayList<>());
            logger.info("   Found: {} employees\n", thirtyOrOlder.size());
            
            // $lt (less than)
            logger.info("3. Employees with salary less than $80,000:");
            List<Document> lowSalary = employeesCollection.find(Filters.lt("salary", 80000))
                    .into(new ArrayList<>());
            lowSalary.forEach(emp -> {
                logger.info("   - {} (${})", emp.getString("name"), emp.getInteger("salary"));
            });
            logger.info("");
            
            // $lte (less than or equal)
            logger.info("4. Employees with salary $80,000 or less:");
            List<Document> salary80kOrLess = employeesCollection.find(Filters.lte("salary", 80000))
                    .into(new ArrayList<>());
            logger.info("   Found: {} employees\n", salary80kOrLess.size());
            
            // $ne (not equal)
            logger.info("5. Inactive employees:");
            List<Document> inactive = employeesCollection.find(Filters.ne("active", true))
                    .into(new ArrayList<>());
            inactive.forEach(emp -> {
                logger.info("   - {} (active: {})", emp.getString("name"), emp.getBoolean("active"));
            });
            logger.info("");
            
            // Range query (between)
            logger.info("6. Employees with salary between $80,000 and $100,000:");
            List<Document> salaryRange = employeesCollection.find(
                    Filters.and(
                            Filters.gte("salary", 80000),
                            Filters.lte("salary", 100000)
                    )
            ).into(new ArrayList<>());
            salaryRange.forEach(emp -> {
                logger.info("   - {} (${})", emp.getString("name"), emp.getInteger("salary"));
            });
            logger.info("");
            
            // ============================================
            // LOGICAL OPERATORS
            // ============================================
            logger.info("🔗 === LOGICAL OPERATORS ===\n");
            
            // $or
            logger.info("1. Employees in Engineering OR Management:");
            List<Document> engOrMgmt = employeesCollection.find(
                    Filters.or(
                            Filters.eq("department", "Engineering"),
                            Filters.eq("department", "Management")
                    )
            ).into(new ArrayList<>());
            engOrMgmt.forEach(emp -> {
                logger.info("   - {} ({})", emp.getString("name"), emp.getString("department"));
            });
            logger.info("");
            
            // $and (explicit)
            logger.info("2. Active Engineering employees over 30:");
            List<Document> activeEngOver30 = employeesCollection.find(
                    Filters.and(
                            Filters.eq("department", "Engineering"),
                            Filters.gt("age", 30),
                            Filters.eq("active", true)
                    )
            ).into(new ArrayList<>());
            activeEngOver30.forEach(emp -> {
                logger.info("   - {} (age: {})", emp.getString("name"), emp.getInteger("age"));
            });
            logger.info("");
            
            // $and (implicit - same as above)
            logger.info("3. Same query using implicit AND:");
            List<Document> implicitAnd = employeesCollection.find(
                    Filters.and(
                            Filters.eq("department", "Engineering"),
                            Filters.gt("age", 30),
                            Filters.eq("active", true)
                    )
            ).into(new ArrayList<>());
            logger.info("   Found: {} employees\n", implicitAnd.size());
            
            // $not
            logger.info("4. Employees NOT in Engineering:");
            List<Document> notEngineering = employeesCollection.find(
                    Filters.ne("department", "Engineering")
            ).into(new ArrayList<>());
            notEngineering.forEach(emp -> {
                logger.info("   - {} ({})", emp.getString("name"), emp.getString("department"));
            });
            logger.info("");
            
            // ============================================
            // ARRAY OPERATORS
            // ============================================
            logger.info("📋 === ARRAY OPERATORS ===\n");
            
            // $in (matches any value in array)
            logger.info("1. Employees with Java OR Python skills:");
            List<Document> javaOrPython = employeesCollection.find(
                    Filters.in("skills", Arrays.asList("Java", "Python"))
            ).into(new ArrayList<>());
            javaOrPython.forEach(emp -> {
                @SuppressWarnings("unchecked")
                List<String> skills = (List<String>) emp.get("skills");
                logger.info("   - {} (skills: {})", emp.getString("name"), String.join(", ", skills));
            });
            logger.info("");
            
            // $nin (not in)
            logger.info("2. Employees without Java or Python:");
            List<Document> noJavaOrPython = employeesCollection.find(
                    Filters.nin("skills", Arrays.asList("Java", "Python"))
            ).into(new ArrayList<>());
            noJavaOrPython.forEach(emp -> {
                @SuppressWarnings("unchecked")
                List<String> skills = (List<String>) emp.get("skills");
                logger.info("   - {} (skills: {})", emp.getString("name"), String.join(", ", skills));
            });
            logger.info("");
            
            // $all (must have all specified values)
            logger.info("3. Employees with BOTH Java AND Python:");
            List<Document> javaAndPython = employeesCollection.find(
                    Filters.all("skills", Arrays.asList("Java", "Python"))
            ).into(new ArrayList<>());
            if (javaAndPython.size() > 0) {
                javaAndPython.forEach(emp -> {
                    @SuppressWarnings("unchecked")
                    List<String> skills = (List<String>) emp.get("skills");
                    logger.info("   - {} (skills: {})", emp.getString("name"), String.join(", ", skills));
                });
            } else {
                logger.info("   (No employees found with both skills)");
            }
            logger.info("");
            
            // $size (array length)
            logger.info("4. Employees with exactly 3 skills:");
            List<Document> threeSkills = employeesCollection.find(
                    Filters.size("skills", 3)
            ).into(new ArrayList<>());
            threeSkills.forEach(emp -> {
                @SuppressWarnings("unchecked")
                List<String> skills = (List<String>) emp.get("skills");
                logger.info("   - {} ({} skills: {})", emp.getString("name"), 
                        skills.size(), String.join(", ", skills));
            });
            logger.info("");
            
            // ============================================
            // COMBINING OPERATORS
            // ============================================
            logger.info("🔀 === COMBINING OPERATORS ===\n");
            
            logger.info("Complex query: Active Engineering employees with salary > $85k and Java skill:");
            List<Document> complex = employeesCollection.find(
                    Filters.and(
                            Filters.eq("department", "Engineering"),
                            Filters.eq("active", true),
                            Filters.gt("salary", 85000),
                            Filters.in("skills", Arrays.asList("Java"))
                    )
            ).into(new ArrayList<>());
            complex.forEach(emp -> {
                @SuppressWarnings("unchecked")
                List<String> skills = (List<String>) emp.get("skills");
                logger.info("   - {} (${}, skills: {})", emp.getString("name"), 
                        emp.getInteger("salary"), String.join(", ", skills));
            });
            logger.info("");
            
            // Cleanup
            logger.info("🧹 Cleaning up test data...");
            employeesCollection.deleteMany(new Document());
            logger.info("✅ Cleanup complete\n");
            
            logger.info("✅ Operator examples completed successfully!");
            
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

