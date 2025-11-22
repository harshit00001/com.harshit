package com.harshit.jwt.dto;

import javax.validation.constraints.NotBlank;

/**
 * Login Request DTO (Data Transfer Object)
 * 
 * DTOs are used to transfer data between layers
 * - Separates API contract from internal entities
 * - Allows validation
 * - Prevents exposing internal structure
 */
public class LoginRequest {
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    // Default constructor
    public LoginRequest() {}
    
    // Constructor with parameters
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}

