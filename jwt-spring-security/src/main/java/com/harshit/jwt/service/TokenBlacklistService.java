package com.harshit.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Token Blacklist Service - Advanced feature for token revocation
 * 
 * Why Token Blacklisting?
 * - When user logs out, we want to invalidate their token
 * - JWT tokens are stateless, so we can't delete them
 * - Solution: Store blacklisted tokens in Redis (fast, expires automatically)
 * 
 * How it works:
 * 1. When user logs out, add token to blacklist
 * 2. Before validating token, check if it's blacklisted
 * 3. Blacklisted tokens are rejected
 * 
 * Alternative approaches:
 * - Store tokens in database (slower)
 * - Use short token expiration (less secure)
 * - Use refresh token rotation (more complex)
 */
@Service
public class TokenBlacklistService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    private static final String BLACKLIST_PREFIX = "blacklist:";
    
    /**
     * Add token to blacklist
     * 
     * We store token with expiration time equal to token's remaining validity
     * This way, Redis automatically removes expired entries
     */
    public void blacklistToken(String token, long expirationTimeInMillis) {
        String key = BLACKLIST_PREFIX + token;
        // Store token until it naturally expires
        redisTemplate.opsForValue().set(key, "blacklisted", expirationTimeInMillis, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Check if token is blacklisted
     */
    public boolean isTokenBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    /**
     * Remove token from blacklist (if needed)
     */
    public void removeFromBlacklist(String token) {
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.delete(key);
    }
}

