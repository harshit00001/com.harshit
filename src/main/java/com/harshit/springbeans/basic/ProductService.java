package com.harshit.springbeans.basic;

/**
 * ============================================
 * METHOD 2: Using @Service annotation
 * ============================================
 * 
 * @Service is a specialized @Component annotation
 * Used for: Business logic / Service layer
 * 
 * Other stereotypes:
 * - @Repository: Data access layer
 * - @Controller: Web layer (Spring MVC)
 * - @RestController: REST API layer
 * 
 * All of these are @Component with additional semantics
 */

import org.springframework.stereotype.Service;

/**
 * STEP 1: Use @Service instead of @Component
 * 
 * What happens:
 * - Same as @Component, but semantically indicates it's a service
 * - Spring treats it the same way during bean creation
 * - Useful for better code organization and clarity
 * 
 * Bean Name: "productService" (default)
 */
@Service
public class ProductService {
    
    public ProductService() {
        System.out.println("STEP 2: ProductService constructor called - Service bean created!");
    }
    
    public void addProduct(String productName) {
        System.out.println("Adding product: " + productName);
    }
}


