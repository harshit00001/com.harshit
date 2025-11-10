package com.harshit.springbeans.basic;

/**
 * ============================================
 * BEAN CREATION METHODS - FOR INTERVIEW
 * ============================================
 * 
 * There are multiple ways to create beans in Spring:
 * 
 * 1. Using @Component (and its stereotypes)
 * 2. Using @Bean method in @Configuration class
 * 3. Using @Service, @Repository, @Controller
 * 4. Using XML configuration (legacy)
 * 
 * This file demonstrates METHOD 1: Using @Component
 */

import org.springframework.stereotype.Component;

/**
 * STEP 1: Mark a class with @Component annotation
 * 
 * What happens:
 * - Spring scans this class during component scanning
 * - Spring creates an instance (bean) of this class
 * - Spring stores it in the ApplicationContext (IoC container)
 * - Bean name defaults to class name with first letter lowercase
 * 
 * Bean Name: "userService" (default)
 */
@Component
public class UserService {
    
    private String serviceName;
    
    /**
     * STEP 2: Spring creates bean using default constructor
     * 
     * When Spring creates this bean:
     * 1. It calls this constructor
     * 2. Creates an instance
     * 3. Stores it in the container
     * 
     * This is called "Bean Instantiation"
     */
    public UserService() {
        this.serviceName = "UserService";
        System.out.println("STEP 2: UserService constructor called - Bean being created!");
    }
    
    public void processUser(String userName) {
        System.out.println("Processing user: " + userName);
    }
    
    public String getServiceName() {
        return serviceName;
    }
}


