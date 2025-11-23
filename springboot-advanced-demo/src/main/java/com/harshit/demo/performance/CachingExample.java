package com.harshit.demo.performance;

import com.harshit.demo.entity.User;
import com.harshit.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * STRATEGY 3: CACHING - Complete Implementation
 * 
 * This class shows the actual code for caching with @Cacheable, @CachePut, and @CacheEvict
 */
@Service
public class CachingExample {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * @Cacheable - Caches the result
     * First call: Executes method and caches result
     * Subsequent calls: Returns cached value without executing method
     */
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        // This code only executes if cache miss
        System.out.println("Executing database query for user ID: " + id);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }
    
    /**
     * @CachePut - Always executes method and updates cache
     * Use when you want to refresh cache after update
     */
    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        // This always executes and updates cache
        System.out.println("Updating user in database: " + user.getId());
        User updated = userRepository.save(user);
        return updated; // Cache is updated with this value
    }
    
    /**
     * @CacheEvict - Removes entry from cache
     * Use when you delete data and want to remove from cache
     */
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        // This removes the cache entry for this ID
        System.out.println("Deleting user from database: " + id);
        userRepository.deleteById(id);
        // Cache entry for this ID is automatically removed
    }
    
    /**
     * @CacheEvict with allEntries - Clears entire cache
     * Use when you want to clear all cached users
     */
    @CacheEvict(value = "users", allEntries = true)
    public void clearAllUsersCache() {
        System.out.println("Clearing all users from cache");
        // All entries in "users" cache are removed
    }
    
    /**
     * Multiple cache names - Cache in multiple caches
     */
    @Cacheable(value = {"users", "userProfiles"}, key = "#id")
    public User getUserWithProfile(Long id) {
        System.out.println("Fetching user with profile: " + id);
        return userRepository.findById(id).orElse(null);
    }
}

