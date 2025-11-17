package com.harshit.inheritance.advanced;

/**
 * INTERFACE EVOLUTION - Before and After Java 8
 * 
 * This file demonstrates how interfaces evolved in Java 8 and later versions.
 * 
 * BEFORE JAVA 8:
 * - All methods were public abstract (implicitly)
 * - Could only have constants (public static final)
 * - No method implementations
 * - No static methods
 * 
 * AFTER JAVA 8:
 * - Default methods (with implementation)
 * - Static methods (with implementation)
 * - Functional interfaces
 * 
 * JAVA 9+:
 * - Private methods in interfaces
 * - Private static methods
 */
public class InterfaceEvolution {

    public static void main(String[] args) {
        System.out.println("=== INTERFACE EVOLUTION ===\n");
        
        demonstrateBeforeJava8();
        demonstrateAfterJava8();
        demonstrateJava9Features();
        demonstrateFunctionalInterfaces();
    }
    
    /**
     * Interview Point: Before Java 8 Interface
     * 
     * Technical Definition (Before Java 8):
     * - All methods in an interface were implicitly public abstract - you didn't need to write
     *   these keywords, but they were always there
     * - Interfaces could NOT have any method implementations - only method signatures (declarations)
     * - Interfaces could only have constants - fields that were implicitly public static final
     * - No static methods were allowed in interfaces
     * - No default methods were allowed - every method had to be implemented by the implementing class
     * - Interfaces were pure contracts - they defined what a class must do, but not how
     * - This made interfaces very rigid - if you wanted to add a method to an interface, all
     *   implementing classes had to be updated, which could break existing code
     * 
     * Simple Explanation:
     * - Think of interfaces before Java 8 as a contract or agreement
     * - The interface says "you must have these methods" but doesn't say how to implement them
     * - It's like a job description - lists what you need to do, but doesn't tell you how
     * - Every class that implements the interface must provide its own implementation for every method
     * - No shared code - each implementing class writes everything from scratch
     * - Very strict - if interface changes, all implementing classes must change too
     * 
     * Interview Answer:
     * "Before Java 8, interfaces were pure contracts. All methods were implicitly public abstract
     * with no implementations. Interfaces could only have constants (public static final fields).
     * There were no static methods or default methods. Every implementing class had to provide
     * implementation for every method. This made interfaces rigid - adding a method to an interface
     * required updating all implementing classes, which could break existing code. Interfaces
     * defined 'what' but not 'how'."
     */
    private static void demonstrateBeforeJava8() {
        System.out.println("--- BEFORE JAVA 8 ---");
        System.out.println();
        System.out.println("  Interface Characteristics:");
        System.out.println("    → All methods are public abstract (implicitly)");
        System.out.println("    → Cannot have method implementations - only signatures");
        System.out.println("    → Can only have constants (public static final)");
        System.out.println("    → No static methods allowed");
        System.out.println("    → No default methods allowed");
        System.out.println("    → Pure contract - defines 'what', not 'how'");
        System.out.println("    → Every implementing class must implement all methods");
        System.out.println("    → Adding method to interface breaks all implementing classes");
        System.out.println();
        
        // Interview Point: Old-style interface (still valid)
        // This interface follows the pre-Java 8 style - only abstract methods and constants
        // It's still valid in Java 8+, but now you can also use default and static methods
        OldStyleInterface oldImpl = new OldStyleImplementation();
        oldImpl.method1();
        oldImpl.method2();
        System.out.println("  Constant: " + OldStyleInterface.CONSTANT);
        System.out.println("  → This style is still valid, but Java 8+ offers more flexibility");
        System.out.println();
    }
    
    /**
     * Interview Point: After Java 8 Interface
     * 
     * Technical Definition (After Java 8):
     * - Default methods: Methods with implementation marked with 'default' keyword
     *   - Provide default behavior that implementing classes can use or override
     *   - Allow adding new methods to interfaces without breaking existing implementations
     *   - Implementing classes automatically inherit default methods
     *   - Can be overridden by implementing classes if needed
     * - Static methods: Methods with implementation marked with 'static' keyword
     *   - Belong to the interface itself, not to implementing classes
     *   - Called using interface name: InterfaceName.staticMethod()
     *   - Cannot be overridden (similar to static methods in classes)
     *   - Useful for utility methods related to the interface
     * - Still can have abstract methods: Interfaces can still have abstract methods
     * - Still can have constants: All previous features still work
     * - Backward compatible: Old code written for pre-Java 8 interfaces still works
     * 
     * Simple Explanation:
     * - Think of interfaces after Java 8 as contracts that can also provide some default behavior
     * - Default methods are like "here's a default way to do this, but you can change it if you want"
     * - It's like a job description that also includes "here's how we usually do it, but feel free
     *   to do it differently if needed"
     * - Static methods are like utility functions that belong to the interface itself
     * - This makes interfaces more flexible - you can add new methods without breaking old code
     * - Implementing classes can use default methods as-is, or override them with their own implementation
     * 
     * Interview Answer:
     * "After Java 8, interfaces can have default methods and static methods with implementations.
     * Default methods provide default behavior that implementing classes inherit automatically.
     * They can be overridden if needed. This allows adding new methods to interfaces without
     * breaking existing implementations - a major improvement for backward compatibility. Static
     * methods belong to the interface itself and are called using the interface name. This makes
     * interfaces more flexible while maintaining backward compatibility with pre-Java 8 code."
     */
    private static void demonstrateAfterJava8() {
        System.out.println("--- AFTER JAVA 8 ---");
        System.out.println();
        System.out.println("  New Features:");
        System.out.println("    → Default methods (with implementation) - marked with 'default' keyword");
        System.out.println("    → Static methods (with implementation) - marked with 'static' keyword");
        System.out.println("    → Can still have abstract methods (backward compatible)");
        System.out.println("    → Can still have constants (backward compatible)");
        System.out.println("    → Backward compatible - old code still works");
        System.out.println("    → Allows evolution of interfaces without breaking existing code");
        System.out.println();
        
        ModernInterface modernImpl = new ModernImplementation();
        modernImpl.abstractMethod();     // Must implement - this is still required (abstract method)
        modernImpl.defaultMethod();      // Interview Point: Can use default - inherited automatically
        modernImpl.overriddenDefault();  // Interview Point: Can override default - provides own implementation
        
        // Interview Point: Static method called using interface name
        // Static methods belong to the interface, not to implementing classes
        // You call them using the interface name, not an object reference
        // This is similar to static methods in classes
        ModernInterface.staticMethod();
        System.out.println("  → Default methods: Provide default behavior, can be overridden");
        System.out.println("  → Static methods: Belong to interface, called with interface name");
        System.out.println("  → Backward compatible: Old interfaces and code still work");
        System.out.println();
    }
    
    /**
     * Interview Point: Java 9+ Features
     * 
     * Technical Definition (Java 9+):
     * - Private methods in interfaces
     * - Private static methods
     * - Used to share code between default methods
     * 
     * Simple Explanation:
     * - Can have private helper methods
     * - Reduces code duplication
     * - Only accessible within interface
     */
    private static void demonstrateJava9Features() {
        System.out.println("--- JAVA 9+ FEATURES ---");
        System.out.println();
        System.out.println("  New Features:");
        System.out.println("    → Private methods");
        System.out.println("    → Private static methods");
        System.out.println("    → Used for code reuse in default methods");
        System.out.println();
        
        Java9Interface java9Impl = new Java9Implementation();
        java9Impl.method1();
        java9Impl.method2();
        System.out.println();
    }
    
    /**
     * Interview Point: Functional Interfaces
     * 
     * Technical Definition:
     * - Interface with exactly one abstract method
     * - Can have multiple default/static methods
     * - Can be used with lambda expressions
     * - @FunctionalInterface annotation (optional but recommended)
     * 
     * Simple Explanation:
     * - Interface with only one method to implement
     * - Perfect for lambda expressions
     * - Makes code more concise
     */
    private static void demonstrateFunctionalInterfaces() {
        System.out.println("--- FUNCTIONAL INTERFACES (Java 8+) ---");
        System.out.println();
        System.out.println("  Characteristics:");
        System.out.println("    → Exactly one abstract method");
        System.out.println("    → Can have default/static methods");
        System.out.println("    → Can use lambda expressions");
        System.out.println("    → @FunctionalInterface annotation");
        System.out.println();
        
        // Interview Point: Using functional interface with lambda
        Calculator add = (a, b) -> a + b;
        Calculator multiply = (a, b) -> a * b;
        
        System.out.println("  Lambda with functional interface:");
        System.out.println("    add(5, 3) = " + add.calculate(5, 3));
        System.out.println("    multiply(4, 6) = " + multiply.calculate(4, 6));
        System.out.println();
    }
}

// ==================== BEFORE JAVA 8 ====================

/**
 * Interview Point: Old-style Interface (Before Java 8)
 * 
 * All methods are implicitly public abstract
 * Cannot have implementations
 */
interface OldStyleInterface {
    // Interview Point: Constant (implicitly public static final)
    String CONSTANT = "OLD_STYLE_CONSTANT";
    
    // Interview Point: Abstract method (implicitly public abstract)
    void method1();
    
    // Interview Point: Abstract method (implicitly public abstract)
    void method2();
    
    // Interview Point: Cannot have implementations
    // void method3() { } // Compilation error before Java 8
}

class OldStyleImplementation implements OldStyleInterface {
    @Override
    public void method1() {
        System.out.println("  OldStyle: method1 implementation");
    }
    
    @Override
    public void method2() {
        System.out.println("  OldStyle: method2 implementation");
    }
}

// ==================== AFTER JAVA 8 ====================

/**
 * Interview Point: Modern Interface (After Java 8)
 * 
 * Can have default methods and static methods
 */
interface ModernInterface {
    // Interview Point: Abstract method (still supported)
    void abstractMethod();
    
    // Interview Point: Default method (Java 8+) - has implementation
    default void defaultMethod() {
        System.out.println("  Modern: default method implementation");
    }
    
    // Interview Point: Static method (Java 8+) - belongs to interface
    static void staticMethod() {
        System.out.println("  Modern: static method in interface");
    }
    
    // Interview Point: Can have multiple default methods
    default void anotherDefaultMethod() {
        System.out.println("  Modern: another default method");
    }
}

class ModernImplementation implements ModernInterface {
    // Interview Point: Must implement abstract method
    @Override
    public void abstractMethod() {
        System.out.println("  Modern: abstractMethod implementation");
    }
    
    // Interview Point: Can override default method (optional)
    @Override
    public void defaultMethod() {
        System.out.println("  Modern: overridden default method");
    }
    
    // Interview Point: Can use other default methods
    public void useDefault() {
        anotherDefaultMethod(); // Can call default method
    }
}

// ==================== JAVA 9+ ====================

/**
 * Interview Point: Java 9+ Interface
 * 
 * Can have private methods for code reuse
 */
interface Java9Interface {
    // Interview Point: Default methods can share code via private methods
    default void method1() {
        System.out.println("  Java9: method1");
        privateHelper(); // Interview Point: Call private method
    }
    
    default void method2() {
        System.out.println("  Java9: method2");
        privateHelper(); // Interview Point: Reuse private method
    }
    
    // Interview Point: Private method (Java 9+) - only accessible within interface
    private void privateHelper() {
        System.out.println("    → Private helper method (Java 9+)");
    }
    
    // Interview Point: Private static method (Java 9+)
    private static void privateStaticHelper() {
        System.out.println("    → Private static helper (Java 9+)");
    }
}

class Java9Implementation implements Java9Interface {
    // Interview Point: Only need to implement if there are abstract methods
    // Default methods are inherited automatically
}

// ==================== FUNCTIONAL INTERFACE ====================

/**
 * Interview Point: Functional Interface (Java 8+)
 * 
 * Exactly one abstract method
 * Can use with lambda expressions
 */
@FunctionalInterface
interface Calculator {
    // Interview Point: Single abstract method
    int calculate(int a, int b);
    
    // Interview Point: Can have default methods
    default void printResult(int result) {
        System.out.println("  Result: " + result);
    }
    
    // Interview Point: Can have static methods
    static Calculator getAdder() {
        return (a, b) -> a + b;
    }
    
    // Interview Point: Cannot have another abstract method
    // void anotherMethod(); // Would break functional interface
}

/**
 * INTERVIEW SUMMARY: Interface Evolution
 * 
 * BEFORE JAVA 8:
 * - All methods public abstract (implicitly)
 * - No implementations
 * - Only constants
 * - Pure contract
 * 
 * AFTER JAVA 8:
 * - Default methods (with implementation)
 * - Static methods (with implementation)
 * - Functional interfaces
 * - Backward compatible
 * 
 * JAVA 9+:
 * - Private methods
 * - Private static methods
 * - Code reuse in interfaces
 * 
 * Key Benefits:
 * - Backward compatibility
 * - Code reuse
 * - Evolution without breaking existing code
 * - Support for lambda expressions
 */

