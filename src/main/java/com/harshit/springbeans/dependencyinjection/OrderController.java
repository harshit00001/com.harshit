package com.harshit.springbeans.dependencyinjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ============================================
 * METHOD 2: SETTER INJECTION
 * ============================================
 * 
 * Use when:
 * - Dependencies are optional
 * - Dependencies might change
 * - Need to support circular dependencies
 */
@Component
public class OrderController {
    
    private EmailService emailService;
    private NotificationService notificationService;
    
    /**
     * Setter injection
     * 
     * Spring automatically:
     * 1. Finds EmailService bean
     * 2. Calls this setter method
     * 3. Injects the bean
     * 
     * @Autowired is required for setter injection
     */
    @Autowired
    public void setEmailService(EmailService emailService) {
        System.out.println("SETTER INJECTION: EmailService set");
        this.emailService = emailService;
    }
    
    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        System.out.println("SETTER INJECTION: NotificationService set");
        this.notificationService = notificationService;
    }
    
    public void processOrder(String orderId) {
        emailService.sendEmail("customer@example.com", "Order processed: " + orderId);
    }
}


