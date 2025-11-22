package com.harshit.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application for JWT Spring Security
 * 
 * This application demonstrates JWT (JSON Web Token) implementation
 * with Spring Security from Basic to Advanced level.
 * 
 * Features:
 * - Basic JWT authentication
 * - Refresh token mechanism
 * - Role-based access control (RBAC)
 * - Token blacklisting
 * - Rate limiting
 * - Multi-factor authentication concepts
 */
@SpringBootApplication
public class JwtSpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtSpringSecurityApplication.class, args);
        System.out.println("\n=========================================");
        System.out.println("JWT Spring Security Application Started!");
        System.out.println("=========================================");
        System.out.println("API Endpoints:");
        System.out.println("POST /api/auth/register - Register new user");
        System.out.println("POST /api/auth/login - Login and get JWT token");
        System.out.println("POST /api/auth/refresh - Refresh access token");
        System.out.println("GET /api/user/profile - Get user profile (requires authentication)");
        System.out.println("GET /api/admin/users - Get all users (requires ADMIN role)");
        System.out.println("POST /api/auth/logout - Logout (blacklist token)");
        System.out.println("=========================================\n");
    }
}

