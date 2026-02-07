package com.harshit.hibernate.basic;

import com.harshit.hibernate.basic.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Example 2: CRUD Operations in Hibernate - Comprehensive Guide
 * 
 * ====================================================================================
 * INTERVIEW QUESTION: How do you perform CRUD operations in Hibernate? Explain each
 * operation in detail, including the differences between similar methods.
 * ====================================================================================
 * 
 * DETAILED ANSWER:
 * 
 * CRUD stands for Create, Read, Update, Delete - the four basic operations you can
 * perform on data. Hibernate provides multiple methods for each operation, and
 * understanding the differences is crucial for writing efficient and correct code.
 * 
 * ====================================================================================
 * CREATE OPERATIONS
 * ====================================================================================
 * 
 * Hibernate provides several methods to create (persist) new entities:
 * 
 * 1. save() METHOD:
 *    - Hibernate-specific method (not part of JPA standard)
 *    - Returns the generated primary key value immediately
 *    - Makes entity managed and schedules it for insertion
 *    - If entity already has an ID, it may throw exception
 *    - The ID is available immediately after calling save()
 *    
 *    Usage:
 *    Long id = (Long) session.save(user);
 *    // id is available immediately
 * 
 * 2. persist() METHOD:
 *    - JPA standard method (also available in Hibernate)
 *    - Returns void (doesn't return generated ID)
 *    - Makes entity managed and schedules it for insertion
 *    - More strict than save() - may throw exception if entity already exists
 *    - ID is set on the entity object after flush/commit
 *    
 *    Usage:
 *    session.persist(user);
 *    // user.getId() is available after flush or commit
 * 
 * 3. saveOrUpdate() METHOD:
 *    - Hibernate-specific method
 *    - Saves if entity is new (no ID), updates if entity exists (has ID)
 *    - Useful when you're not sure if entity is new or existing
 *    - Automatically determines whether to INSERT or UPDATE
 *    
 *    Usage:
 *    session.saveOrUpdate(user); // Works for both new and existing entities
 * 
 * KEY DIFFERENCES: save() vs persist()
 * 
 * - Return Value:
 *   * save() returns the generated ID immediately
 *   * persist() returns void, ID is set on entity later
 * 
 * - Behavior with existing entities:
 *   * save() may reassign ID if entity already has one
 *   * persist() throws exception if entity already exists
 * 
 * - When to use:
 *   * Use save() when you need the ID immediately
 *   * Use persist() for JPA compliance and stricter behavior
 *   * Use saveOrUpdate() when entity state is unknown
 * 
 * ====================================================================================
 * READ OPERATIONS
 * ====================================================================================
 * 
 * Hibernate provides several methods to read (retrieve) entities:
 * 
 * 1. get() METHOD:
 *    - Eager loading - executes SELECT query immediately
 *    - Returns null if entity doesn't exist
 *    - Always hits the database (unless in first-level cache)
 *    - Safe to use - won't throw exception if not found
 *    - Returns actual entity object, not a proxy
 *    
 *    Usage:
 *    User user = session.get(User.class, 1L);
 *    if (user != null) {
 *        // Entity exists and is loaded
 *    }
 * 
 * 2. load() METHOD:
 *    - Lazy loading - returns a proxy object immediately
 *    - Throws exception if entity doesn't exist (when proxy is accessed)
 *    - Doesn't hit database until you access a property
 *    - More efficient if you might not need the entity
 *    - Returns Hibernate proxy, not actual entity
 *    
 *    Usage:
 *    User user = session.load(User.class, 1L); // Returns proxy
 *    String name = user.getName(); // Now database is queried
 *    // If entity doesn't exist, exception is thrown here
 * 
 * 3. createQuery() METHOD:
 *    - Used for HQL (Hibernate Query Language) queries
 *    - More flexible than get()/load() - can query by any criteria
 *    - Returns list of entities matching criteria
 *    - Supports WHERE clauses, JOINs, aggregations, etc.
 *    
 *    Usage:
 *    List<User> users = session.createQuery("FROM User WHERE age > :age", User.class)
 *        .setParameter("age", 25)
 *        .list();
 * 
 * KEY DIFFERENCES: get() vs load()
 * 
 * - Database Access:
 *   * get() hits database immediately (eager)
 *   * load() returns proxy, hits database on access (lazy)
 * 
 * - Not Found Behavior:
 *   * get() returns null if not found
 *   * load() throws exception when proxy is accessed
 * 
 * - When to use:
 *   * Use get() when you need the entity immediately and want null if not found
 *   * Use load() when you might not need the entity and want lazy loading
 *   * Use createQuery() for complex queries and filtering
 * 
 * ====================================================================================
 * UPDATE OPERATIONS
 * ====================================================================================
 * 
 * Hibernate provides several methods to update entities:
 * 
 * 1. update() METHOD:
 *    - Updates a detached entity (entity not in current session)
 *    - Throws exception if entity is already in session with different ID
 *    - Reattaches entity to session
 *    - Use when you're sure entity exists and is detached
 *    
 *    Usage:
 *    User detachedUser = ...; // Entity from previous session
 *    session.update(detachedUser); // Reattaches and updates
 * 
 * 2. merge() METHOD:
 *    - Merges detached entity into current session
 *    - If entity with same ID exists in session, copies state to it
 *    - If entity doesn't exist, creates new managed entity
 *    - Returns managed entity (may be different object than input)
 *    - Safer than update() - handles more cases
 *    
 *    Usage:
 *    User detachedUser = ...;
 *    User managedUser = (User) session.merge(detachedUser);
 *    // managedUser is the managed entity (may be different object)
 * 
 * 3. saveOrUpdate() METHOD:
 *    - Saves if new, updates if existing
 *    - Automatically determines entity state
 *    - Convenient but less explicit
 *    
 *    Usage:
 *    session.saveOrUpdate(user); // Works for both new and existing
 * 
 * KEY DIFFERENCES: update() vs merge()
 * 
 * - Detached Entity Handling:
 *   * update() throws exception if entity already in session
 *   * merge() handles this case gracefully
 * 
 * - Return Value:
 *   * update() returns void
 *   * merge() returns managed entity (may be different object)
 * 
 * - When to use:
 *   * Use update() when you're sure entity is detached and not in session
 *   * Use merge() for safer, more flexible updates
 *   * For managed entities, just modify and commit (no need to call update/merge)
 * 
 * IMPORTANT: For managed entities (entities already in session), you don't need
 * to call update() or merge(). Just modify the entity and commit - Hibernate
 * tracks changes automatically!
 * 
 * ====================================================================================
 * DELETE OPERATIONS
 * ====================================================================================
 * 
 * Hibernate provides methods to delete entities:
 * 
 * 1. delete() / remove() METHOD:
 *    - Removes entity from database
 *    - Entity must be in session (managed or you need to load it first)
 *    - Marks entity for deletion
 *    - Actual DELETE happens on commit
 *    
 *    Usage:
 *    User user = session.get(User.class, 1L);
 *    session.delete(user); // or session.remove(user) in JPA
 * 
 * 2. createQuery() with DELETE:
 *    - Bulk delete using HQL
 *    - More efficient for deleting multiple entities
 *    - Bypasses entity lifecycle (no events fired)
 *    
 *    Usage:
 *    int deleted = session.createQuery("DELETE FROM User WHERE age < :age")
 *        .setParameter("age", 18)
 *        .executeUpdate();
 * 
 * ====================================================================================
 * REAL-WORLD SCENARIO: USER MANAGEMENT SYSTEM
 * ====================================================================================
 * 
 * In a real user management system, you would:
 * 
 * 1. CREATE: When a new user registers
 *    - Use save() or persist() to create new user account
 *    - Validate data before persisting
 *    - Handle duplicate email/username exceptions
 * 
 * 2. READ: When retrieving user information
 *    - Use get() to fetch user by ID (safe, returns null if not found)
 *    - Use createQuery() to search users by criteria (name, email, etc.)
 *    - Use load() if you might not need all user data (lazy loading)
 * 
 * 3. UPDATE: When user updates profile
 *    - Load user with get() or find()
 *    - Modify properties (Hibernate tracks changes automatically)
 *    - Commit transaction (no need to call update() for managed entities)
 *    - Use merge() if user object came from outside session (e.g., from web form)
 * 
 * 4. DELETE: When deactivating or deleting user
 *    - Load user first
 *    - Call delete() to remove from database
 *    - Consider soft delete (set status to INACTIVE) instead of hard delete
 *    - Handle cascade deletes (delete related records)
 * 
 * BEST PRACTICES:
 * 
 * 1. Always use transactions for write operations
 * 2. Handle exceptions properly (rollback on error)
 * 3. Close sessions in finally block
 * 4. Use get() when you need null if not found
 * 5. Use load() when you want lazy loading
 * 6. For managed entities, just modify and commit (no update/merge needed)
 * 7. Use merge() for detached entities from outside session
 * 8. Consider soft deletes instead of hard deletes for audit trails
 * 
 * This example demonstrates all these operations with working code and detailed
 * explanations of when and why to use each method.
 */
public class Example2CRUDOperations {
    
    private static final Logger logger = LoggerFactory.getLogger(Example2CRUDOperations.class);
    
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        Session session = null;
        
        try {
            logger.info("🔄 === HIBERNATE CRUD OPERATIONS ===\n");
            
            // Setup
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(User.class);
            sessionFactory = configuration.buildSessionFactory();
            session = sessionFactory.openSession();
            
            // ============================================
            // CREATE OPERATIONS
            // ============================================
            logger.info("📝 === CREATE OPERATIONS ===\n");
            
            session.beginTransaction();
            
            // Create new user using save()
            logger.info("1. Creating user using save()...");
            User user1 = new User("john_doe", "john.doe@example.com");
            user1.setFirstName("John");
            user1.setLastName("Doe");
            user1.setAge(30);
            Long userId = (Long) session.save(user1);
            logger.info("   ✅ User created with ID: {}\n", userId);
            
            // Create user using persist()
            logger.info("2. Creating user using persist()...");
            User user2 = new User("jane_smith", "jane.smith@example.com");
            user2.setFirstName("Jane");
            user2.setLastName("Smith");
            user2.setAge(25);
            session.persist(user2);
            logger.info("   ✅ User persisted with ID: {}\n", user2.getId());
            
            session.getTransaction().commit();
            
            // ============================================
            // READ OPERATIONS
            // ============================================
            logger.info("📖 === READ OPERATIONS ===\n");
            
            // Read using get() - returns null if not found
            logger.info("1. Reading user using get()...");
            session.beginTransaction();
            User retrievedUser1 = session.get(User.class, userId);
            if (retrievedUser1 != null) {
                logger.info("   ✅ Found user: {}\n", retrievedUser1);
            } else {
                logger.info("   ❌ User not found\n");
            }
            
            // Read using load() - throws exception if not found (lazy loading)
            logger.info("2. Reading user using load()...");
            User retrievedUser2 = session.load(User.class, user2.getId());
            logger.info("   ✅ User loaded: {}\n", retrievedUser2);
            
            // Read using HQL (Hibernate Query Language)
            logger.info("3. Reading users using HQL...");
            List<User> allUsers = session.createQuery("FROM User", User.class).list();
            logger.info("   ✅ Found {} users:", allUsers.size());
            allUsers.forEach(user -> logger.info("      - {}", user));
            logger.info("");
            
            // Read with WHERE clause
            logger.info("4. Reading users with age > 25...");
            List<User> olderUsers = session.createQuery(
                    "FROM User WHERE age > :age", User.class)
                    .setParameter("age", 25)
                    .list();
            logger.info("   ✅ Found {} users older than 25\n", olderUsers.size());
            
            session.getTransaction().commit();
            
            // ============================================
            // UPDATE OPERATIONS
            // ============================================
            logger.info("✏️  === UPDATE OPERATIONS ===\n");
            
            session.beginTransaction();
            
            // Update using update()
            logger.info("1. Updating user using update()...");
            retrievedUser1.setAge(31);
            retrievedUser1.setUpdatedAt(new Date());
            session.update(retrievedUser1);
            logger.info("   ✅ User updated: {}\n", retrievedUser1);
            
            // Update using merge() - handles detached entities
            logger.info("2. Updating user using merge()...");
            User detachedUser = new User();
            detachedUser.setId(user2.getId());
            detachedUser.setUsername("jane_smith_updated");
            detachedUser.setEmail("jane.smith.updated@example.com");
            User mergedUser = (User) session.merge(detachedUser);
            logger.info("   ✅ User merged: {}\n", mergedUser);
            
            session.getTransaction().commit();
            
            // ============================================
            // DELETE OPERATIONS
            // ============================================
            logger.info("🗑️  === DELETE OPERATIONS ===\n");
            
            session.beginTransaction();
            
            // Delete using delete()
            logger.info("1. Deleting user using delete()...");
            User userToDelete = session.get(User.class, userId);
            if (userToDelete != null) {
                session.delete(userToDelete);
                logger.info("   ✅ User deleted\n");
            }
            
            // Delete using HQL
            logger.info("2. Deleting users with age < 30 using HQL...");
            int deletedCount = session.createQuery("DELETE FROM User WHERE age < :age")
                    .setParameter("age", 30)
                    .executeUpdate();
            logger.info("   ✅ {} users deleted\n", deletedCount);
            
            session.getTransaction().commit();
            
            logger.info("✅ CRUD operations completed successfully!");
            
        } catch (Exception e) {
            logger.error("❌ Error occurred: {}", e.getMessage(), e);
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
}

