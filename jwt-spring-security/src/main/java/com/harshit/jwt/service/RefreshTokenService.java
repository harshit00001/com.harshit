package com.harshit.jwt.service;

import com.harshit.jwt.entity.RefreshToken;
import com.harshit.jwt.entity.User;
import com.harshit.jwt.repository.RefreshTokenRepository;
import com.harshit.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Refresh Token Service - Manages refresh tokens
 * 
 * Why Refresh Tokens?
 * - Access tokens have short expiration (security)
 * - Refresh tokens allow getting new access tokens without re-login
 * - Can be revoked/blacklisted if compromised
 */
@Service
public class RefreshTokenService {
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Refresh token expiration time (default: 7 days)
     */
    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration;
    
    /**
     * Create refresh token for user
     * 
     * Steps:
     * 1. Generate unique token (UUID)
     * 2. Set expiration (7-30 days)
     * 3. Delete old refresh token if exists
     * 4. Save new refresh token
     */
    @Transactional
    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Delete existing refresh token (one token per user)
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);
        
        // Create new refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
        
        return refreshTokenRepository.save(refreshToken);
    }
    
    /**
     * Validate refresh token
     * 
     * Checks:
     * 1. Token exists in database
     * 2. Token is not expired
     */
    public Optional<RefreshToken> validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(refreshToken -> !refreshToken.isExpired());
    }
    
    /**
     * Delete refresh token (logout)
     */
    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshTokenRepository::delete);
    }
    
    /**
     * Delete refresh token by user
     */
    @Transactional
    public void deleteRefreshTokenByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}

