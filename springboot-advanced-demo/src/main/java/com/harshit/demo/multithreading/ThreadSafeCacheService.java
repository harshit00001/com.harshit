package com.harshit.demo.multithreading;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Demonstrates thread-safe caching patterns
 * Shows cache-aside pattern with proper locking
 */
@Service
public class ThreadSafeCacheService {
    
    // ✅ SOLUTION: Use ConcurrentHashMap for thread-safe cache
    private final Map<String, String> cache = new ConcurrentHashMap<>();
    
    // Alternative: Read-Write Lock for more control
    private final Map<String, String> cacheWithLock = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    
    /**
     * Simple thread-safe cache using ConcurrentHashMap
     */
    public String getCachedValue(String key) {
        return cache.get(key);
    }
    
    public void putCachedValue(String key, String value) {
        cache.put(key, value);
    }
    
    /**
     * Cache with computeIfAbsent - atomic operation
     */
    public String getOrCompute(String key, java.util.function.Function<String, String> computeFunction) {
        return cache.computeIfAbsent(key, computeFunction);
    }
    
    /**
     * Cache-aside pattern with read-write lock
     * Allows multiple concurrent readers, exclusive writer
     */
    public String getValueWithLock(String key, java.util.function.Function<String, String> loadFunction) {
        // Try read lock first
        cacheLock.readLock().lock();
        try {
            String value = cacheWithLock.get(key);
            if (value != null) {
                return value;
            }
        } finally {
            cacheLock.readLock().unlock();
        }
        
        // Upgrade to write lock
        cacheLock.writeLock().lock();
        try {
            // Double-check (another thread might have loaded it)
            String value = cacheWithLock.get(key);
            if (value != null) {
                return value;
            }
            
            // Load from source
            value = loadFunction.apply(key);
            cacheWithLock.put(key, value);
            return value;
        } finally {
            cacheLock.writeLock().unlock();
        }
    }
    
    /**
     * Invalidate cache entry
     */
    public void invalidate(String key) {
        cache.remove(key);
        cacheWithLock.remove(key);
    }
    
    /**
     * Clear entire cache
     */
    public void clear() {
        cache.clear();
        cacheWithLock.clear();
    }
    
    /**
     * Get cache statistics
     */
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("cacheSize", cache.size());
        stats.put("cacheWithLockSize", cacheWithLock.size());
        return stats;
    }
}

