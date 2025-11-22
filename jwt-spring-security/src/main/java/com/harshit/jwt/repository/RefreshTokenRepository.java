package com.harshit.jwt.repository;

import com.harshit.jwt.entity.RefreshToken;
import com.harshit.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Refresh Token Repository - Data access layer for RefreshToken entity
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    /**
     * Find refresh token by token string
     */
    Optional<RefreshToken> findByToken(String token);
    
    /**
     * Find refresh token by user
     */
    Optional<RefreshToken> findByUser(User user);
    
    /**
     * Delete refresh token by user
     * Used when user logs out
     */
    void deleteByUser(User user);
}

