package com.harshit.jwt.entity;

import javax.persistence.*;

/**
 * Role Entity - Represents user roles in the system
 * 
 * Common roles:
 * - ROLE_USER: Regular user
 * - ROLE_ADMIN: Administrator
 * - ROLE_MODERATOR: Moderator (example)
 */
@Entity
@Table(name = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Role name (e.g., ROLE_USER, ROLE_ADMIN)
     * Spring Security expects roles to start with "ROLE_" prefix
     */
    @Column(unique = true, nullable = false)
    private String name;
    
    // Default constructor
    public Role() {}
    
    // Constructor with name
    public Role(String name) {
        this.name = name;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}

