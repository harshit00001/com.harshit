package com.harshit.inheritance.advanced;

/**
 * METHOD OVERRIDING - Interview Explanation:
 * 
 * Method Overriding: Child class provides its own implementation of a method
 * that is already defined in parent class.
 * 
 * Technical Definition:
 * - Runtime polymorphism
 * - Method signature must match parent class
 * - @Override annotation (recommended)
 * - Access modifier can be same or more accessible
 * - Return type must be same or covariant
 * 
 * Simple Explanation:
 * - Child class says "I'll do it my way"
 * - Replaces parent's method with its own version
 * - Method name and parameters must be same
 * 
 * Rules:
 * 1. Method name must be same
 * 2. Parameters must be same
 * 3. Return type must be same or covariant
 * 4. Access modifier can't be more restrictive
 * 5. Can't override static, final, or private methods
 */
public class MethodOverriding {

    public static void main(String[] args) {
        System.out.println("=== METHOD OVERRIDING ===\n");
        
        demonstrateBasicOverriding();
        demonstrateRuntimePolymorphism();
        demonstrateOverrideAnnotation();
        demonstrateAccessModifierRules();
        demonstrateCovariantReturnType();
    }
    
    /**
     * Interview Point: Basic Method Overriding
     * 
     * Technical Explanation:
     * - Method overriding occurs when a child class provides its own implementation of a method
     *   that is already defined in the parent class
     * - The method signature (name and parameters) must be exactly the same in both parent and child
     * - The return type must be the same or a subtype (covariant return type)
     * - The access modifier can be the same or more accessible (but not less accessible)
     * - This is runtime polymorphism - the method to be called is determined at runtime based on
     *   the actual object type, not the reference type
     * 
     * Simple Explanation:
     * - Think of overriding like a child saying "I'll do it my way" instead of following parent's way
     * - Parent has a method, child replaces it with its own version
     * - The method name and parameters must match exactly
     * - When you call the method on a child object, it uses the child's version, not the parent's
     * 
     * Interview Answer:
     * "Method overriding allows a child class to provide its own implementation of a method
     * that exists in the parent class. The method signature must match exactly. When you call
     * the method on a child object, the child's version is executed, not the parent's. This
     * is runtime polymorphism - the JVM decides which method to call based on the actual
     * object type at runtime."
     */
    private static void demonstrateBasicOverriding() {
        System.out.println("--- Basic Method Overriding ---");
        
        // Interview Point: Parent class object calls parent's method
        // When we create a Vehicle object and call start(), it uses Vehicle's implementation
        Vehicle vehicle = new Vehicle();
        vehicle.start(); // Parent class method - prints "Vehicle is starting"
        
        // Interview Point: Child class object calls child's overridden method
        // When we create a Car object (which extends Vehicle), it has overridden the start() method
        // So when we call start() on Car, it uses Car's implementation, not Vehicle's
        // This is the key point of overriding - child's version replaces parent's version
        Car car = new Car();
        car.start(); // Interview Point: Overridden method in child - prints "Car is starting with engine"
        // Notice that Car's start() method has different behavior than Vehicle's start() method
        // This is method overriding in action - same method name, different implementation
        
        System.out.println();
    }
    
    /**
     * Interview Point: Runtime Polymorphism
     * 
     * Technical Explanation:
     * - Runtime polymorphism (also called dynamic method dispatch) means the method to be called
     *   is determined at runtime, not at compile time
     * - The decision of which method to execute is based on the actual object type (what object
     *   was created), not the reference type (what variable type is used)
     * - This is achieved through method overriding - when a parent reference points to a child object,
     *   the child's overridden method is called, not the parent's method
     * - The JVM uses a virtual method table (vtable) to determine which method to call at runtime
     * 
     * Simple Explanation:
     * - Think of it like this: You have a reference that says "Vehicle" but the actual object is a "Car"
     * - When you call a method, Java looks at what the object actually is (Car), not what the
     *   reference says (Vehicle)
     * - So even though the variable type is Vehicle, if the actual object is Car, Car's method runs
     * - This is like having a label that says "Animal" but the actual thing is a "Dog" - when it
     *   makes a sound, it barks (Dog's sound), not makes a generic animal sound
     * 
     * Interview Answer:
     * "Runtime polymorphism means the method to be called is determined at runtime based on the
     * actual object type, not the reference type. For example, if you have 'Vehicle v = new Car()',
     * and you call v.start(), it will call Car's start() method, not Vehicle's, because the actual
     * object is a Car. This is different from compile-time polymorphism (method overloading) where
     * the decision is made at compile time based on method parameters."
     */
    private static void demonstrateRuntimePolymorphism() {
        System.out.println("--- Runtime Polymorphism ---");
        
        // Interview Point: Reference type is Vehicle, object type is Car
        // This is a crucial concept: the reference variable 'vehicle1' is of type Vehicle,
        // but the actual object created is of type Car. This is called "upcasting" - treating
        // a child object as a parent type
        Vehicle vehicle1 = new Car();
        // Interview Point: Calls Car's start() method, not Vehicle's
        // Even though vehicle1 is declared as Vehicle type, when we call start(), the JVM
        // looks at the actual object type (Car) and calls Car's overridden start() method
        // This is runtime polymorphism - the decision is made at runtime, not compile time
        vehicle1.start(); // Interview Point: Calls Car's start() method - prints "Car is starting with engine"
        
        // Interview Point: Reference type is Vehicle, object type is Bike
        // Same concept - reference is Vehicle, but actual object is Bike
        Vehicle vehicle2 = new Bike();
        // Interview Point: Calls Bike's start() method, not Vehicle's
        // Again, even though vehicle2 is Vehicle type, Bike's start() method is called
        // because the actual object is a Bike
        vehicle2.start(); // Interview Point: Calls Bike's start() method - prints "Bike is starting with kick"
        
        System.out.println("  → Method called depends on actual object type");
        System.out.println("  → This is runtime polymorphism - decision made at runtime");
        System.out.println("  → JVM uses virtual method table to find correct method");
        System.out.println("  → Different from compile-time polymorphism (overloading)");
        System.out.println();
    }
    
    /**
     * Interview Point: @Override Annotation
     * 
     * Technical: Helps compiler verify overriding
     * Simple: Tells compiler "I'm overriding a method"
     */
    private static void demonstrateOverrideAnnotation() {
        System.out.println("--- @Override Annotation ---");
        
        Car car = new Car();
        car.start(); // Uses @Override annotation
        
        System.out.println("  → @Override ensures method is actually overriding");
        System.out.println("  → Compiler error if method doesn't override anything");
        System.out.println();
    }
    
    /**
     * Interview Point: Access Modifier Rules
     * 
     * Technical: Overriding method can't be more restrictive
     * Simple: Can make it more accessible, but not less
     */
    private static void demonstrateAccessModifierRules() {
        System.out.println("--- Access Modifier Rules ---");
        
        Parent parent = new Parent();
        parent.display(); // Protected in parent
        
        Child child = new Child();
        child.display(); // Public in child (more accessible - allowed)
        
        System.out.println("  → Overriding method can be more accessible");
        System.out.println("  → Cannot be less accessible (compilation error)");
        System.out.println();
    }
    
    /**
     * Interview Point: Covariant Return Type
     * 
     * Technical: Return type can be subclass of parent's return type
     * Simple: Can return more specific type
     */
    private static void demonstrateCovariantReturnType() {
        System.out.println("--- Covariant Return Type ---");
        
        ParentClass parent = new ParentClass();
        Object obj = parent.getObject(); // Returns Object
        
        ChildClass child = new ChildClass();
        String str = child.getObject(); // Interview Point: Returns String (subclass of Object)
        
        System.out.println("  → Parent returns: " + obj.getClass().getSimpleName());
        System.out.println("  → Child returns: " + str.getClass().getSimpleName());
        System.out.println("  → Covariant return type allows more specific return type");
        System.out.println();
    }
}

/**
 * Interview Point: Parent class for overriding example
 */
class Vehicle {
    public void start() {
        System.out.println("  Vehicle is starting");
    }
    
    public void stop() {
        System.out.println("  Vehicle is stopping");
    }
}

/**
 * Interview Point: Child class overriding parent method
 */
class Car extends Vehicle {
    // Interview Point: @Override annotation (recommended)
    @Override
    public void start() {
        System.out.println("  Car is starting with engine");
    }
    
    // Interview Point: Can also override stop method
    @Override
    public void stop() {
        System.out.println("  Car is stopping with brakes");
    }
}

/**
 * Interview Point: Another child class with different override
 */
class Bike extends Vehicle {
    @Override
    public void start() {
        System.out.println("  Bike is starting with kick");
    }
}

/**
 * Interview Point: Access modifier example
 */
class Parent {
    protected void display() {
        System.out.println("  Parent display (protected)");
    }
}

class Child extends Parent {
    // Interview Point: Can make it more accessible (public)
    @Override
    public void display() {
        System.out.println("  Child display (public - more accessible)");
    }
    
    // Interview Point: Cannot make it less accessible
    // @Override
    // private void display() { } // Compilation error - can't be more restrictive
}

/**
 * Interview Point: Covariant return type example
 */
class ParentClass {
    public Object getObject() {
        return new Object();
    }
}

class ChildClass extends ParentClass {
    // Interview Point: Can return subclass of Object (String)
    @Override
    public String getObject() {
        return "Hello";
    }
}

/**
 * INTERVIEW SUMMARY: Method Overriding
 * 
 * Key Points:
 * 1. Runtime polymorphism
 * 2. Method signature must match
 * 3. @Override annotation recommended
 * 4. Access modifier can't be more restrictive
 * 5. Return type can be covariant
 * 6. Can't override static, final, or private methods
 * 
 * Rules:
 * - Same method name
 * - Same parameters
 * - Same or covariant return type
 * - Same or more accessible modifier
 * - Must be instance method (not static)
 */

