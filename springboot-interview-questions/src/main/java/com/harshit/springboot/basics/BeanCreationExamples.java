package com.harshit.springboot.basics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * BEAN CREATION EXAMPLES - Different Ways to Create Spring Beans
 * 
 * Spring provides multiple ways to create and register beans. Understanding these
 * different approaches and when to use each one is important for Spring Boot interviews.
 * The most common ways are using @Component annotation and using @Bean annotation
 * in a @Configuration class.
 */
@Configuration
public class BeanCreationExamples {
    
    /**
     * METHOD 1: Using @Component Annotation
     * 
     * When you annotate a class with @Component (or its specializations like @Service,
     * @Repository, @Controller), Spring automatically detects and registers it as a bean
     * during component scanning. This is the simplest and most common approach for
     * creating beans. Spring uses reflection to instantiate the class, so the class
     * must have a default constructor or a constructor that Spring can use.
     * 
     * If a class has a private constructor, Spring cannot instantiate it directly.
     * In such cases, you need to use a static factory method and register the bean
     * using @Bean annotation in a configuration class.
     */
    
    /**
     * METHOD 2: Using @Bean Annotation in @Configuration Class
     * 
     * The @Bean annotation is used in a @Configuration class to define beans explicitly.
     * This approach gives you more control over bean creation. You can use this method
     * when you need to create beans from third-party libraries, when you need to configure
     * beans with specific parameters, or when you need to use factory methods.
     * 
     * This is also the way to create beans when you have a class with a private constructor
     * and a static factory method. You call the factory method in the @Bean method to
     * create the instance.
     */
    @Bean
    public Employee createEmployee() {
        // Interview Point: Can call factory methods, configure with parameters, etc.
        return Employee.createInstance("John Doe", "Developer");
    }
    
    /**
     * METHOD 3: Multiple Beans of Same Type
     * 
     * You can have multiple @Bean methods that return the same type. This is useful when
     * you need different configurations of the same type. However, when you have multiple
     * beans of the same type, Spring needs to know which one to inject. You can use
     * @Primary to mark one as the default, or use @Qualifier to specify which one to use.
     */
    @Bean
    public Shape rectangle() {
        return new Rectangle(10, 20);
    }
    
    @Bean
    @Primary  // This bean will be used by default when Shape is autowired
    public Shape square() {
        return new Square(15);
    }
    
    @Bean
    public Shape circle() {
        return new Circle(5);
    }
    
    /**
     * METHOD 4: Bean with Private Constructor
     * 
     * If a class has a private constructor, Spring cannot instantiate it directly using
     * @Component. You must use a static factory method and register it using @Bean.
     */
    @Bean
    public EmployeeWithPrivateConstructor createEmployeeWithPrivateConstructor() {
        // Interview Point: Must use static factory method when constructor is private
        return EmployeeWithPrivateConstructor.createInstance("Jane Smith", "Manager");
    }
}

/**
 * Example class with static factory method
 */
class Employee {
    private String name;
    private String role;
    
    // Private constructor - Spring cannot use this directly
    private Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }
    
    // Static factory method - must use this with @Bean
    public static Employee createInstance(String name, String role) {
        return new Employee(name, role);
    }
    
    public String getName() {
        return name;
    }
    
    public String getRole() {
        return role;
    }
}

/**
 * Example with private constructor
 */
class EmployeeWithPrivateConstructor {
    private String name;
    private String role;
    
    private EmployeeWithPrivateConstructor() {
        // Private constructor
    }
    
    public static EmployeeWithPrivateConstructor createInstance(String name, String role) {
        EmployeeWithPrivateConstructor emp = new EmployeeWithPrivateConstructor();
        emp.name = name;
        emp.role = role;
        return emp;
    }
}

/**
 * Shape interface and implementations for multiple beans example
 */
interface Shape {
    double getArea();
}

class Rectangle implements Shape {
    private double width;
    private double height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
}

class Square implements Shape {
    private double side;
    
    public Square(double side) {
        this.side = side;
    }
    
    @Override
    public double getArea() {
        return side * side;
    }
}

class Circle implements Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

