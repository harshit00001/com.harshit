package com.harshit.multidb.service;

import com.harshit.multidb.primary.entity.User;
import com.harshit.multidb.primary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * User Service - Handles operations on PRIMARY database
 * 
 * Uses @Transactional with primary transaction manager
 * All operations here go to the primary database
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Create a new user
     * Transaction is managed by primary TransactionManager
     */
    @Transactional(transactionManager = "primaryTransactionManager")
    public User createUser(String username, String email, String fullName) {
        // Check if user already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }
        
        User user = new User(username, email, fullName);
        return userRepository.save(user);
    }
    
    /**
     * Get all users
     */
    @Transactional(readOnly = true, transactionManager = "primaryTransactionManager")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Get user by ID
     */
    @Transactional(readOnly = true, transactionManager = "primaryTransactionManager")
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Get user by username
     */
    @Transactional(readOnly = true, transactionManager = "primaryTransactionManager")
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Delete user by ID
     */
    @Transactional(transactionManager = "primaryTransactionManager")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

