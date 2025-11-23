package com.harshit.demo.entity;

import javax.persistence.*;

/**
 * Order Entity - Demonstrates LAZY loading relationships
 * 
 * STRATEGY 2: LAZY LOADING
 * Notice the FetchType.LAZY on the relationship
 */
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String orderNumber;
    
    private Double totalAmount;
    
    /**
     * LAZY LOADING EXAMPLE
     * @ManyToOne with FetchType.LAZY
     * User is only loaded when you access order.getUser()
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    public Order() {}
    
    public Order(String orderNumber, Double totalAmount) {
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}

