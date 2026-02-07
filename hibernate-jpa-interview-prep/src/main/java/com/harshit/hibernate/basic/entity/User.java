package com.harshit.hibernate.basic.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Example Entity: User
 * 
 * INTERVIEW QUESTION: What are the key annotations in Hibernate/JPA?
 * 
 * ANSWER:
 * @Entity: Marks class as a JPA entity (table in database)
 * @Table: Specifies table name (optional, defaults to class name)
 * @Id: Marks field as primary key
 * @GeneratedValue: Auto-generates primary key values
 * @Column: Maps field to column (optional, defaults to field name)
 * @Temporal: Maps Date/Calendar to appropriate SQL type
 * @Enumerated: Maps enum to database column
 * 
 * REAL-WORLD SCENARIO:
 * User entity represents a customer in an e-commerce system.
 * Each user has a unique ID, email, and profile information.
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "age")
    private Integer age;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;
    
    // Default constructor (required by Hibernate)
    public User() {
    }
    
    // Constructor with required fields
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.createdAt = new Date();
        this.status = UserStatus.ACTIVE;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
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
    
    public UserStatus getStatus() {
        return status;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", status=" + status +
                '}';
    }
}

enum UserStatus {
    ACTIVE, INACTIVE, SUSPENDED
}

