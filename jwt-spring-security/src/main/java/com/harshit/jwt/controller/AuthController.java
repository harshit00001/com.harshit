package com.harshit.jwt.controller;

import com.harshit.jwt.dto.JwtResponse;
import com.harshit.jwt.dto.LoginRequest;
import com.harshit.jwt.dto.RegisterRequest;
import com.harshit.jwt.entity.RefreshToken;
import com.harshit.jwt.entity.User;
import com.harshit.jwt.service.RefreshTokenService;
import com.harshit.jwt.service.TokenBlacklistService;
import com.harshit.jwt.service.UserService;
import com.harshit.jwt.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Authentication Controller - Handles authentication endpoints
 * 
 * Endpoints:
 * - POST /api/auth/register - Register new user
 * - POST /api/auth/login - Login and get JWT token
 * - POST /api/auth/refresh - Refresh access token
 * - POST /api/auth/logout - Logout (blacklist token)
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    
    /**
     * Register new user
     * 
     * Flow:
     * 1. Validate request
     * 2. Create user (password is encoded)
     * 3. Return success message
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.registerUser(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getEmail()
            );
            
            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "username", user.getUsername()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Login and get JWT token
     * 
     * Flow:
     * 1. Authenticate user (Spring Security)
     * 2. Generate JWT access token
     * 3. Generate refresh token
     * 4. Return tokens
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        // Set authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Get user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // Generate JWT token
        String accessToken = jwtTokenUtil.generateToken(userDetails);
        
        // Generate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        
        // Get user roles
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        
        // Return JWT response
        return ResponseEntity.ok(new JwtResponse(
                accessToken,
                refreshToken.getToken(),
                userDetails.getUsername(),
                roles
        ));
    }
    
    /**
     * Refresh access token
     * 
     * Flow:
     * 1. Validate refresh token
     * 2. Generate new access token
     * 3. Optionally rotate refresh token (for security)
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        
        return refreshTokenService.validateRefreshToken(refreshToken)
                .map(token -> {
                    User user = token.getUser();
                    UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
                    
                    // Generate new access token
                    String newAccessToken = jwtTokenUtil.generateToken(userDetails);
                    
                    return ResponseEntity.ok(Map.of(
                            "accessToken", newAccessToken,
                            "refreshToken", refreshToken  // Same refresh token (or rotate it)
                    ));
                })
                .orElse(ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid refresh token")));
    }
    
    /**
     * Logout - Blacklist token
     * 
     * Flow:
     * 1. Extract token from request
     * 2. Add token to blacklist
     * 3. Delete refresh token
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            // Get token expiration
            try {
                long expirationTime = jwtTokenUtil.getExpirationDateFromToken(token).getTime();
                long currentTime = System.currentTimeMillis();
                long remainingTime = expirationTime - currentTime;
                
                // Blacklist token
                if (remainingTime > 0) {
                    tokenBlacklistService.blacklistToken(token, remainingTime);
                }
                
                // Delete refresh token
                String username = jwtTokenUtil.getUsernameFromToken(token);
                User user = userService.getUserByUsername(username);
                refreshTokenService.deleteRefreshTokenByUser(user);
                
                return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid token"));
            }
        }
        
        return ResponseEntity.badRequest()
                .body(Map.of("error", "No token provided"));
    }
}

