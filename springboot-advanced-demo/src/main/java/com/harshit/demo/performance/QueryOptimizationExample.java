package com.harshit.demo.performance;

import com.harshit.demo.entity.User;
import com.harshit.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * STRATEGY 5: DATABASE QUERY OPTIMIZATION - Complete Implementation
 * 
 * This class shows actual code for query optimization techniques
 */
@Service
public class QueryOptimizationExample {
    
    @Autowired
    private UserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * PAGINATION - Load only requested page, not all records
     * This is crucial for large datasets
     */
    @Transactional(readOnly = true)
    public Page<User> getUsersPaginated(int page, int size) {
        // Create Pageable with sorting
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        
        // Only loads requested page (e.g., page 0, size 10 = first 10 records)
        // Doesn't load all users into memory
        return userRepository.findAll(pageable);
    }
    
    /**
     * READ-ONLY TRANSACTION - Optimizes for read operations
     * Database can apply read optimizations
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsersReadOnly() {
        // readOnly = true tells database this is a read operation
        // Database can skip write locks and apply read optimizations
        return userRepository.findAll();
    }
    
    /**
     * CUSTOM JPQL QUERY - More efficient than multiple queries
     * Single query instead of N+1 queries
     */
    @Transactional(readOnly = true)
    public List<User> findUsersWithCustomQuery(String emailPattern) {
        // Single optimized query
        String jpql = "SELECT u FROM User u WHERE u.email LIKE :pattern";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("pattern", "%" + emailPattern + "%");
        return query.getResultList();
    }
    
    /**
     * NAMED QUERY - Pre-compiled for better performance
     * Defined in entity class with @NamedQuery
     */
    @Transactional(readOnly = true)
    public List<User> findUsersWithNamedQuery(String name) {
        Query query = entityManager.createNamedQuery("User.findByName");
        query.setParameter("name", name);
        return query.getResultList();
    }
    
    /**
     * BATCH OPERATIONS - Process multiple records efficiently
     */
    @Transactional
    public void updateUsersInBatch(List<User> users) {
        int batchSize = 20;
        for (int i = 0; i < users.size(); i++) {
            entityManager.persist(users.get(i));
            
            // Flush and clear every batchSize items
            if (i % batchSize == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
    
    /**
     * PROJECTION - Fetch only needed fields, not entire entity
     * Reduces memory usage and network transfer
     */
    @Transactional(readOnly = true)
    public List<Object[]> getUserNamesOnly() {
        // Only fetch name field, not entire User entity
        String jpql = "SELECT u.id, u.name FROM User u";
        Query query = entityManager.createQuery(jpql);
        return query.getResultList();
    }
    
    /**
     * JOIN FETCH - Avoid N+1 problem by fetching related data in one query
     * Use when you know you'll need related entities
     */
    @Transactional(readOnly = true)
    public List<User> findUsersWithOrders() {
        // If User had orders relationship, this would fetch both in one query
        String jpql = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders";
        Query query = entityManager.createQuery(jpql);
        return query.getResultList();
    }
}

