package com.harshit.interview.model;

import java.util.Objects;

/**
 * Sample domain object used in Stream / CompletableFuture demos.
 */
public final class Employee {

    private final String name;
    private final int id;
    private final String city;
    private final double salary;
    private final String department;

    public Employee(String name, int id, String city, double salary, String department) {
        this.name = name;
        this.id = id;
        this.city = city;
        this.salary = salary;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public double getSalary() {
        return salary;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return "Employee{name='%s', city='%s', salary=%.0f, dept='%s'}".formatted(name, city, salary, department);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee other)) {
            return false;
        }
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
