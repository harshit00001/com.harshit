package com.rest.annotations.model;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * EMPLOYEE MODEL CLASS
 * 
 * Interview Explanation:
 * This is a simple POJO (Plain Old Java Object) representing an Employee.
 * It uses validation annotations to ensure data integrity.
 */
public class Employee {
    
    @NotNull(message = "Employee ID is required")
    private Long id;
    
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Department cannot be blank")
    private String department;
    
    @NotNull(message = "Salary is required")
    @Min(value = 0, message = "Salary must be positive")
    private Double salary;
    
    @Past(message = "Hire date must be in the past")
    private LocalDate hireDate;
    
    private String position;
    
    // Default constructor
    public Employee() {
    }
    
    // Constructor with all fields
    public Employee(Long id, String name, String email, String department, 
                   Double salary, LocalDate hireDate, String position) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
        this.position = position;
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
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public Double getSalary() {
        return salary;
    }
    
    public void setSalary(Double salary) {
        this.salary = salary;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                ", hireDate=" + hireDate +
                ", position='" + position + '\'' +
                '}';
    }
}



