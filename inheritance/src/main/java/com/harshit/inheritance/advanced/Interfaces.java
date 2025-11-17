package com.harshit.inheritance.advanced;

/**
 * INTERFACES - Interview Explanation:
 * 
 * Interface: Contract that defines what a class must do, not how.
 * 
 * Technical Definition:
 * - Declared with 'interface' keyword
 * - All methods are public and abstract (before Java 8)
 * - Can have constants (public static final)
 * - Class implements interface using 'implements' keyword
 * - Class must implement all methods
 * - Supports multiple inheritance (class can implement multiple interfaces)
 * 
 * Simple Explanation:
 * - Like a contract or agreement
 * - Says "you must have these methods"
 * - Doesn't say how to implement them
 * - Class signs the contract by implementing interface
 * 
 * Java 8+ Features:
 * - Default methods (with implementation)
 * - Static methods
 * - Private methods (Java 9+)
 */
public class Interfaces {

    public static void main(String[] args) {
        System.out.println("=== INTERFACES ===\n");
        
        demonstrateBasicInterface();
        demonstrateMultipleInterfaces();
        demonstrateDefaultMethods();
        demonstrateStaticMethods();
        demonstrateInterfaceInheritance();
    }
    
    /**
     * Interview Point: Basic Interface Implementation
     * 
     * Technical Explanation:
     * - When a class implements an interface, it must provide implementations for all abstract methods
     *   in that interface (before Java 8, all methods were abstract)
     * - A class can implement multiple interfaces, providing implementations for all methods in all interfaces
     * - The class "signs a contract" - it agrees to provide these methods
     * - If a class doesn't implement all methods, it must be declared abstract
     * - You can use interface as a reference type - this enables polymorphism
     * - Interface reference can point to any object of a class that implements that interface
     * 
     * Simple Explanation:
     * - Think of interface as a contract or agreement
     * - When a class implements an interface, it's like signing a contract saying "I will provide these methods"
     * - The class must fulfill the contract by implementing all required methods
     * - It's like a job - you agree to do certain tasks (interface methods), and you must do them (implement them)
     * - You can have multiple contracts (implement multiple interfaces)
     * - Interface reference is like saying "I need something that can do X" - any class that implements
     *   the interface can be used
     * 
     * Interview Answer:
     * "When a class implements an interface, it must provide implementations for all abstract methods
     * in that interface. The class is essentially signing a contract - it agrees to provide these methods.
     * If a class implements an interface but doesn't provide all methods, it must be declared abstract.
     * A class can implement multiple interfaces. You can use the interface as a reference type, which
     * allows polymorphism - any class implementing the interface can be used where the interface is expected."
     */
    private static void demonstrateBasicInterface() {
        System.out.println("--- Basic Interface ---");
        
        // Interview Point: Using interface as reference type
        // Drawable is an interface, and we're using it as the reference type
        // This is polymorphism - we can use any class that implements Drawable
        // CircleShape implements Drawable, so we can use Drawable reference to point to CircleShape object
        Drawable circle = new CircleShape(5.0);
        // Interview Point: Can call interface methods
        // Even though reference is Drawable type, we can call methods defined in Drawable interface
        // The actual implementation comes from CircleShape class
        circle.draw();
        System.out.println("  Area: " + circle.calculateArea());
        
        // Interview Point: Same interface, different implementation
        // RectangleShape also implements Drawable, so we can use same interface reference
        // This demonstrates polymorphism - same interface, different implementations
        Drawable rectangle = new RectangleShape(4.0, 6.0);
        rectangle.draw();
        System.out.println("  Area: " + rectangle.calculateArea());
        
        System.out.println("  → Interface defines contract - class must implement all methods");
        System.out.println("  → Can use interface as reference type for polymorphism");
        System.out.println("  → Different classes can implement same interface differently");
        System.out.println();
    }
    
    /**
     * Interview Point: Multiple Interface Implementation
     * 
     * Technical: Class can implement multiple interfaces
     * Simple: Class can sign multiple contracts
     */
    private static void demonstrateMultipleInterfaces() {
        System.out.println("--- Multiple Interfaces ---");
        
        SmartPhone phone = new SmartPhone();
        phone.call();      // From Phone interface
        phone.sendSMS();  // From Phone interface
        phone.takePhoto(); // From Camera interface
        phone.recordVideo(); // From Camera interface
        
        System.out.println("  → Class can implement multiple interfaces");
        System.out.println("  → This is how Java achieves multiple inheritance");
        System.out.println();
    }
    
    /**
     * Interview Point: Default Methods (Java 8+)
     * 
     * Technical: Interface can have methods with implementation
     * Simple: Interface can provide default behavior
     */
    private static void demonstrateDefaultMethods() {
        System.out.println("--- Default Methods (Java 8+) ---");
        
        Vehicle car = new Car();
        car.start();        // Overridden method
        car.honk();         // Interview Point: Default method from interface
        car.stop();         // Overridden method
        
        System.out.println();
    }
    
    /**
     * Interview Point: Static Methods in Interface (Java 8+)
     * 
     * Technical: Interface can have static methods
     * Simple: Methods that belong to interface, not implementing class
     */
    private static void demonstrateStaticMethods() {
        System.out.println("--- Static Methods in Interface ---");
        
        // Interview Point: Call static method using interface name
        MathOperations.add(5, 3);
        MathOperations.multiply(4, 6);
        
        System.out.println("  → Static methods belong to interface");
        System.out.println("  → Called using interface name, not object");
        System.out.println();
    }
    
    /**
     * Interview Point: Interface Inheritance
     * 
     * Technical: Interface can extend other interfaces
     * Simple: Interface can inherit from other interfaces
     */
    private static void demonstrateInterfaceInheritance() {
        System.out.println("--- Interface Inheritance ---");
        
        AdvancedDevice device = new SmartDevice();
        device.turnOn();    // From Device interface
        device.turnOff();   // From Device interface
        device.connect();   // From AdvancedDevice interface
        device.disconnect(); // From AdvancedDevice interface
        
        System.out.println();
    }
}

/**
 * Interview Point: Basic Interface
 * 
 * Technical: Defines contract (methods that must be implemented)
 * Simple: Agreement that class must follow
 */
interface Drawable {
    // Interview Point: Abstract method (implicitly public abstract)
    void draw();
    
    // Interview Point: Abstract method
    double calculateArea();
    
    // Interview Point: Constant (implicitly public static final)
    String DEFAULT_COLOR = "Black";
}

/**
 * Interview Point: Class implementing interface
 */
class CircleShape implements Drawable {
    private double radius;
    
    public CircleShape(double radius) {
        this.radius = radius;
    }
    
    // Interview Point: Must implement all interface methods
    @Override
    public void draw() {
        System.out.println("  Drawing Circle with radius: " + radius);
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

class RectangleShape implements Drawable {
    private double width;
    private double height;
    
    public RectangleShape(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void draw() {
        System.out.println("  Drawing Rectangle: " + width + " x " + height);
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
}

/**
 * Interview Point: Multiple Interface Implementation
 */
interface Phone {
    void call();
    void sendSMS();
}

interface Camera {
    void takePhoto();
    void recordVideo();
}

class SmartPhone implements Phone, Camera {
    @Override
    public void call() {
        System.out.println("  Making a call");
    }
    
    @Override
    public void sendSMS() {
        System.out.println("  Sending SMS");
    }
    
    @Override
    public void takePhoto() {
        System.out.println("  Taking photo");
    }
    
    @Override
    public void recordVideo() {
        System.out.println("  Recording video");
    }
}

/**
 * Interview Point: Default Methods (Java 8+)
 */
interface Vehicle {
    void start();
    void stop();
    
    // Interview Point: Default method - has implementation
    default void honk() {
        System.out.println("  Vehicle is honking (default method)");
    }
}

class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("  Car is starting");
    }
    
    @Override
    public void stop() {
        System.out.println("  Car is stopping");
    }
    
    // Interview Point: Can override default method
    @Override
    public void honk() {
        System.out.println("  Car is honking (overridden)");
    }
}

/**
 * Interview Point: Static Methods in Interface (Java 8+)
 */
interface MathOperations {
    // Interview Point: Static method in interface
    static void add(int a, int b) {
        System.out.println("  Addition: " + a + " + " + b + " = " + (a + b));
    }
    
    static void multiply(int a, int b) {
        System.out.println("  Multiplication: " + a + " * " + b + " = " + (a * b));
    }
}

/**
 * Interview Point: Interface Inheritance
 */
interface Device {
    void turnOn();
    void turnOff();
}

interface AdvancedDevice extends Device {
    void connect();
    void disconnect();
}

class SmartDevice implements AdvancedDevice {
    @Override
    public void turnOn() {
        System.out.println("  Device turned on");
    }
    
    @Override
    public void turnOff() {
        System.out.println("  Device turned off");
    }
    
    @Override
    public void connect() {
        System.out.println("  Device connected");
    }
    
    @Override
    public void disconnect() {
        System.out.println("  Device disconnected");
    }
}

/**
 * INTERVIEW SUMMARY: Interfaces
 * 
 * Key Points:
 * 1. Contract that class must follow
 * 2. All methods are public abstract (before Java 8)
 * 3. Can have constants (public static final)
 * 4. Class implements interface using 'implements'
 * 5. Supports multiple inheritance
 * 6. Java 8+: default methods, static methods
 * 
 * Interface vs Abstract Class:
 * - Interface: All methods abstract (before Java 8), multiple inheritance
 * - Abstract Class: Can have concrete methods, single inheritance
 * 
 * When to use:
 * - Interface: Define contract, multiple inheritance needed
 * - Abstract Class: Share code, partial implementation
 */

