package com.harshit.jpa.basic;

import com.harshit.jpa.basic.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

/**
 * Example 1: JPA Basic Setup - Comprehensive Guide
 * 
 * ====================================================================================
 * INTERVIEW QUESTION: How do you set up JPA in a Java application? Explain step by step.
 * ====================================================================================
 * 
 * DETAILED ANSWER:
 * 
 * Setting up JPA (Java Persistence API) in a Java application involves several
 * important steps. Let me walk you through the complete process with detailed
 * explanations of each component and why it's necessary.
 * 
 * STEP 1: ADD JPA DEPENDENCIES
 * 
 * First, you need to add the required dependencies to your project. The main
 * dependencies are:
 * 
 * a) javax.persistence-api (or jakarta.persistence-api for Jakarta EE 9+):
 *    - This is the JPA specification API
 *    - Contains all the annotations (@Entity, @Id, @OneToMany, etc.)
 *    - Contains interfaces (EntityManager, EntityManagerFactory, etc.)
 *    - This is just the specification, not an implementation
 * 
 * b) hibernate-entitymanager (or another JPA provider):
 *    - This is Hibernate's implementation of the JPA specification
 *    - Provides the actual implementation of EntityManager and other interfaces
 *    - Other providers include EclipseLink, OpenJPA, DataNucleus
 * 
 * c) Database Driver:
 *    - MySQL: mysql-connector-java
 *    - PostgreSQL: postgresql
 *    - H2: h2 (for testing)
 * 
 * In Maven, these would be added to pom.xml:
 * <dependency>
 *     <groupId>javax.persistence</groupId>
 *     <artifactId>javax.persistence-api</artifactId>
 *     <version>2.2</version>
 * </dependency>
 * <dependency>
 *     <groupId>org.hibernate</groupId>
 *     <artifactId>hibernate-entitymanager</artifactId>
 *     <version>5.6.15.Final</version>
 * </dependency>
 * 
 * STEP 2: CREATE persistence.xml CONFIGURATION FILE
 * 
 * The persistence.xml file is the main configuration file for JPA. It must be
 * located in the META-INF folder of your classpath (typically
 * src/main/resources/META-INF/persistence.xml).
 * 
 * This file contains:
 * 
 * a) Persistence Unit:
 *    - A named configuration that groups entity classes and database settings
 *    - You can have multiple persistence units in one application
 *    - Each persistence unit has a unique name
 * 
 * b) Provider:
 *    - Specifies which JPA implementation to use
 *    - Example: org.hibernate.jpa.HibernatePersistenceProvider
 * 
 * c) Database Connection Properties:
 *    - JDBC driver class name
 *    - Database URL
 *    - Username and password
 *    - Connection pool settings
 * 
 * d) Hibernate-specific Properties:
 *    - Dialect (database-specific SQL dialect)
 *    - DDL auto (create, update, validate, none)
 *    - Show SQL (for debugging)
 *    - Format SQL (for readability)
 * 
 * Example persistence.xml structure:
 * <persistence-unit name="jpa-examples">
 *     <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
 *     <properties>
 *         <property name="javax.persistence.jdbc.url" value="jdbc:h2:mem:testdb"/>
 *         <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
 *         <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
 *         <property name="hibernate.hbm2ddl.auto" value="create-drop"/>
 *     </properties>
 * </persistence-unit>
 * 
 * STEP 3: CREATE ENTITY CLASSES
 * 
 * Entity classes are Java classes that represent database tables. They must:
 * 
 * a) Be annotated with @Entity
 * b) Have a no-argument constructor (required by JPA)
 * c) Have at least one field annotated with @Id (primary key)
 * d) Not be final (Hibernate uses proxies)
 * e) Have getters and setters for persistent fields
 * 
 * Example:
 * @Entity
 * @Table(name = "products")
 * public class Product {
 *     @Id
 *     @GeneratedValue(strategy = GenerationType.IDENTITY)
 *     private Long id;
 *     
 *     @Column(name = "name", nullable = false)
 *     private String name;
 * }
 * 
 * STEP 4: CREATE EntityManagerFactory
 * 
 * EntityManagerFactory is a factory for creating EntityManager instances. It's:
 * 
 * a) Expensive to create (loads configuration, validates mappings, etc.)
 * b) Thread-safe (can be shared across threads)
 * c) Should be created once per application (singleton pattern)
 * d) Should be closed when application shuts down
 * 
 * Creation:
 * EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-examples");
 * 
 * The string parameter is the persistence unit name from persistence.xml.
 * 
 * STEP 5: CREATE EntityManager
 * 
 * EntityManager is the main interface for interacting with the persistence context.
 * It's:
 * 
 * a) Lightweight (cheap to create)
 * b) Not thread-safe (one per thread)
 * c) Short-lived (typically one per transaction)
 * d) Represents a persistence context (set of managed entities)
 * 
 * Creation:
 * EntityManager em = emf.createEntityManager();
 * 
 * STEP 6: USE EntityManager FOR OPERATIONS
 * 
 * Common operations:
 * 
 * a) persist(entity): Makes entity managed and schedules it for insertion
 * b) find(Class, id): Finds entity by primary key
 * c) merge(entity): Merges detached entity into persistence context
 * d) remove(entity): Removes entity from database
 * e) createQuery(JPQL): Creates JPQL query
 * 
 * STEP 7: MANAGE TRANSACTIONS
 * 
 * All database operations must be within a transaction:
 * 
 * em.getTransaction().begin();
 * try {
 *     // Perform operations
 *     em.getTransaction().commit();
 * } catch (Exception e) {
 *     em.getTransaction().rollback();
 * }
 * 
 * STEP 8: CLOSE RESOURCES
 * 
 * Always close EntityManager and EntityManagerFactory:
 * 
 * em.close();  // Closes persistence context
 * emf.close(); // Closes factory and releases resources
 * 
 * KEY COMPONENTS EXPLAINED:
 * 
 * 1. EntityManagerFactory:
 *    - Factory for creating EntityManager instances
 *    - Expensive to create (should be singleton)
 *    - Thread-safe
 *    - Loads persistence.xml configuration
 *    - Validates entity mappings
 *    - Creates database connection pool
 * 
 * 2. EntityManager:
 *    - Main interface for database operations
 *    - Represents a persistence context
 *    - Not thread-safe
 *    - Manages entity lifecycle
 *    - Tracks changes to managed entities
 *    - Executes queries
 * 
 * 3. Persistence Context:
 *    - Set of managed entity instances
 *    - Associated with one EntityManager
 *    - Tracks changes automatically
 *    - Synchronizes with database on commit
 *    - Provides identity guarantee (same ID = same object instance)
 * 
 * 4. EntityTransaction:
 *    - Interface for transaction management
 *    - begin(): Starts transaction
 *    - commit(): Commits transaction (saves changes)
 *    - rollback(): Rolls back transaction (discards changes)
 *    - isActive(): Checks if transaction is active
 * 
 * REAL-WORLD SCENARIO:
 * 
 * In a real e-commerce application, you would:
 * 
 * 1. Create EntityManagerFactory at application startup (Spring Boot does this automatically)
 * 2. Create EntityManager per request/transaction (Spring manages this)
 * 3. Use EntityManager to persist orders, products, customers
 * 4. Handle transactions properly to ensure data consistency
 * 5. Close EntityManager after each request
 * 6. Close EntityManagerFactory on application shutdown
 * 
 * In Spring Boot, most of this is handled automatically through:
 * - @PersistenceContext annotation
 * - @Transactional annotation
 * - Spring Data JPA repositories
 * 
 * However, understanding the manual setup is crucial for:
 * - Debugging issues
 * - Understanding how JPA works internally
 * - Working with standalone Java applications
 * - Interview questions about JPA fundamentals
 * 
 * This example demonstrates the manual setup process, which gives you a deep
 * understanding of how JPA works under the hood.
 */
public class Example1JPABasicSetup {
    
    private static final Logger logger = LoggerFactory.getLogger(Example1JPABasicSetup.class);
    
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            logger.info("🔄 === JPA BASIC SETUP ===\n");
            
            // ========================================================================
            // STEP 1: CREATE EntityManagerFactory
            // ========================================================================
            // EntityManagerFactory is the entry point to JPA. It's responsible for:
            // - Loading and parsing persistence.xml configuration
            // - Validating entity mappings and annotations
            // - Creating database connection pool
            // - Initializing the JPA provider (Hibernate in our case)
            // - Building metadata about entities
            //
            // IMPORTANT: EntityManagerFactory is expensive to create because it:
            // - Reads and parses configuration files
            // - Validates all entity mappings
            // - Creates connection pools
            // - Builds internal caches and metadata
            //
            // Therefore, it should be created ONCE per application and reused.
            // In production applications, it's typically created at application startup
            // and closed at shutdown. In Spring Boot, this is handled automatically.
            logger.info("1. Creating EntityManagerFactory...");
            logger.info("   This step:");
            logger.info("   - Loads persistence.xml from META-INF folder");
            logger.info("   - Validates entity mappings and annotations");
            logger.info("   - Creates database connection pool");
            logger.info("   - Initializes Hibernate as JPA provider");
            logger.info("   - Builds metadata about all entities");
            logger.info("   - This is EXPENSIVE - should be done once per application\n");
            
            // The string "jpa-examples" must match the persistence-unit name in persistence.xml
            // This tells JPA which configuration to use (you can have multiple persistence units)
            emf = Persistence.createEntityManagerFactory("jpa-examples");
            logger.info("   ✅ EntityManagerFactory created successfully");
            logger.info("   Persistence unit 'jpa-examples' loaded from persistence.xml\n");
            
            // ========================================================================
            // STEP 2: CREATE EntityManager
            // ========================================================================
            // EntityManager is the main interface for interacting with the database.
            // It represents a persistence context - a set of managed entity instances.
            //
            // Key characteristics:
            // - Lightweight (cheap to create compared to EntityManagerFactory)
            // - Not thread-safe (each thread should have its own)
            // - Short-lived (typically one per transaction or request)
            // - Manages entity lifecycle (NEW -> MANAGED -> DETACHED -> REMOVED)
            // - Tracks changes to managed entities automatically
            //
            // The persistence context is like a cache of entities that are currently
            // being managed. When you persist, find, or merge entities, they become
            // part of this context, and JPA tracks any changes you make to them.
            logger.info("2. Creating EntityManager...");
            logger.info("   EntityManager:");
            logger.info("   - Represents a persistence context (set of managed entities)");
            logger.info("   - Is lightweight and cheap to create");
            logger.info("   - Is NOT thread-safe (one per thread)");
            logger.info("   - Tracks changes to managed entities automatically");
            logger.info("   - Should be closed after use\n");
            
            em = emf.createEntityManager();
            logger.info("   ✅ EntityManager created");
            logger.info("   Persistence context is now active\n");
            
            // ========================================================================
            // STEP 3: BEGIN TRANSACTION
            // ========================================================================
            // In JPA, all database operations (INSERT, UPDATE, DELETE) must be performed
            // within a transaction. This ensures ACID properties:
            //
            // - Atomicity: All operations succeed or all fail
            // - Consistency: Database remains in valid state
            // - Isolation: Transactions don't interfere with each other
            // - Durability: Committed changes are permanent
            //
            // Without a transaction, you can only perform read operations (SELECT).
            // Write operations require an active transaction.
            logger.info("3. Beginning transaction...");
            logger.info("   Transactions ensure ACID properties:");
            logger.info("   - Atomicity: All or nothing");
            logger.info("   - Consistency: Valid state maintained");
            logger.info("   - Isolation: Transactions don't interfere");
            logger.info("   - Durability: Changes are permanent");
            logger.info("   All write operations (persist, merge, remove) require transaction\n");
            
            em.getTransaction().begin();
            logger.info("   ✅ Transaction started");
            logger.info("   Now we can perform write operations\n");
            
            // ========================================================================
            // STEP 4: PERFORM OPERATIONS (PERSIST)
            // ========================================================================
            // The persist() method makes an entity managed and schedules it for insertion
            // into the database. Here's what happens:
            //
            // 1. Entity state changes from NEW (transient) to MANAGED (persistent)
            // 2. Entity is added to the persistence context
            // 3. JPA starts tracking changes to the entity
            // 4. Entity is scheduled for INSERT on commit
            // 5. If entity has @GeneratedValue, ID is generated (depending on strategy)
            //
            // IMPORTANT: persist() doesn't immediately insert into database!
            // The actual INSERT happens when you commit the transaction.
            // This is called "deferred execution" and allows JPA to batch operations.
            logger.info("4. Creating and persisting product...");
            logger.info("   Creating Product entity (NEW/transient state)...");
            
            Product product = new Product("Laptop", "High-performance laptop", new BigDecimal("999.99"));
            product.setSku("LAP-001");
            product.setStockQuantity(50);
            logger.info("   Product created: {}", product);
            logger.info("   Entity state: NEW (not yet in persistence context)\n");
            
            logger.info("   Calling persist()...");
            logger.info("   This will:");
            logger.info("   - Change entity state from NEW to MANAGED");
            logger.info("   - Add entity to persistence context");
            logger.info("   - Schedule INSERT for transaction commit");
            logger.info("   - Generate ID if @GeneratedValue is configured");
            logger.info("   - Note: INSERT doesn't happen yet, only on commit!\n");
            
            em.persist(product);
            logger.info("   ✅ Product persisted (now in MANAGED state)");
            logger.info("   Product ID generated: {}", product.getId());
            logger.info("   Entity is now tracked by JPA - any changes will be saved on commit\n");
            
            // ========================================================================
            // STEP 5: COMMIT TRANSACTION
            // ========================================================================
            // commit() is where the magic happens! This is when:
            // - All INSERT statements are executed
            // - All UPDATE statements are executed (for modified managed entities)
            // - All DELETE statements are executed
            // - Changes are made permanent in the database
            // - Transaction is completed
            //
            // If commit() succeeds, all changes are permanent.
            // If commit() fails, the transaction is rolled back automatically.
            logger.info("5. Committing transaction...");
            logger.info("   This is when actual database operations happen:");
            logger.info("   - INSERT INTO products (id, name, price, ...) VALUES (...)");
            logger.info("   - All changes to managed entities are synchronized");
            logger.info("   - Transaction is completed and changes are permanent");
            logger.info("   - If this fails, transaction is automatically rolled back\n");
            
            em.getTransaction().commit();
            logger.info("   ✅ Transaction committed successfully");
            logger.info("   Product is now saved in database with ID: {}\n", product.getId());
            
            // ========================================================================
            // STEP 6: READ ENTITY (FIND)
            // ========================================================================
            // find() retrieves an entity by its primary key. It:
            // - Executes SELECT query immediately (eager loading)
            // - Returns entity in MANAGED state
            // - Returns null if entity doesn't exist (unlike getReference())
            // - Can use first-level cache if entity was already loaded
            //
            // Since we're using the same EntityManager, if the entity is already
            // in the persistence context, it returns the same instance (identity guarantee).
            logger.info("6. Reading product by ID using find()...");
            logger.info("   find() method:");
            logger.info("   - Executes SELECT query immediately");
            logger.info("   - Returns entity in MANAGED state");
            logger.info("   - Returns null if not found");
            logger.info("   - Can use first-level cache if already loaded");
            logger.info("   - SQL: SELECT * FROM products WHERE id = ?\n");
            
            Product foundProduct = em.find(Product.class, product.getId());
            logger.info("   ✅ Found product: {}", foundProduct);
            logger.info("   Entity state: MANAGED (in persistence context)");
            logger.info("   Any changes to this entity will be tracked and saved\n");
            
            logger.info("✅ JPA basic setup completed successfully!");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.info("   Transaction rolled back");
            }
        } finally {
            // Step 7: Close resources
            if (em != null) {
                em.close();
                logger.info("\n🔒 EntityManager closed");
            }
            if (emf != null) {
                emf.close();
                logger.info("🔒 EntityManagerFactory closed");
            }
        }
    }
}

