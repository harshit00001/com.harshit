package com.harshit.jwt.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Token Utility Class - Handles JWT token creation and validation
 * 
 * This is the CORE class for JWT operations. Understanding this is crucial
 * for JWT interviews.
 * 
 * What is JWT?
 * - JSON Web Token is a compact, URL-safe token format
 * - Contains 3 parts: Header.Payload.Signature
 * - Stateless: Server doesn't need to store session
 * - Self-contained: Token contains all necessary information
 * 
 * JWT Structure:
 * Header: Algorithm and token type
 * Payload: Claims (user info, expiration, etc.)
 * Signature: Ensures token hasn't been tampered with
 */
@Component
public class JwtTokenUtil {
    
    /**
     * Secret key for signing JWT tokens
     * In production, use a strong, randomly generated secret (at least 256 bits for HS512)
     * Store in environment variables or secure vault (not in code!)
     */
    @Value("${jwt.secret}")
    private String secret;
    
    /**
     * Token expiration time in milliseconds
     * Default: 24 hours (86400000 ms)
     */
    @Value("${jwt.expiration}")
    private Long expiration;
    
    /**
     * Get secret key for signing
     * Converts string secret to SecretKey object
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Extract username from token
     * 
     * How it works:
     * 1. Extract "sub" (subject) claim from token
     * 2. "sub" typically contains username
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    /**
     * Extract expiration date from token
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    /**
     * Extract a specific claim from token
     * 
     * Generic method to extract any claim
     * Uses Function interface for flexibility
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Get all claims from token
     * 
     * This method:
     * 1. Parses the token
     * 2. Validates signature
     * 3. Returns claims (payload)
     * 
     * Throws exceptions if:
     * - Token is malformed
     * - Signature is invalid
     * - Token is expired
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Check if token is expired
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * Generate token for user
     * 
     * This is the MAIN method for creating JWT tokens
     * 
     * Steps:
     * 1. Create claims (payload) with user info
     * 2. Set subject (username)
     * 3. Set issued at time
     * 4. Set expiration time
     * 5. Sign with secret key
     * 6. Return compact token string
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * Generate token with custom claims
     * 
     * Custom claims can include:
     * - User roles
     * - User ID
     * - Additional metadata
     */
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return createToken(extraClaims, userDetails.getUsername());
    }
    
    /**
     * Create JWT token
     * 
     * This method actually builds the JWT:
     * - Sets claims (payload)
     * - Sets subject (username)
     * - Sets issued time
     * - Sets expiration
     * - Signs with secret key
     * - Returns compact string
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)                          // Custom claims
                .setSubject(subject)                         // Username
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Issued time
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // Expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)  // Sign with HS512
                .compact();                                 // Convert to string
    }
    
    /**
     * Validate token
     * 
     * Checks:
     * 1. Token is not expired
     * 2. Username in token matches provided username
     * 
     * Returns true only if both conditions are met
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * Validate token structure and signature
     * 
     * This method checks if token is:
     * - Properly formatted
     * - Has valid signature
     * - Not expired
     * 
     * Throws exceptions if validation fails
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid
            return false;
        }
    }
    
    /**
     * Extract roles from token
     * 
     * Roles are typically stored in "roles" claim
     */
    @SuppressWarnings("unchecked")
    public String[] getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Object rolesObj = claims.get("roles");
        
        if (rolesObj instanceof java.util.List) {
            java.util.List<String> rolesList = (java.util.List<String>) rolesObj;
            return rolesList.toArray(new String[0]);
        }
        
        return new String[0];
    }
}

