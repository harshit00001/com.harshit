package com.harshit.inheritance.advanced;

/**
 * ABSTRACT CLASSES - Interview Explanation:
 * 
 * Abstract Class: Class that cannot be instantiated directly.
 * 
 * Technical Definition:
 * - Declared with 'abstract' keyword
 * - Can have abstract methods (no body)
 * - Can have concrete methods (with body)
 * - Can have fields, constructors, etc.
 * - Must be extended by child class
 * - Child must implement all abstract methods
 * 
 * Simple Explanation:
 * - Like a template or blueprint
 * - Can't create object directly
 * - Child classes must complete the template
 * - Some methods are defined, some are left for child to define
 * 
 * When to use:
 * - When you want to provide common functionality
 * - When you want to force child classes to implement certain methods
 * - When you have partial implementation
 */
public class AbstractClasses {

    public static void main(String[] args) {
        System.out.println("=== ABSTRACT CLASSES ===\n");
        
        demonstrateAbstractClass();
        demonstrateAbstractMethods();
        demonstrateConcreteMethods();
        demonstrateAbstractVsInterface();
    }
    
    /**
     * Interview Point: Abstract Class Usage
     * 
     * Technical Explanation:
     * - Abstract classes cannot be instantiated directly - you cannot create objects of abstract classes
     * - This is because abstract classes may have abstract methods (methods without implementation)
     * - If you could create an abstract class object, you could call abstract methods which have no code
     * - Abstract classes are meant to be extended by concrete (non-abstract) classes
     * - Concrete child classes must provide implementations for all abstract methods
     * - Abstract classes can have both abstract methods (no body) and concrete methods (with body)
     * - They serve as a partial implementation - some methods are defined, some are left for child to define
     * 
     * Simple Explanation:
     * - Think of abstract class as an incomplete template or blueprint
     * - You can't build a house from just a blueprint - you need the complete plan
     * - Abstract class is like a blueprint with some parts filled in and some parts left blank
     * - Child class completes the blueprint by filling in the blank parts (abstract methods)
     * - Once complete, you can create objects (build the house)
     * - It's like a recipe where some steps are written, but some steps say "you decide how to do this"
     * 
     * Interview Answer:
     * "Abstract classes cannot be instantiated directly because they may contain abstract methods
     * without implementations. They serve as partial implementations - some methods are defined,
     * some are left abstract for child classes to implement. Concrete child classes must implement
     * all abstract methods before they can be instantiated. Abstract classes are useful when you
     * want to provide common functionality while forcing child classes to implement specific methods."
     */
    private static void demonstrateAbstractClass() {
        System.out.println("--- Abstract Class ---");
        
        // Interview Point: Cannot instantiate abstract class
        // If we try to create a Shape object directly, we get a compilation error
        // This is because Shape is abstract - it may have abstract methods that have no implementation
        // You cannot call methods that don't exist, so you cannot create abstract class objects
        // Shape shape = new Shape(); // Compilation error - cannot instantiate abstract class
        
        // Interview Point: Must use concrete child class
        // To use an abstract class, you must extend it with a concrete (non-abstract) class
        // The concrete class must implement all abstract methods, making it complete and usable
        // Circle and Rectangle are concrete classes - they implement all abstract methods from Shape
        Circle circle = new Circle(5.0);
        Rectangle rectangle = new Rectangle(4.0, 6.0);
        
        // Interview Point: Can call methods on concrete child classes
        // These objects are complete - they have implementations for all methods
        // So we can create them and use them normally
        circle.draw();
        rectangle.draw();
        
        System.out.println("  → Abstract classes cannot be instantiated - they're incomplete");
        System.out.println("  → Must extend with concrete class that implements all abstract methods");
        System.out.println("  → Abstract class provides partial implementation + contract");
        System.out.println("  → Child class completes the implementation");
        System.out.println();
    }
    
    /**
     * Interview Point: Abstract Methods
     * 
     * Technical: Methods without body, must be implemented by child
     * Simple: "Child, you must provide this method"
     */
    private static void demonstrateAbstractMethods() {
        System.out.println("--- Abstract Methods ---");
        
        Circle circle = new Circle(5.0);
        System.out.println("  Circle area: " + circle.calculateArea());
        
        Rectangle rectangle = new Rectangle(4.0, 6.0);
        System.out.println("  Rectangle area: " + rectangle.calculateArea());
        
        System.out.println("  → Abstract methods must be implemented by child");
        System.out.println();
    }
    
    /**
     * Interview Point: Concrete Methods in Abstract Class
     * 
     * Technical: Abstract class can have methods with implementation
     * Simple: "Here's a method you can use, or override if needed"
     */
    private static void demonstrateConcreteMethods() {
        System.out.println("--- Concrete Methods in Abstract Class ---");
        
        Circle circle = new Circle(5.0);
        circle.displayInfo(); // Interview Point: Uses concrete method from abstract class
        
        System.out.println();
    }
    
    /**
     * Interview Point: Abstract vs Interface
     * 
     * Technical: Abstract class can have implementation, interface can't (before Java 8)
     * Simple: Abstract = partial template, Interface = contract
     */
    private static void demonstrateAbstractVsInterface() {
        System.out.println("--- Abstract Class vs Interface ---");
        
        System.out.println("  Abstract Class:");
        System.out.println("    → Can have abstract and concrete methods");
        System.out.println("    → Can have fields");
        System.out.println("    → Can have constructors");
        System.out.println("    → Single inheritance");
        System.out.println();
        System.out.println("  Interface:");
        System.out.println("    → All methods abstract (before Java 8)");
        System.out.println("    → Can only have constants");
        System.out.println("    → No constructors");
        System.out.println("    → Multiple inheritance");
        System.out.println();
    }
}

/**
 * Interview Point: Abstract Class
 * 
 * Technical: Cannot be instantiated, can have abstract methods
 * Simple: Template that child classes must complete
 */
abstract class Shape {
    protected String color;
    
    // Interview Point: Abstract class can have constructor
    public Shape(String color) {
        this.color = color;
    }
    
    // Interview Point: Abstract method - no body, must be implemented by child
    public abstract double calculateArea();
    
    // Interview Point: Abstract method - must be implemented by child
    public abstract void draw();
    
    // Interview Point: Concrete method - has implementation
    public void displayInfo() {
        System.out.println("  Shape color: " + color);
    }
    
    // Interview Point: Can have getters/setters
    public String getColor() {
        return color;
    }
}

/**
 * Interview Point: Concrete class extending abstract class
 * Must implement all abstract methods
 */
class Circle extends Shape {
    private double radius;
    
    public Circle(double radius) {
        super("Red"); // Interview Point: Call abstract class constructor
        this.radius = radius;
    }
    
    // Interview Point: Must implement abstract method
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    
    // Interview Point: Must implement abstract method
    @Override
    public void draw() {
        System.out.println("  Drawing Circle with radius: " + radius);
    }
}

/**
 * Interview Point: Another concrete class
 */
class Rectangle extends Shape {
    private double width;
    private double height;
    
    public Rectangle(double width, double height) {
        super("Blue");
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
    
    @Override
    public void draw() {
        System.out.println("  Drawing Rectangle: " + width + " x " + height);
    }
}

/**
 * INTERVIEW SUMMARY: Abstract Classes
 * 
 * Key Points:
 * 1. Cannot be instantiated directly
 * 2. Can have abstract methods (no body)
 * 3. Can have concrete methods (with body)
 * 4. Can have fields, constructors
 * 5. Child must implement all abstract methods
 * 6. Used for partial implementation
 * 
 * When to use:
 * - Provide common functionality
 * - Force child to implement certain methods
 * - Share code between related classes
 */

