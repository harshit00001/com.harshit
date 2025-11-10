package com.harshit.springbeans.dependencyinjection;

/**
 * ============================================
 * DEPENDENCY INJECTION IN BEANS
 * ============================================
 * 
 * Dependency Injection (DI) is when Spring automatically
 * provides dependencies to beans.
 * 
 * TYPES OF DEPENDENCY INJECTION:
 * 
 * 1. CONSTRUCTOR INJECTION (Recommended)
 *    - Dependencies provided via constructor
 *    - Immutable dependencies
 *    - Ensures all dependencies are available
 * 
 * 2. SETTER INJECTION
 *    - Dependencies provided via setter methods
 *    - Mutable dependencies
 *    - Optional dependencies
 * 
 * 3. FIELD INJECTION (Not recommended)
 *    - Dependencies injected directly into fields
 *    - Uses reflection
 *    - Hard to test
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * ============================================
 * SERVICE LAYER (DEPENDENCY)
 * ============================================
 */
@Service
class EmailService {
    public void sendEmail(String to, String message) {
        System.out.println("Sending email to " + to + ": " + message);
    }
}

@Service
class NotificationService {
    public void sendNotification(String message) {
        System.out.println("Notification: " + message);
    }
}

/**
 * ============================================
 * DEPENDENCY INJECTION EXAMPLES
 * ============================================
 * 
 * See separate files:
 * - UserController.java - Constructor Injection (Recommended)
 * - OrderController.java - Setter Injection
 * - ProductController.java - Field Injection (Not Recommended)
 * 
 * ============================================
 * OPTIONAL DEPENDENCIES
 * ============================================
 * 
 * Use @Autowired(required = false) for optional dependencies
 */
@Component
class OptionalDependencyExample {
    
    /**
     * This dependency is optional
     * If bean doesn't exist, Spring won't throw error
     * Field will be null if bean not found
     */
    @Autowired(required = false)
    private EmailService emailService;
    
    public void doSomething() {
        if (emailService != null) {
            emailService.sendEmail("test@example.com", "Hello");
        } else {
            System.out.println("EmailService not available");
        }
    }
}

