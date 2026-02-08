package com.harshit.hibernate.basic.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * User Entity - Basic Hibernate/JPA Entity Example
 * 
 * ====================================================================================
 * INTERVIEW QUESTION: Explain JPA entity annotations and their purpose.
 * ====================================================================================
 * 
 * DETAILED ANSWER:
 * 
 * JPA entities are Java classes that represent database tables. They use annotations
 * to map Java objects to relational database structures. Let me explain each annotation:
 * 
 * @Entity:
 * - Marks this class as a JPA entity
 * - Tells JPA that this class should be mapped to a database table
 * - The table name defaults to the class name (User -> users, camelCase to snake_case)
 * - Entity classes must have a no-argument constructor
 * - Entity classes should not be final (Hibernate uses proxies)
 * 
 * @Table:
 * - Specifies the database table name
 * - Optional - if omitted, table name is derived from entity name
 * - Can specify schema, catalog, indexes, unique constraints
 * - Example: @Table(name = "users", schema = "public")
 * 
 * @Id:
 * - Marks a field as the primary key
 * - Each entity must have exactly one @Id field
 * - Can be on a field or getter method
 * - Primary key uniquely identifies each row in the table
 * 
 * @GeneratedValue:
 * - Specifies how the primary key value is generated
 * - Strategies:
 *   * IDENTITY: Database auto-increment (MySQL AUTO_INCREMENT, PostgreSQL SERIAL)
 *   * SEQUENCE: Uses database sequence (Oracle, PostgreSQL)
 *   * TABLE: Uses a separate table to generate IDs
 *   * AUTO: Let JPA provider choose (usually IDENTITY or SEQUENCE)
 * - IDENTITY is most common and works with most databases
 * 
 * @Column:
 * - Maps a field to a database column
 * - Optional - if omitted, column name is derived from field name
 * - Attributes:
 *   * name: Column name in database
 *   * nullable: Can the column be NULL?
 *   * unique: Is the column unique?
 *   * length: Maximum length (for String)
 *   * precision/scale: For BigDecimal
 *   * insertable/updatable: Can this column be inserted/updated?
 * 
 * @Temporal:
 * - Specifies how Date/Calendar should be persisted
 * - Types:
 *   * DATE: Only date (YYYY-MM-DD)
 *   * TIME: Only time (HH:MM:SS)
 *   * TIMESTAMP: Date and time (YYYY-MM-DD HH:MM:SS)
 * - Required for Date/Calendar fields in JPA 2.1 and earlier
 * - Optional in JPA 2.2+ (uses TIMESTAMP by default)
 * 
 * @Enumerated:
 * - Maps Java enum to database column
 * - Strategies:
 *   * ORDINAL: Stores enum index (0, 1, 2...) - not recommended (breaks if enum order changes)
 *   * STRING: Stores enum name ("ACTIVE", "INACTIVE") - recommended
 * 
 * @PrePersist and @PreUpdate:
 * - Lifecycle callbacks - methods called automatically by JPA
 * - @PrePersist: Called before entity is persisted (INSERT)
 * - @PreUpdate: Called before entity is updated (UPDATE)
 * - Useful for setting timestamps, validation, etc.
 * 
 * ====================================================================================
 * REAL-WORLD SCENARIO:
 * ====================================================================================
 * 
 * In a user management system, this entity would represent a user account:
 * - Each user has a unique ID (primary key)
 * - Username and email must be unique
 * - Created/updated timestamps track when user was created/modified
 * - Status enum represents user account state (active, inactive, suspended)
 * 
 * This entity would be used in:
 * - User registration (persist new user)
 * - User login (find by username/email)
 * - User profile updates (update existing user)
 * - User management (activate/deactivate users)
 */
@Entity
@Table(name = "users", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "email")
       })
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
    @Column(name = "status", nullable = false)
    private UserStatus status = UserStatus.ACTIVE;
    
    /**
     * Default constructor (required by JPA/Hibernate)
     * 
     * JPA requires entities to have a no-argument constructor.
     * This is used by Hibernate when loading entities from database.
     */
    public User() {
    }
    
    /**
     * Convenience constructor
     * 
     * Creates user with required fields.
     * Timestamps will be set automatically by @PrePersist callback.
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.status = UserStatus.ACTIVE;
    }
    
    /**
     * Lifecycle Callback: Before Persist
     * 
     * Automatically called by JPA before INSERT operation.
     * Sets createdAt and updatedAt timestamps.
     * This ensures timestamps are always set correctly.
     */
    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }
    
    /**
     * Lifecycle Callback: Before Update
     * 
     * Automatically called by JPA before UPDATE operation.
     * Updates the updatedAt timestamp.
     * This tracks when entity was last modified.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
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

