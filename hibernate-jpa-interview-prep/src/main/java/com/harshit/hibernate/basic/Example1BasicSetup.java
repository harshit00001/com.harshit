package com.harshit.hibernate.basic;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example 1: Basic Hibernate Setup
 * 
 * INTERVIEW QUESTION: What is Hibernate and how do you set it up?
 * 
 * ANSWER:
 * Hibernate is an ORM (Object-Relational Mapping) framework that simplifies
 * database operations by mapping Java objects to database tables.
 * 
 * Key Components:
 * 1. Configuration: Loads hibernate.cfg.xml or programmatic configuration
 * 2. SessionFactory: Thread-safe factory for creating Session instances
 * 3. Session: Single-threaded, short-lived object representing a conversation with DB
 * 4. Transaction: Unit of work with the database
 * 
 * SETUP STEPS:
 * 1. Add dependencies (hibernate-core, database driver)
 * 2. Create hibernate.cfg.xml with database connection details
 * 3. Create entity classes with @Entity annotation
 * 4. Create SessionFactory from Configuration
 * 5. Get Session from SessionFactory
 * 6. Perform operations within a transaction
 * 
 * REAL-WORLD SCENARIO:
 * In an e-commerce application, you need to persist customer orders.
 * Instead of writing raw SQL, Hibernate maps Order Java objects to database tables.
 */
public class Example1BasicSetup {
    
    private static final Logger logger = LoggerFactory.getLogger(Example1BasicSetup.class);
    
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        Session session = null;
        
        try {
            logger.info("🔄 === HIBERNATE BASIC SETUP ===\n");
            
            // Step 1: Create Configuration and load hibernate.cfg.xml
            logger.info("1. Loading Hibernate configuration...");
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            
            // Step 2: Build SessionFactory (expensive operation, should be singleton)
            logger.info("2. Building SessionFactory...");
            sessionFactory = configuration.buildSessionFactory();
            logger.info("   ✅ SessionFactory created successfully\n");
            
            // Step 3: Open Session (represents a database connection)
            logger.info("3. Opening Session...");
            session = sessionFactory.openSession();
            logger.info("   ✅ Session opened\n");
            
            // Step 4: Begin Transaction
            logger.info("4. Beginning transaction...");
            session.beginTransaction();
            logger.info("   ✅ Transaction started\n");
            
            // Step 5: Perform database operations (examples in other files)
            logger.info("5. Ready to perform database operations");
            logger.info("   (See other examples for CRUD operations)\n");
            
            // Step 6: Commit Transaction
            logger.info("6. Committing transaction...");
            session.getTransaction().commit();
            logger.info("   ✅ Transaction committed\n");
            
            logger.info("✅ Basic setup completed successfully!");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
                logger.info("   Transaction rolled back");
            }
        } finally {
            // Step 7: Close resources
            if (session != null) {
                session.close();
                logger.info("\n🔒 Session closed");
            }
            if (sessionFactory != null) {
                sessionFactory.close();
                logger.info("🔒 SessionFactory closed");
            }
        }
    }
}

