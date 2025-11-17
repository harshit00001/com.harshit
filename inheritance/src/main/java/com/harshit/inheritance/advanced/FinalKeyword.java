package com.harshit.inheritance.advanced;

/**
 * FINAL KEYWORD IN INHERITANCE - Interview Explanation:
 * 
 * final: Keyword that prevents modification.
 * 
 * Technical Definition:
 * - final class: Cannot be extended
 * - final method: Cannot be overridden
 * - final variable: Cannot be reassigned
 * 
 * Simple Explanation:
 * - final class: "No one can inherit from this"
 * - final method: "No one can change this method"
 * - final variable: "This value cannot change"
 * 
 * Use Cases:
 * - Security: Prevent inheritance of sensitive classes
 * - Performance: Compiler optimizations
 * - Immutability: Prevent modification
 */
public class FinalKeyword {

    public static void main(String[] args) {
        System.out.println("=== FINAL KEYWORD IN INHERITANCE ===\n");
        
        demonstrateFinalClass();
        demonstrateFinalMethod();
        demonstrateFinalVariable();
    }
    
    /**
     * Interview Point: Final Class
     * 
     * Technical Explanation:
     * - A final class cannot be extended - no class can inherit from it
     * - This is useful for security, immutability, and performance
     * - Security: Prevents malicious code from extending and modifying behavior of critical classes
     * - Immutability: Classes like String are final to ensure they cannot be modified through inheritance
     * - Performance: Compiler can optimize final classes more aggressively (no need to check for overrides)
     * - Examples: String, Integer, Double, Math - all final classes in Java
     * - Once a class is final, the inheritance chain stops there
     * 
     * Simple Explanation:
     * - Think of final class as a "no inheritance allowed" sign
     * - It's like saying "this class is complete, don't try to extend it"
     * - Like a sealed box - you can use it, but you can't modify it or create variations
     * - Used for classes that should never be changed or extended
     * - Examples: String class is final - you can't create MyString extends String
     * 
     * Interview Answer:
     * "A final class cannot be extended. This is used for security (prevent modification),
     * immutability (like String class), and performance (compiler optimizations). Once a class
     * is final, no other class can inherit from it. Examples include String, Integer, and Math
     * classes. This ensures the class behavior cannot be changed through inheritance."
     */
    private static void demonstrateFinalClass() {
        System.out.println("--- Final Class ---");
        
        // Interview Point: Can create objects of final class
        // Final classes can be instantiated normally - they just can't be extended
        // You can use them, but you can't create child classes from them
        FinalClass finalObj = new FinalClass();
        finalObj.display();
        
        // Interview Point: Cannot extend final class
        // If we try to create a class that extends FinalClass, we get a compilation error
        // This is the whole point of final - it prevents inheritance
        // class Child extends FinalClass { } // Compilation error - cannot extend final class
        
        System.out.println("  → Final class cannot be extended - inheritance chain stops");
        System.out.println("  → Used for security, immutability, and performance");
        System.out.println("  → Examples: String, Integer, Double, Math classes are all final");
        System.out.println("  → Prevents modification of class behavior through inheritance");
        System.out.println();
    }
    
    /**
     * Interview Point: Final Method
     * 
     * Technical Explanation:
     * - A final method cannot be overridden in child classes
     * - The method implementation is fixed - child classes must use parent's version
     * - This is useful when you want to prevent child classes from changing critical behavior
     * - Performance: Compiler can inline final methods (optimization technique)
     * - Security: Prevents child classes from modifying important methods
     * - The method can still be inherited and used by child classes, just not overridden
     * - Only instance methods can be final - static methods cannot be overridden anyway
     * 
     * Simple Explanation:
     * - Think of final method as "this method is set in stone"
     * - Parent says "this method works this way, and you cannot change it"
     * - Child can use the method, but must use parent's implementation
     * - Like a rule that cannot be broken - "this is how we do it, no exceptions"
     * - Used for critical methods that should never be changed
     * 
     * Interview Answer:
     * "A final method cannot be overridden in child classes. The method implementation is fixed
     * and child classes must use the parent's version. This is useful for security (prevent
     * modification of critical methods) and performance (compiler can inline final methods).
     * Child classes can still inherit and use final methods, they just cannot provide their
     * own implementation."
     */
    private static void demonstrateFinalMethod() {
        System.out.println("--- Final Method ---");
        
        ParentClass parent = new ParentClass();
        parent.finalMethod(); // Can call - final method works normally
        parent.normalMethod(); // Can call - normal method
        
        ChildClass child = new ChildClass();
        // Interview Point: Calls parent's final method
        // Child inherits finalMethod() but cannot override it
        // So when we call finalMethod() on child, it uses parent's implementation
        child.finalMethod(); // Interview Point: Calls parent's final method (cannot be overridden)
        
        // Interview Point: Calls overridden method
        // normalMethod() is not final, so child can override it
        // Child provides its own implementation
        child.normalMethod(); // Interview Point: Calls overridden method (child's version)
        
        // Interview Point: Cannot override final method
        // In ChildClass, if we try to override finalMethod(), we get compilation error
        // This is the whole point - final methods cannot be changed
        
        System.out.println("  → Final method cannot be overridden - implementation is fixed");
        System.out.println("  → Child can use final method but must use parent's implementation");
        System.out.println("  → Used for security and performance (compiler optimizations)");
        System.out.println("  → Prevents modification of critical method behavior");
        System.out.println();
    }
    
    /**
     * Interview Point: Final Variable
     * 
     * Technical: Cannot be reassigned
     * Simple: "This value is constant"
     */
    private static void demonstrateFinalVariable() {
        System.out.println("--- Final Variable ---");
        
        FinalVariableExample example = new FinalVariableExample();
        System.out.println("  Final variable: " + example.FINAL_VALUE);
        System.out.println("  → Final variable cannot be reassigned");
        System.out.println("  → Must be initialized");
        System.out.println();
    }
}

/**
 * Interview Point: Final Class
 * 
 * Technical: Cannot be extended
 * Simple: No inheritance allowed
 */
final class FinalClass {
    public void display() {
        System.out.println("  FinalClass display method");
    }
}

// Interview Point: Cannot extend final class
// class Child extends FinalClass { } // Compilation error

/**
 * Interview Point: Final Method
 */
class ParentClass {
    // Interview Point: Final method - cannot be overridden
    public final void finalMethod() {
        System.out.println("  ParentClass finalMethod() - cannot be overridden");
    }
    
    // Interview Point: Normal method - can be overridden
    public void normalMethod() {
        System.out.println("  ParentClass normalMethod() - can be overridden");
    }
}

class ChildClass extends ParentClass {
    // Interview Point: Cannot override final method
    // @Override
    // public void finalMethod() { } // Compilation error
    
    // Interview Point: Can override normal method
    @Override
    public void normalMethod() {
        System.out.println("  ChildClass normalMethod() - overridden");
    }
}

/**
 * Interview Point: Final Variable
 */
class FinalVariableExample {
    // Interview Point: Final variable - must be initialized
    public final int FINAL_VALUE = 100;
    
    // Interview Point: Final variable can be initialized in constructor
    public final String name;
    
    public FinalVariableExample() {
        this.name = "Test"; // Must initialize final variable
    }
    
    // Interview Point: Cannot reassign final variable
    public void tryReassign() {
        // FINAL_VALUE = 200; // Compilation error
        // name = "New"; // Compilation error
    }
}

/**
 * INTERVIEW SUMMARY: Final Keyword
 * 
 * Key Points:
 * 1. final class: Cannot be extended
 * 2. final method: Cannot be overridden
 * 3. final variable: Cannot be reassigned
 * 
 * Use Cases:
 * - Security: Prevent inheritance
 * - Performance: Compiler optimizations
 * - Immutability: Prevent modification
 * 
 * Examples:
 * - String class is final
 * - Integer, Double, etc. are final
 * - Math class is final
 */

