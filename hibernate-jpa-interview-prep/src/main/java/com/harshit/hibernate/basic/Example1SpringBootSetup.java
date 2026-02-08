package com.harshit.hibernate.basic;

import com.harshit.hibernate.basic.entity.User;
import com.harshit.hibernate.basic.entity.UserStatus;
import com.harshit.hibernate.basic.repository.UserRepository;
import com.harshit.hibernate.basic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Example 1: Spring Boot Setup and Basic Operations
 * 
 * ====================================================================================
 * INTERVIEW QUESTION: How does Spring Boot simplify Hibernate/JPA setup compared to
 * standalone Java applications?
 * ====================================================================================
 * 
 * DETAILED ANSWER:
 * 
 * Spring Boot dramatically simplifies Hibernate/JPA setup through auto-configuration
 * and convention over configuration. Let me explain the key differences:
 * 
 * STANDALONE JAVA APPLICATION (Traditional):
 * 
 * 1. Manual Configuration:
 *    - Create hibernate.cfg.xml or persistence.xml
 *    - Manually create EntityManagerFactory
 *    - Manually create EntityManager
 *    - Manually manage transactions
 *    - Manually close resources
 * 
 * 2. Boilerplate Code:
 *    - Lots of setup code in every class
 *    - Resource management (try-finally blocks)
 *    - Transaction management code
 * 
 * 3. Configuration Files:
 *    - hibernate.cfg.xml for Hibernate
 *    - persistence.xml for JPA
 *    - Multiple configuration files to maintain
 * 
 * SPRING BOOT APPLICATION (Modern):
 * 
 * 1. Auto-Configuration:
 *    - Spring Boot automatically configures DataSource
 *    - Auto-configures EntityManagerFactory
 *    - Auto-configures TransactionManager
 *    - Auto-configures JPA repositories
 *    - All based on classpath dependencies!
 * 
 * 2. Zero Boilerplate:
 *    - Just add @Repository, @Service, @Controller
 *    - Spring manages everything
 *    - No resource management code
 *    - @Transactional handles transactions
 * 
 * 3. Single Configuration File:
 *    - application.properties or application.yml
 *    - All configuration in one place
 *    - Environment-specific profiles
 * 
 * KEY SPRING BOOT FEATURES:
 * 
 * 1. STARTER DEPENDENCIES:
 *    - spring-boot-starter-data-jpa brings everything
 *    - Includes Hibernate, JPA, Spring Data JPA
 *    - Pre-configured and tested together
 * 
 * 2. AUTO-CONFIGURATION:
 *    - Detects H2 on classpath -> configures H2 DataSource
 *    - Detects JPA -> configures EntityManagerFactory
 *    - Detects Spring Data JPA -> configures repositories
 *    - All automatic, zero configuration needed!
 * 
 * 3. EMBEDDED SERVER:
 *    - Tomcat embedded by default
 *    - No need to deploy WAR files
 *    - Run as standalone JAR
 *    - Perfect for microservices
 * 
 * 4. PRODUCTION READY:
 *    - Actuator for monitoring
 *    - Health checks
 *    - Metrics
 *    - All built-in
 * 
 * ====================================================================================
 * WHAT THIS EXAMPLE DEMONSTRATES:
 * ====================================================================================
 * 
 * This example shows:
 * 1. How Spring Boot auto-configures everything
 * 2. How to use Spring Data JPA repositories
 * 3. How to use @Service for business logic
 * 4. How @Transactional works
 * 5. How dependency injection works
 * 
 * ====================================================================================
 * COMMANDLINERUNNER EXPLANATION:
 * ====================================================================================
 * 
 * CommandLineRunner is a Spring Boot interface that allows code to run after
 * application context is fully loaded. It's perfect for:
 * - Running initialization code
 * - Seeding database
 * - Running examples/demos
 * - One-time setup tasks
 * 
 * The run() method is called automatically after Spring Boot starts.
 */
@Component
public class Example1SpringBootSetup implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(Example1SpringBootSetup.class);
    
    /**
     * User Repository - Injected by Spring
     * 
     * Spring automatically:
     * 1. Detects UserRepository interface
     * 2. Creates proxy implementation
     * 3. Injects it here
     * 
     * No manual EntityManagerFactory or EntityManager needed!
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * User Service - Injected by Spring
     * 
     * Spring automatically:
     * 1. Creates UserService bean
     * 2. Injects UserRepository into it
     * 3. Injects UserService here
     * 
     * All dependency injection is automatic!
     */
    @Autowired
    private UserService userService;
    
    /**
     * This method runs automatically after Spring Boot application starts
     * 
     * It demonstrates:
     * - Spring Boot auto-configuration
     * - Spring Data JPA repositories
     * - Service layer usage
     * - Transaction management
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("\n" + "=".repeat(80));
        logger.info("🔄 === SPRING BOOT HIBERNATE/JPA SETUP EXAMPLE ===\n");
        
        // ========================================================================
        // DEMONSTRATION 1: Spring Boot Auto-Configuration
        // ========================================================================
        logger.info("📝 === DEMONSTRATION 1: SPRING BOOT AUTO-CONFIGURATION ===\n");
        logger.info("What Spring Boot configured automatically:");
        logger.info("✅ DataSource - H2 in-memory database");
        logger.info("✅ EntityManagerFactory - JPA entity manager factory");
        logger.info("✅ TransactionManager - Transaction management");
        logger.info("✅ JPA Repositories - Spring Data JPA repositories");
        logger.info("✅ All without any manual configuration!\n");
        
        // ========================================================================
        // DEMONSTRATION 2: Using Spring Data JPA Repository
        // ========================================================================
        logger.info("📝 === DEMONSTRATION 2: SPRING DATA JPA REPOSITORY ===\n");
        
        logger.info("1. Creating users using repository...");
        
        // Create users directly using repository
        User user1 = new User("john_doe", "john.doe@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setAge(30);
        user1 = userRepository.save(user1);
        logger.info("   ✅ User 1 created: {} (ID: {})\n", user1.getUsername(), user1.getId());
        
        User user2 = new User("jane_smith", "jane.smith@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setAge(25);
        user2 = userRepository.save(user2);
        logger.info("   ✅ User 2 created: {} (ID: {})\n", user2.getUsername(), user2.getId());
        
        // ========================================================================
        // DEMONSTRATION 3: Repository Query Methods
        // ========================================================================
        logger.info("📝 === DEMONSTRATION 3: REPOSITORY QUERY METHODS ===\n");
        
        logger.info("2. Finding user by username...");
        userRepository.findByUsername("john_doe")
                .ifPresent(user -> logger.info("   ✅ Found user: {}\n", user));
        
        logger.info("3. Finding users older than 25...");
        List<User> olderUsers = userRepository.findByAgeGreaterThan(25);
        logger.info("   ✅ Found {} users older than 25\n", olderUsers.size());
        
        logger.info("4. Counting active users...");
        long activeCount = userRepository.countByStatus(UserStatus.ACTIVE);
        logger.info("   ✅ Active users count: {}\n", activeCount);
        
        // ========================================================================
        // DEMONSTRATION 4: Using Service Layer
        // ========================================================================
        logger.info("📝 === DEMONSTRATION 4: SERVICE LAYER ===\n");
        
        logger.info("5. Creating user using service...");
        User user3 = userService.createUser(
                "bob_johnson",
                "bob.johnson@example.com",
                "Bob",
                "Johnson",
                35
        );
        logger.info("   ✅ User created via service: {} (ID: {})\n", user3.getUsername(), user3.getId());
        
        logger.info("6. Finding all users using service...");
        List<User> allUsers = userService.findAllUsers();
        logger.info("   ✅ Total users: {}\n", allUsers.size());
        
        logger.info("7. Updating user using service...");
        User updatedUser = userService.updateUser(user1.getId(), "John Updated", "Doe Updated", 31);
        logger.info("   ✅ User updated: {}\n", updatedUser);
        
        // ========================================================================
        // DEMONSTRATION 5: Transaction Management
        // ========================================================================
        logger.info("📝 === DEMONSTRATION 5: TRANSACTION MANAGEMENT ===\n");
        logger.info("8. All operations above ran in transactions automatically!");
        logger.info("   - @Transactional on service methods ensures atomicity");
        logger.info("   - If any operation fails, entire transaction rolls back");
        logger.info("   - No manual transaction.begin() or commit() needed!\n");
        
        // ========================================================================
        // SUMMARY
        // ========================================================================
        logger.info("📝 === SUMMARY: SPRING BOOT BENEFITS ===\n");
        logger.info("✅ No hibernate.cfg.xml or persistence.xml needed");
        logger.info("✅ No manual EntityManagerFactory creation");
        logger.info("✅ No manual transaction management");
        logger.info("✅ No resource cleanup code");
        logger.info("✅ Just @Repository, @Service, @Transactional");
        logger.info("✅ All configuration in application.properties");
        logger.info("✅ Embedded server - run as standalone JAR");
        logger.info("✅ Production-ready features built-in\n");
        
        logger.info("=".repeat(80));
        logger.info("✅ Spring Boot setup example completed successfully!");
        logger.info("=".repeat(80) + "\n");
        
        logger.info("🌐 REST API is available at:");
        logger.info("   GET    http://localhost:8080/api/users");
        logger.info("   POST   http://localhost:8080/api/users");
        logger.info("   GET    http://localhost:8080/api/users/{id}");
        logger.info("   PUT    http://localhost:8080/api/users/{id}");
        logger.info("   DELETE http://localhost:8080/api/users/{id}\n");
        
        logger.info("💾 H2 Console available at:");
        logger.info("   http://localhost:8080/h2-console");
        logger.info("   JDBC URL: jdbc:h2:mem:testdb");
        logger.info("   Username: sa");
        logger.info("   Password: (empty)\n");
    }
}



