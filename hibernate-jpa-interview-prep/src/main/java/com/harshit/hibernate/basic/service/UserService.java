package com.harshit.hibernate.basic.service;

import com.harshit.hibernate.basic.entity.User;
import com.harshit.hibernate.basic.entity.UserStatus;
import com.harshit.hibernate.basic.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * User Service - Spring Boot Service Layer Example
 * 
 * ====================================================================================
 * INTERVIEW QUESTION: What is the Service layer in Spring Boot and why is it needed?
 * ====================================================================================
 * 
 * DETAILED ANSWER:
 * 
 * The Service layer is a crucial part of the layered architecture in Spring Boot
 * applications. It sits between the Controller (presentation layer) and Repository
 * (data access layer), providing business logic and transaction management.
 * 
 * LAYERED ARCHITECTURE:
 * 
 * Controller (Presentation Layer)
 *     ↓
 * Service (Business Logic Layer)  ← You are here
 *     ↓
 * Repository (Data Access Layer)
 *     ↓
 * Database
 * 
 * WHY SERVICE LAYER IS NEEDED:
 * 
 * 1. BUSINESS LOGIC SEPARATION:
 *    - Controllers should only handle HTTP requests/responses
 *    - Repositories should only handle data access
 *    - Services contain business rules and logic
 *    - Example: Validating user age, calculating discounts, sending emails
 * 
 * 2. TRANSACTION MANAGEMENT:
 *    - @Transactional annotation is typically placed on service methods
 *    - Ensures all database operations in a method are atomic
 *    - If any operation fails, entire transaction rolls back
 *    - Example: Creating user + sending welcome email (both or neither)
 * 
 * 3. REUSABILITY:
 *    - Business logic can be reused across multiple controllers
 *    - Example: UserService can be used by REST API and Web UI
 * 
 * 4. TESTABILITY:
 *    - Easy to unit test business logic without HTTP layer
 *    - Can mock repositories for testing
 *    - Example: Test user creation logic without database
 * 
 * 5. SECURITY:
 *    - Can apply security rules at service level
 *    - Example: Check if user has permission before deleting
 * 
 * ====================================================================================
 * SPRING ANNOTATIONS EXPLAINED:
 * ====================================================================================
 * 
 * @Service:
 * - Marks this class as a Spring service component
 * - Specialized form of @Component
 * - Spring automatically detects and registers it as a bean
 * - Can be injected into other components using @Autowired
 * 
 * @Autowired:
 * - Injects dependencies automatically
 * - Spring finds matching bean and injects it
 * - Can be on constructor, field, or setter method
 * - Constructor injection is recommended (better for testing, required fields)
 * 
 * @Transactional:
 * - Declares that a method should run within a transaction
 * - If method completes successfully, transaction commits
 * - If exception occurs, transaction rolls back
 * - Can be applied at class level (all methods) or method level
 * - Attributes:
 *   * propagation: How transaction propagates (REQUIRED, REQUIRES_NEW, etc.)
 *   * isolation: Transaction isolation level (READ_COMMITTED, etc.)
 *   * readOnly: Optimize for read-only operations
 *   * timeout: Transaction timeout in seconds
 *   * rollbackFor: Exceptions that trigger rollback
 * 
 * ====================================================================================
 * TRANSACTION PROPAGATION:
 * ====================================================================================
 * 
 * REQUIRED (default):
 * - If transaction exists, use it
 * - If no transaction, create new one
 * - Most common scenario
 * 
 * REQUIRES_NEW:
 * - Always create new transaction
 * - Suspend existing transaction if any
 * - Useful for operations that must commit independently
 * 
 * SUPPORTS:
 * - Use transaction if exists
 * - If no transaction, execute without transaction
 * 
 * MANDATORY:
 * - Must have existing transaction
 * - Throws exception if no transaction
 * 
 * NEVER:
 * - Must NOT have transaction
 * - Throws exception if transaction exists
 * 
 * NOT_SUPPORTED:
 * - Suspend transaction if exists
 * - Execute without transaction
 * 
 * NESTED:
 * - Create nested transaction (savepoint)
 * - Rollback nested transaction doesn't affect outer
 * 
 * ====================================================================================
 * REAL-WORLD SCENARIO:
 * ====================================================================================
 * 
 * In a user management system, this service would handle:
 * - User registration (validate, create, send welcome email)
 * - User authentication (validate credentials)
 * - User profile updates (validate, update, log changes)
 * - User deletion (soft delete, archive data)
 * - User search and filtering
 * 
 * All business logic is centralized here, making it:
 * - Easy to maintain
 * - Easy to test
 * - Easy to reuse
 * - Easy to secure
 */
@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    /**
     * User Repository - Injected by Spring
     * 
     * Constructor injection is preferred over field injection because:
     * 1. Required dependencies are explicit
     * 2. Easier to test (can pass mock repository)
     * 3. Immutable (final field)
     * 4. No reflection needed
     */
    private final UserRepository userRepository;
    
    /**
     * Constructor Injection
     * 
     * Spring automatically injects UserRepository when creating UserService bean
     * No need for @Autowired annotation on constructor (Spring 4.3+)
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.info("UserService initialized with UserRepository");
    }
    
    /**
     * Create a new user
     * 
     * This method demonstrates:
     * - Transaction management (@Transactional)
     * - Business logic (validation)
     * - Repository usage
     * - Exception handling
     * 
     * @Transactional ensures:
     * - All operations in this method are atomic
     * - If exception occurs, all changes are rolled back
     * - Changes are committed only if method completes successfully
     */
    @Transactional
    public User createUser(String username, String email, String firstName, String lastName, Integer age) {
        logger.info("Creating user with username: {}", username);
        
        // Business Logic: Validation
        // Check if username already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }
        
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username " + username + " already exists");
        }
        
        // Business Logic: Create user entity
        User user = new User(username, email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setStatus(UserStatus.ACTIVE);
        
        // Save to database (within transaction)
        User savedUser = userRepository.save(user);
        
        logger.info("User created successfully with ID: {}", savedUser.getId());
        
        // Business Logic: Could send welcome email here
        // If email sending fails, entire transaction (including user creation) rolls back
        // sendWelcomeEmail(savedUser);
        
        return savedUser;
    }
    
    /**
     * Find user by ID
     * 
     * @Transactional(readOnly = true) optimizes for read operations:
     * - No need to track changes
     * - Better performance
     * - Can be used with read replicas
     */
    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        logger.info("Finding user by ID: {}", id);
        return userRepository.findById(id);
    }
    
    /**
     * Find user by username
     * 
     * Read-only transaction for query operation
     */
    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }
    
    /**
     * Find all users
     * 
     * Returns all users in the database
     */
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        logger.info("Finding all users");
        return userRepository.findAll();
    }
    
    /**
     * Find active users
     * 
     * Demonstrates repository method with condition
     */
    @Transactional(readOnly = true)
    public List<User> findActiveUsers() {
        logger.info("Finding active users");
        return userRepository.findByStatus(UserStatus.ACTIVE);
    }
    
    /**
     * Update user
     * 
     * This method demonstrates:
     * - Finding existing entity
     * - Modifying managed entity (changes tracked automatically)
     * - Transaction commits changes automatically
     * 
     * Note: We don't need to call save() again because:
     * - Entity is managed (loaded from database)
     * - Hibernate tracks changes automatically
     * - Changes are saved on transaction commit
     */
    @Transactional
    public User updateUser(Long id, String firstName, String lastName, Integer age) {
        logger.info("Updating user with ID: {}", id);
        
        // Find user (loads into persistence context - becomes managed)
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        
        // Modify entity (Hibernate tracks changes automatically)
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        // updatedAt will be set automatically by @PreUpdate callback
        
        // No need to call save() - entity is managed, changes tracked
        // Changes will be saved when transaction commits
        
        logger.info("User updated successfully: {}", user);
        return user;
    }
    
    /**
     * Delete user (soft delete)
     * 
     * Instead of hard delete, we set status to INACTIVE
     * This preserves data for audit purposes
     */
    @Transactional
    public void deactivateUser(Long id) {
        logger.info("Deactivating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        
        // Soft delete - set status to INACTIVE
        user.setStatus(UserStatus.INACTIVE);
        
        // Changes saved automatically on commit
        logger.info("User deactivated successfully");
    }
    
    /**
     * Hard delete user
     * 
     * Permanently removes user from database
     * Use with caution - data cannot be recovered
     */
    @Transactional
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        
        userRepository.deleteById(id);
        logger.info("User deleted successfully");
    }
    
    /**
     * Get user statistics
     * 
     * Demonstrates aggregate queries
     */
    @Transactional(readOnly = true)
    public long getActiveUserCount() {
        return userRepository.countByStatus(UserStatus.ACTIVE);
    }
    
    /**
     * Find users older than specified age
     * 
     * Demonstrates custom query method
     */
    @Transactional(readOnly = true)
    public List<User> findUsersOlderThan(Integer age) {
        logger.info("Finding users older than: {}", age);
        return userRepository.findByAgeGreaterThan(age);
    }
}

