package com.harshit.springbeans.dependencyinjection;

import org.springframework.stereotype.Component;

/**
 * ============================================
 * METHOD 1: CONSTRUCTOR INJECTION
 * ============================================
 * 
 * BEST PRACTICE - Most recommended way!
 * 
 * Benefits:
 * - Dependencies are immutable (final)
 * - All dependencies required at construction time
 * - Easy to test (can mock in constructor)
 * - No need for @Autowired (Spring 4.3+)
 */
@Component
public class UserController {
    
    // Dependencies as final fields
    private final EmailService emailService;
    private final NotificationService notificationService;
    
    /**
     * Constructor with dependencies
     * 
     * Spring automatically:
     * 1. Finds EmailService bean
     * 2. Finds NotificationService bean
     * 3. Calls this constructor with those beans
     * 
     * @Autowired is optional for constructor injection (Spring 4.3+)
     */
    // @Autowired // Optional in Spring 4.3+
    public UserController(EmailService emailService, 
                         NotificationService notificationService) {
        System.out.println("========================================");
        System.out.println("CONSTRUCTOR INJECTION EXAMPLE");
        System.out.println("  -> UserController constructor called");
        System.out.println("  -> EmailService injected via constructor");
        System.out.println("  -> NotificationService injected via constructor");
        System.out.println("========================================");
        
        this.emailService = emailService;
        this.notificationService = notificationService;
    }
    
    public void registerUser(String userName) {
        emailService.sendEmail(userName, "Welcome!");
        notificationService.sendNotification("User registered: " + userName);
    }
}


