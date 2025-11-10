package com.harshit.springbeans.basic;

/**
 * ============================================
 * METHOD 3: Using @Repository annotation
 * ============================================
 * 
 * @Repository is used for Data Access Layer
 * 
 * Benefits:
 * - Automatic exception translation (SQLException -> DataAccessException)
 * - Clear indication of data access responsibility
 * - Works with @Transactional
 */

import org.springframework.stereotype.Repository;

/**
 * STEP 1: Use @Repository for data access
 * 
 * Bean Name: "orderRepository" (default)
 */
@Repository
public class OrderRepository {
    
    public OrderRepository() {
        System.out.println("STEP 2: OrderRepository constructor called - Repository bean created!");
    }
    
    public void saveOrder(String orderId) {
        System.out.println("Saving order: " + orderId);
    }
    
    public String findOrder(String orderId) {
        return "Order: " + orderId;
    }
}


