package com.harshit.springboot.basics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Component, @Service, @Repository - Understanding the Differences
 * 
 * These three annotations are all specializations of @Component, which means they
 * all mark a class as a Spring-managed component. However, they serve different
 * purposes and communicate different intentions about what the class does. Using
 * the right annotation improves code readability and helps maintain clean architecture.
 * 
 * All three annotations are detected by @ComponentScan, so functionally they work
 * the same way - Spring will create beans from all of them. The difference is in
 * the semantic meaning and the additional features that some annotations provide.
 */
@Component
class EmployeeComponent {
    /**
     * @Component is a generic stereotype annotation that marks a class as a
     * Spring-managed component. It is the parent annotation for @Service and
     * @Repository. You can use @Component for any Spring bean that doesn't fit
     * into the service or repository layer.
     * 
     * Use cases for @Component include utility classes, helper components, or
     * any general-purpose Spring bean that doesn't have a specific role in the
     * service or repository layers.
     */
    public List<String> showCourses() {
        return Arrays.asList("Java", "Spring", "Hibernate");
    }
}

@Service
class EmployeeServiceImpl {
    /**
     * @Service is a specialized @Component used for business logic or service
     * layer beans. It makes it clear that the class performs business-related
     * operations and helps maintain clean code and separation of concerns.
     * 
     * Use cases for @Service include classes that implement business logic,
     * transactional operations, and service-layer operations. The @Service
     * annotation doesn't add any special functionality beyond @Component, but
     * it communicates the intent that this class is part of the service layer.
     */
    private EmployeeDetails empDetails;
    
    public String getEmpName(int empId) {
        String empName = empDetails.getById(empId).toUpperCase();
        return empName;
    }
}

@Repository
interface ICategoryRepository extends JpaRepository<Category, Integer> {
    /**
     * @Repository is a specialized @Component used for the DAO (Data Access Object)
     * layer. It indicates that the class interacts with the database and provides
     * exception translation, where Spring automatically converts JDBC/SQL exceptions
     * into Spring DataAccessExceptions.
     * 
     * Use cases for @Repository include when working with Spring Data JPA, Hibernate,
     * or any database-related operations. The @Repository annotation provides
     * exception translation, which means that database-specific exceptions are
     * automatically converted to Spring's DataAccessException hierarchy, making
     * exception handling more consistent across different database technologies.
     */
}

/**
 * Helper classes for demonstration
 */
class EmployeeDetails {
    public Employee getById(int id) {
        return new Employee("John", id);
    }
}

class Employee {
    private String name;
    private int id;
    
    Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
}

class Category {
    // Entity class
}

