package com.harshit.springbeans.configuration;

/**
 * ============================================
 * METHOD 4: Using @Configuration and @Bean
 * ============================================
 * 
 * This is the MOST IMPORTANT method for interviews!
 * 
 * @Configuration class:
 * - Contains @Bean methods
 * - Spring processes this class
 * - Each @Bean method creates a bean
 * 
 * When to use @Bean instead of @Component:
 * 1. Third-party classes (you can't add @Component to them)
 * 2. Complex initialization logic
 * 3. Conditional bean creation
 * 4. Multiple beans of same type with different configurations
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * STEP 1: Mark class with @Configuration
 * 
 * What happens:
 * - Spring identifies this as a configuration class
 * - Spring processes all @Bean methods inside
 * - Each @Bean method is executed ONCE to create a bean
 * - Result is stored in ApplicationContext
 */
@Configuration
public class AppConfig {
    
    /**
     * STEP 2: Define a @Bean method
     * 
     * Method name = Bean name (default)
     * Return type = Bean type
     * Method body = Bean creation logic
     * 
     * Bean Creation Process:
     * 1. Spring calls this method
     * 2. Method executes and returns an object
     * 3. Object is registered as a bean
     * 4. Bean name: "databaseConnection"
     * 5. Bean type: DatabaseConnection
     * 
     * This is called "Bean Factory Method"
     */
    @Bean
    public DatabaseConnection databaseConnection() {
        System.out.println("STEP 2: @Bean method called - Creating DatabaseConnection bean");
        
        // STEP 3: Create and configure the object
        DatabaseConnection connection = new DatabaseConnection();
        connection.setUrl("jdbc:mysql://localhost:3306/mydb");
        connection.setUsername("admin");
        connection.setPassword("password");
        
        System.out.println("STEP 3: DatabaseConnection object created and configured");
        
        // STEP 4: Return the object (Spring stores it as bean)
        return connection;
    }
    
    /**
     * STEP 5: Custom bean name
     * 
     * You can specify bean name using @Bean("customName")
     * 
     * Bean name: "paymentService"
     * Bean type: PaymentService
     */
    @Bean("paymentService")
    public PaymentService paymentService() {
        System.out.println("STEP 5: Creating PaymentService bean with custom name");
        return new PaymentService();
    }
    
    /**
     * STEP 6: Bean with dependencies
     * 
     * Spring automatically injects dependencies!
     * When Spring calls this method, it sees the parameter
     * and automatically provides the databaseConnection bean
     * 
     * This is called "Dependency Injection via Method Parameter"
     */
    @Bean
    public OrderService orderService(DatabaseConnection databaseConnection) {
        System.out.println("STEP 6: Creating OrderService with DatabaseConnection dependency");
        OrderService service = new OrderService();
        service.setDatabaseConnection(databaseConnection);
        return service;
    }
}

/**
 * ============================================
 * SUPPORTING CLASSES FOR EXAMPLES
 * ============================================
 */

class DatabaseConnection {
    private String url;
    private String username;
    private String password;
    
    public DatabaseConnection() {
        System.out.println("  -> DatabaseConnection constructor called");
    }
    
    public void setUrl(String url) { this.url = url; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    
    public void connect() {
        System.out.println("Connecting to: " + url);
    }
}

class PaymentService {
    public PaymentService() {
        System.out.println("  -> PaymentService constructor called");
    }
    
    public void processPayment(double amount) {
        System.out.println("Processing payment: $" + amount);
    }
}

class OrderService {
    private DatabaseConnection databaseConnection;
    
    public void setDatabaseConnection(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        System.out.println("  -> DatabaseConnection injected into OrderService");
    }
    
    public void processOrder(String orderId) {
        databaseConnection.connect();
        System.out.println("Processing order: " + orderId);
    }
}


