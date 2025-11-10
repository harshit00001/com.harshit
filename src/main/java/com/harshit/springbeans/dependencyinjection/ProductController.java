package com.harshit.springbeans.dependencyinjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ============================================
 * METHOD 3: FIELD INJECTION (NOT RECOMMENDED)
 * ============================================
 * 
 * Avoid in production code!
 * 
 * Problems:
 * - Hard to test (need reflection or Spring context)
 * - Dependencies not visible in constructor
 * - Can't make fields final
 * 
 * Sometimes used in:
 * - Legacy code
 * - Test classes
 */
@Component
public class ProductController {
    
    /**
     * Field injection
     * 
     * Spring automatically:
     * 1. Finds EmailService bean
     * 2. Uses reflection to set this field
     * 3. Injects the bean
     * 
     * @Autowired is required for field injection
     */
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private NotificationService notificationService;
    
    public ProductController() {
        System.out.println("FIELD INJECTION: ProductController created");
        System.out.println("  -> Fields will be injected after construction");
    }
    
    public void addProduct(String productName) {
        emailService.sendEmail("admin@example.com", "Product added: " + productName);
    }
}


