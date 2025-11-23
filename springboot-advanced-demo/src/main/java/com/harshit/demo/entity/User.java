package com.harshit.demo.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * User Entity - Demonstrates LAZY loading
 * 
 * STRATEGY 2: LAZY LOADING
 * Always use FetchType.LAZY for relationships to avoid N+1 query problem
 */
@Entity
@Table(name = "users")
@NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String name;
    
    @Email
    @NotNull
    @Column(unique = true, nullable = false)
    private String email;
    
    private Integer age;
    
    /**
     * LAZY LOADING EXAMPLE
     * @OneToMany with FetchType.LAZY
     * Orders are only loaded when you access user.getOrders()
     * 
     * If you used FetchType.EAGER:
     * - Loading 100 users would execute 1 query for users + 100 queries for orders = 101 queries
     * 
     * With FetchType.LAZY:
     * - Loading 100 users executes only 1 query
     * - Orders are loaded only when accessed
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<com.harshit.demo.entity.Order> orders;
    
    public User() {}
    
    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public List<com.harshit.demo.entity.Order> getOrders() { return orders; }
    public void setOrders(List<com.harshit.demo.entity.Order> orders) { this.orders = orders; }
}

