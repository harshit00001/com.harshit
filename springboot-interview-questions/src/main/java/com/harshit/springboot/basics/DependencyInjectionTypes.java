package com.harshit.springboot.basics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DEPENDENCY INJECTION TYPES - Complete Demonstration
 * 
 * Dependency Injection is a design pattern used to manage dependencies between objects.
 * Instead of objects creating their own dependencies, dependencies are provided
 * (injected) from outside. This makes code more modular, testable, and maintainable.
 * 
 * Spring Framework provides four main types of dependency injection:
 * 1. Constructor Injection
 * 2. Setter Injection
 * 3. Field Injection
 * 4. Method Injection (less common)
 * 
 * Each type has its own use cases, advantages, and disadvantages. Understanding
 * when to use each type is crucial for writing clean, maintainable Spring applications.
 */
@Component
public class DependencyInjectionTypes {
    
    // This class demonstrates all types of dependency injection
    // Each example shows a different way to inject the same dependency
    
    /**
     * TYPE 1: CONSTRUCTOR INJECTION
     * 
     * Constructor injection is when dependencies are provided through the constructor
     * when the object is created. This is the recommended approach for mandatory
     * dependencies because it ensures that the object cannot be created without its
     * required dependencies. It also makes dependencies explicit and immutable.
     * 
     * Advantages:
     * - Ensures all required dependencies are provided at object creation
     * - Creates immutable objects (dependencies can be final)
     * - Makes dependencies explicit and clear
     * - Better for thread safety (immutable objects)
     * - Easier to test (can pass mocks in constructor)
     * 
     * Disadvantages:
     * - Can cause circular dependency issues
     * - All dependencies must be available at construction time
     */
    private final UserService userService;
    
    // Constructor injection - recommended approach
    public DependencyInjectionTypes(UserService userService) {
        this.userService = userService;  // Dependencies are injected via constructor
    }
    
    /**
     * TYPE 2: SETTER INJECTION
     * 
     * Setter injection is when dependencies are provided through setter methods after
     * the object is created. This is useful for optional dependencies or when you need
     * the flexibility to change dependencies after object creation.
     * 
     * Advantages:
     * - Allows optional dependencies
     * - Can change dependencies after object creation
     * - Can help resolve circular dependencies with @Lazy
     * - More flexible for testing (can set different dependencies)
     * 
     * Disadvantages:
     * - Dependencies might not be set (object can exist without them)
     * - Objects are mutable (not thread-safe by default)
     * - Less explicit (dependencies not visible in constructor)
     */
    private EmailService emailService;
    
    // Setter injection - for optional dependencies
    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;  // Dependency injected via setter method
    }
    
    /**
     * TYPE 3: FIELD INJECTION
     * 
     * Field injection is when dependencies are injected directly into fields using
     * the @Autowired annotation. This is the simplest approach but is generally not
     * recommended for production code because it makes testing harder and hides
     * dependencies.
     * 
     * Advantages:
     * - Very simple and concise code
     * - No need for constructors or setters
     * - Quick to write
     * 
     * Disadvantages:
     * - Hard to test (cannot easily inject mocks)
     * - Dependencies are hidden (not visible in constructor)
     * - Cannot make fields final (not immutable)
     * - Uses reflection (slightly slower)
     * - Not recommended by Spring team
     */
    @Autowired
    private NotificationService notificationService;  // Field injection - not recommended
    
    /**
     * TYPE 4: METHOD INJECTION (Less Common)
     * 
     * Method injection is when dependencies are injected through any method annotated
     * with @Autowired. This is less common but can be useful for specific scenarios
     * like getting a new instance of a prototype bean each time.
     */
    private LoggerService loggerService;
    
    @Autowired
    public void injectLoggerService(LoggerService loggerService) {
        this.loggerService = loggerService;  // Method injection
    }
    
    /**
     * DEMONSTRATION METHOD
     * 
     * This method shows how all injected dependencies can be used. In a real application,
     * you would use these services to perform business operations. The key point is that
     * all these dependencies were provided by Spring, not created by this class itself.
     */
    public void demonstrateInjection() {
        System.out.println("=== DEPENDENCY INJECTION DEMONSTRATION ===");
        System.out.println("UserService injected via constructor: " + userService);
        System.out.println("EmailService injected via setter: " + emailService);
        System.out.println("NotificationService injected via field: " + notificationService);
        System.out.println("LoggerService injected via method: " + loggerService);
    }
}

/**
 * Example service classes for demonstration
 */
@Component
class UserService {
    public String getUserName(Long id) {
        return "User " + id;
    }
}

@Component
class EmailService {
    public void sendEmail(String to, String message) {
        System.out.println("Sending email to " + to + ": " + message);
    }
}

@Component
class NotificationService {
    public void sendNotification(String message) {
        System.out.println("Notification: " + message);
    }
}

@Component
class LoggerService {
    public void log(String message) {
        System.out.println("Log: " + message);
    }
}

