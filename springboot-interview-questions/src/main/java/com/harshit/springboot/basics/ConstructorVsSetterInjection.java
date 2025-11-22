package com.harshit.springboot.basics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CONSTRUCTOR INJECTION vs SETTER INJECTION - Detailed Comparison
 * 
 * This is one of the most frequently asked questions in Spring Boot interviews.
 * Understanding the differences between constructor injection and setter injection,
 * and knowing when to use each, demonstrates a deep understanding of Spring's
 * dependency injection mechanism and best practices.
 */
@Component
public class ConstructorVsSetterInjection {
    
    /**
     * CONSTRUCTOR INJECTION EXAMPLE
     * 
     * Constructor injection is when dependencies are provided through the constructor
     * when the object is created. This approach has several important characteristics
     * that make it the preferred choice for mandatory dependencies.
     * 
     * When you use constructor injection, Spring ensures that all dependencies
     * specified in the constructor are available before creating the bean. If any
     * dependency is missing, Spring will fail to start the application, which helps
     * catch configuration errors early. This is different from setter injection,
     * where an object can be created even if some dependencies are not set.
     * 
     * Another important advantage of constructor injection is that it allows you to
     * make dependencies final, which creates immutable objects. Immutable objects
     * are inherently thread-safe because their state cannot be changed after creation.
     * This is particularly important in multi-threaded environments where you want
     * to avoid race conditions and ensure data consistency.
     */
    private final OrderService orderService;  // Can be final with constructor injection
    private final PaymentService paymentService;  // Can be final with constructor injection
    
    // Constructor injection - recommended for mandatory dependencies
    public ConstructorVsSetterInjection(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        // Interview Point: All dependencies must be available at construction time
        // If any dependency is missing, Spring fails to start (fail-fast)
    }
    
    /**
     * SETTER INJECTION EXAMPLE
     * 
     * Setter injection is when dependencies are provided through setter methods after
     * the object is created. This approach is useful for optional dependencies or when
     * you need the flexibility to change dependencies after object creation.
     * 
     * One key difference is that with setter injection, the object can be created
     * even if the dependencies are not set. This means you need to handle null checks
     * or ensure that setters are called before using the dependencies. This flexibility
     * can be useful in certain scenarios, but it also introduces the possibility of
     * runtime errors if dependencies are not properly set.
     * 
     * Setter injection is also useful for resolving circular dependencies. When two
     * classes depend on each other, constructor injection can cause issues because
     * each class needs the other to be constructed first. With setter injection,
     * you can use @Lazy annotation to break the circular dependency by deferring
     * the injection until the dependency is actually needed.
     */
    private EmailNotificationService emailNotificationService;  // Cannot be final with setter injection
    
    // Setter injection - for optional dependencies
    @Autowired(required = false)  // Makes injection optional
    public void setEmailNotificationService(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
        // Interview Point: Object can exist without this dependency
        // Must check for null before using
    }
    
    /**
     * USAGE EXAMPLE
     * 
     * This method demonstrates how to use the injected dependencies. Notice that
     * constructor-injected dependencies are guaranteed to be available, while
     * setter-injected dependencies might be null and need null checks.
     */
    public void processOrder(Long orderId) {
        // Constructor-injected dependencies are always available
        String order = orderService.getOrder(orderId);
        paymentService.processPayment(orderId);
        
        // Setter-injected dependency might be null - need to check
        if (emailNotificationService != null) {
            emailNotificationService.sendConfirmation(orderId);
        }
    }
}

/**
 * Service classes for demonstration
 */
@Component
class OrderService {
    public String getOrder(Long id) {
        return "Order " + id;
    }
}

@Component
class PaymentService {
    public void processPayment(Long orderId) {
        System.out.println("Processing payment for order: " + orderId);
    }
}

@Component
class EmailNotificationService {
    public void sendConfirmation(Long orderId) {
        System.out.println("Sending confirmation email for order: " + orderId);
    }
}

