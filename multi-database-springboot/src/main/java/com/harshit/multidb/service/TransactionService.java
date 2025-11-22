package com.harshit.multidb.service;

import com.harshit.multidb.primary.entity.User;
import com.harshit.multidb.secondary.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Transaction Service - Handles operations across BOTH databases
 * 
 * This service demonstrates how to work with multiple databases
 * in a single transaction or across transactions.
 * 
 * Important Notes:
 * - Each database has its own transaction manager
 * - Cross-database transactions are NOT supported by default
 * - Use distributed transaction manager (JTA) for true ACID across DBs
 * - Or handle rollback manually if one operation fails
 */
@Service
public class TransactionService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;
    
    /**
     * Create user and order in separate transactions
     * 
     * Note: These are separate transactions, not a single distributed transaction.
     * If order creation fails, user will still be created.
     * 
     * For true distributed transactions, use JTA (Java Transaction API).
     */
    public void createUserAndOrder(String username, String email, String fullName,
                                   String productName, BigDecimal amount, Integer quantity) {
        // Create user in primary database
        User user = userService.createUser(username, email, fullName);
        
        // Create order in secondary database
        // If this fails, user is already created (separate transactions)
        Order order = orderService.createOrder(user.getId(), productName, amount, quantity);
        
        System.out.println("User created: " + user);
        System.out.println("Order created: " + order);
    }
    
    /**
     * Create user and order with manual rollback handling
     * 
     * This method attempts to create both, and if order creation fails,
     * it manually deletes the user to maintain consistency.
     */
    public void createUserAndOrderWithRollback(String username, String email, String fullName,
                                              String productName, BigDecimal amount, Integer quantity) {
        User user = null;
        try {
            // Create user in primary database
            user = userService.createUser(username, email, fullName);
            
            // Create order in secondary database
            Order order = orderService.createOrder(user.getId(), productName, amount, quantity);
            
            System.out.println("User and Order created successfully");
        } catch (Exception e) {
            // If order creation fails, rollback user creation
            if (user != null) {
                userService.deleteUser(user.getId());
                System.out.println("Rolled back user creation due to order creation failure");
            }
            throw new RuntimeException("Failed to create user and order: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get user with their orders
     * 
     * This demonstrates reading from both databases
     */
    public UserOrderInfo getUserWithOrders(Long userId) {
        // Get user from primary database
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        // Get orders from secondary database
        var orders = orderService.getOrdersByUserId(userId);
        
        return new UserOrderInfo(user, orders);
    }
    
    /**
     * Inner class to hold user and orders together
     */
    public static class UserOrderInfo {
        private User user;
        private java.util.List<Order> orders;
        
        public UserOrderInfo(User user, java.util.List<Order> orders) {
            this.user = user;
            this.orders = orders;
        }
        
        public User getUser() {
            return user;
        }
        
        public java.util.List<Order> getOrders() {
            return orders;
        }
    }
}

