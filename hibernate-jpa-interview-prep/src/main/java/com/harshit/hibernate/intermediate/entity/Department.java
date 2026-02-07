package com.harshit.hibernate.intermediate.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Example: One-to-Many Relationship
 * 
 * INTERVIEW QUESTION: How do you map one-to-many relationships in Hibernate?
 * 
 * ANSWER:
 * Use @OneToMany on the parent entity and @ManyToOne on the child entity.
 * 
 * Key Annotations:
 * - @OneToMany(mappedBy = "department"): Inverse side (no foreign key column)
 * - @ManyToOne: Owning side (has foreign key column)
 * - cascade = CascadeType.ALL: Operations cascade to children
 * - fetch = FetchType.LAZY: Load children on-demand (default for @OneToMany)
 * - fetch = FetchType.EAGER: Load children immediately (default for @ManyToOne)
 * 
 * REAL-WORLD SCENARIO:
 * A department has many employees. When you delete a department,
 * you might want to reassign employees or delete them (cascade).
 */
@Entity
@Table(name = "departments")
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "location")
    private String location;
    
    // One-to-Many: One department has many employees
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();
    
    public Department() {
    }
    
    public Department(String name, String location) {
        this.name = name;
        this.location = location;
    }
    
    // Helper method to maintain bidirectional relationship
    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
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
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public List<Employee> getEmployees() {
        return employees;
    }
    
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    
    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", employeesCount=" + (employees != null ? employees.size() : 0) +
                '}';
    }
}

