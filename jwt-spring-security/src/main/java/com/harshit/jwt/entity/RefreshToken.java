package com.harshit.jwt.entity;

import javax.persistence.*;
import java.time.Instant;

/**
 * Refresh Token Entity - Stores refresh tokens for users
 * 
 * Why separate refresh tokens?
 * - Access tokens have short expiration (15 min - 1 hour)
 * - Refresh tokens have long expiration (7 days - 30 days)
 * - If access token is stolen, damage is limited
 * - Refresh tokens can be revoked/blacklisted
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * One-to-One relationship with User
     * Each user can have one active refresh token
     * (In production, you might allow multiple devices)
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    @Column(nullable = false, unique = true)
    private String token;
    
    /**
     * Expiration timestamp
     * Refresh tokens typically expire after 7-30 days
     */
    @Column(nullable = false)
    private Instant expiryDate;
    
    // Default constructor
    public RefreshToken() {}
    
    // Constructor
    public RefreshToken(User user, String token, Instant expiryDate) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Instant getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    /**
     * Check if token is expired
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }
}

