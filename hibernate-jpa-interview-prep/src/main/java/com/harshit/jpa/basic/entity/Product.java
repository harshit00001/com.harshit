package com.harshit.jpa.basic.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * JPA Entity: Product
 * 
 * INTERVIEW QUESTION: What is JPA and how is it different from Hibernate?
 * 
 * ANSWER:
 * JPA (Java Persistence API) is a specification for ORM in Java.
 * Hibernate is an implementation of JPA.
 * 
 * KEY DIFFERENCES:
 * - JPA is a standard/specification, Hibernate is an implementation
 * - JPA uses EntityManager, Hibernate uses Session
 * - JPA uses JPQL, Hibernate uses HQL
 * - You can switch JPA implementations (Hibernate, EclipseLink, OpenJPA)
 * 
 * JPA ANNOTATIONS:
 * - @Entity: Marks class as JPA entity
 * - @Table: Specifies table name
 * - @Id: Primary key
 * - @GeneratedValue: Auto-generation strategy
 * - @Column: Column mapping
 * 
 * REAL-WORLD SCENARIO:
 * Product entity in an e-commerce system represents items for sale.
 */
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;
    
    @Column(name = "stock_quantity")
    private Integer stockQuantity;
    
    @Column(name = "sku", unique = true)
    private String sku;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;
    
    public Product() {
    }
    
    public Product(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = 0;
        this.createdAt = new Date();
        this.status = ProductStatus.ACTIVE;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public ProductStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProductStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", status=" + status +
                '}';
    }
}

