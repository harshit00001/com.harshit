package com.harshit.demo.performance;

import com.harshit.demo.entity.User;
import com.harshit.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * STRATEGY 2: Lazy Loading - Complete Implementation
 * 
 * This class shows actual code demonstrating lazy loading
 * and how to avoid N+1 query problems
 */
@Component
public class LazyLoadingDemo {
    
    @Autowired
    private UserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * LAZY LOADING EXAMPLE
     * User.orders is FetchType.LAZY
     * Orders are only loaded when accessed
     */
    @Transactional(readOnly = true)
    public void demonstrateLazyLoading() {
        // Load user - only user query executed
        User user = userRepository.findById(1L).orElse(null);
        
        // At this point, orders are NOT loaded yet
        // Only when you access user.getOrders(), the query executes
        
        if (user != null) {
            // This triggers lazy loading - orders query executes here
            // If orders were EAGER, query would execute when loading user
            var orders = user.getOrders();
            System.out.println("User: " + user.getName() + ", Orders: " + orders.size());
        }
    }
    
    /**
     * N+1 PROBLEM EXAMPLE (with EAGER loading)
     * If orders were EAGER, this would execute:
     * - 1 query for users
     * - N queries for orders (one per user)
     * Total: 1 + N queries
     */
    @Transactional(readOnly = true)
    public void demonstrateNPlusOneProblem() {
        // Load all users
        var users = userRepository.findAll();
        
        // With EAGER: Each user.getOrders() would trigger a query
        // With LAZY: Orders loaded only when accessed
        for (User user : users) {
            // If EAGER: Query executes here for each user
            // If LAZY: Query executes only if orders are accessed
            var orders = user.getOrders();
            System.out.println("User: " + user.getName() + " has " + orders.size() + " orders");
        }
    }
    
    /**
     * SOLUTION: Use JOIN FETCH when you need related data
     * This loads user and orders in a single query
     */
    @Transactional(readOnly = true)
    public void loadUserWithOrdersEfficiently() {
        // Single query with JOIN FETCH
        String jpql = "SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :id";
        User user = entityManager.createQuery(jpql, User.class)
                .setParameter("id", 1L)
                .getSingleResult();
        
        // Orders are already loaded, no additional query
        var orders = user.getOrders();
        System.out.println("User: " + user.getName() + " has " + orders.size() + " orders");
    }
    
    /**
     * Entity configuration in User.java:
     * 
     * @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
     * private List<Order> orders;
     * 
     * This ensures orders are loaded only when accessed
     */
}

