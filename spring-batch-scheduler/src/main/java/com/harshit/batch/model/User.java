package com.harshit.batch.model;

/**
 * USER MODEL - Interview Explanation:
 * 
 * This represents the data structure we're processing.
 * In our example, we read user data from CSV and store it in database.
 * 
 * Simple Explanation:
 * - This is what one row of CSV becomes
 * - Like a form with name, email, age fields
 */
public class User {
    
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private String department;
    
    // Default constructor (required for Spring Batch)
    public User() {
    }
    
    // Constructor with all fields
    public User(Long id, String name, String email, Integer age, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.department = department;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + 
               "', age=" + age + ", department='" + department + "'}";
    }
}

