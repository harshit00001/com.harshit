package com.harshit.memory.stack;

/**
 * STACK MEMORY DETAILS - Interview Explanation:
 * 
 * Stack Memory:
 * 
 * Technical Definition:
 * - Stores method calls and local variables
 * - One stack per thread
 * - Stores primitive types and object references
 * - Fast access, limited size (usually 1-2 MB per thread)
 * - LIFO (Last In First Out) structure
 * - Automatically cleaned when method exits
 * 
 * Simple Explanation:
 * - Personal workspace for each thread
 * - Like a stack of plates (last one added is first one removed)
 * - Stores method calls (call stack)
 * - Stores local variables
 * - Very fast but limited space
 * 
 * Stack Frame:
 * - Created for each method call
 * - Contains: local variables, parameters, return address
 * - Destroyed when method returns
 */
public class StackMemoryDetails {

    public static void main(String[] args) {
        System.out.println("=== STACK MEMORY DETAILS ===\n");
        
        explainStackStructure();
        demonstrateStackFrames();
        demonstrateLocalVariables();
        demonstrateStackOverflow();
        demonstrateStackSettings();
    }
    
    /**
     * Interview Point: Stack Structure
     * 
     * Technical Definition:
     * - LIFO (Last In First Out) data structure
     * - Each thread has its own stack
     * - Each method call creates a stack frame
     * - Stack frame pushed when method called
     * - Stack frame popped when method returns
     * 
     * Simple Explanation:
     * - Like a stack of plates
     * - Add to top (push), remove from top (pop)
     * - Each method call adds a plate
     * - When method returns, plate is removed
     */
    private static void explainStackStructure() {
        System.out.println("--- STACK STRUCTURE ---");
        System.out.println();
        System.out.println("  Stack (LIFO - Last In First Out):");
        System.out.println("    ┌─────────────┐");
        System.out.println("    │ method3()   │ ← Top (most recent call)");
        System.out.println("    ├─────────────┤");
        System.out.println("    │ method2()   │");
        System.out.println("    ├─────────────┤");
        System.out.println("    │ method1()   │");
        System.out.println("    ├─────────────┤");
        System.out.println("    │ main()      │ ← Bottom (first call)");
        System.out.println("    └─────────────┘");
        System.out.println();
        System.out.println("  Key Points:");
        System.out.println("    → One stack per thread");
        System.out.println("    → Each method call = one stack frame");
        System.out.println("    → Last method called = first to return");
        System.out.println("    → Limited size (1-2 MB default)");
        System.out.println();
    }
    
    /**
     * Interview Point: Stack Frames
     * What's inside a stack frame
     */
    private static void demonstrateStackFrames() {
        System.out.println("--- STACK FRAMES ---");
        System.out.println();
        System.out.println("  Each Stack Frame Contains:");
        System.out.println("    ┌─────────────────────┐");
        System.out.println("    │ Local Variables      │");
        System.out.println("    │ Method Parameters    │");
        System.out.println("    │ Return Address       │");
        System.out.println("    │ Reference to 'this'  │");
        System.out.println("    └─────────────────────┘");
        System.out.println();
        
        // Interview Point: Demonstrate stack frames
        System.out.println("  Example Call Stack:");
        System.out.println("    main() calls method1()");
        method1();
    }
    
    private static void method1() {
        System.out.println("    → method1() stack frame created");
        System.out.println("    → method1() calls method2()");
        method2();
        System.out.println("    → method1() returns (stack frame removed)");
    }
    
    private static void method2() {
        int localVar = 10; // Stored in method2's stack frame
        System.out.println("    → method2() stack frame created");
        System.out.println("    → localVar stored in method2's stack frame");
        System.out.println("    → method2() returns (stack frame removed)");
    }
    
    /**
     * Interview Point: Local Variables in Stack
     * How local variables are stored
     */
    private static void demonstrateLocalVariables() {
        System.out.println();
        System.out.println("--- LOCAL VARIABLES ---");
        System.out.println();
        
        // Interview Point: Primitive types stored directly in stack
        int number = 42;
        double price = 99.99;
        boolean isActive = true;
        
        System.out.println("  Primitive Types (stored directly in stack):");
        System.out.println("    int number = 42;");
        System.out.println("    → Value 42 stored directly in stack frame");
        System.out.println("    → Size: 4 bytes");
        System.out.println();
        
        // Interview Point: Object references stored in stack
        String text = new String("Hello");
        System.out.println("  Object Reference (stored in stack):");
        System.out.println("    String text = new String(\"Hello\");");
        System.out.println("    → Reference 'text' stored in stack");
        System.out.println("    → Reference points to object in HEAP");
        System.out.println("    → Reference size: 4-8 bytes (depending on JVM)");
        System.out.println();
        
        // Interview Point: Arrays
        int[] numbers = new int[5];
        System.out.println("  Array:");
        System.out.println("    int[] numbers = new int[5];");
        System.out.println("    → Reference 'numbers' stored in stack");
        System.out.println("    → Array object stored in HEAP");
        System.out.println("    → Array elements stored in HEAP");
        System.out.println();
    }
    
    /**
     * Interview Point: Stack Overflow
     * What happens when stack is full
     */
    private static void demonstrateStackOverflow() {
        System.out.println("--- STACK OVERFLOW ---");
        System.out.println();
        System.out.println("  Stack Overflow occurs when:");
        System.out.println("    → Too many method calls (deep recursion)");
        System.out.println("    → Stack size exceeds limit");
        System.out.println("    → Throws StackOverflowError");
        System.out.println();
        System.out.println("  Example (commented to prevent crash):");
        System.out.println("    void recursiveMethod() {");
        System.out.println("        recursiveMethod(); // Calls itself");
        System.out.println("    }");
        System.out.println("    → Each call adds stack frame");
        System.out.println("    → Eventually stack overflows");
        System.out.println();
        System.out.println("  Prevention:");
        System.out.println("    → Use iteration instead of deep recursion");
        System.out.println("    → Increase stack size: -Xss2m");
        System.out.println();
        
        // Safe demonstration
        try {
            recursiveMethod(0);
        } catch (StackOverflowError e) {
            System.out.println("  → StackOverflowError caught (expected)");
        }
    }
    
    private static void recursiveMethod(int depth) {
        if (depth > 1000) {
            return; // Prevent actual overflow in demo
        }
        recursiveMethod(depth + 1);
    }
    
    /**
     * Interview Point: Stack Settings
     * How to configure stack size
     */
    private static void demonstrateStackSettings() {
        System.out.println();
        System.out.println("--- STACK SETTINGS ---");
        System.out.println();
        System.out.println("  JVM Flags for Stack:");
        System.out.println();
        System.out.println("  -Xss1m");
        System.out.println("    → Stack size per thread: 1 MB");
        System.out.println("    → Default is usually 1-2 MB");
        System.out.println();
        System.out.println("  -Xss2m");
        System.out.println("    → Stack size per thread: 2 MB");
        System.out.println("    → Use for deep recursion");
        System.out.println();
        System.out.println("  Example:");
        System.out.println("    java -Xss2m MyApp");
        System.out.println("    → Each thread gets 2 MB stack");
        System.out.println();
        System.out.println("  Note:");
        System.out.println("    → More threads = more total stack memory");
        System.out.println("    → 100 threads × 1MB = 100 MB total stack");
        System.out.println();
    }
}

/**
 * INTERVIEW SUMMARY: Stack Memory
 * 
 * Key Points:
 * 1. One stack per thread
 * 2. Stores method calls (call stack)
 * 3. Stores local variables and parameters
 * 4. LIFO structure (Last In First Out)
 * 5. Limited size (1-2 MB default)
 * 6. Fast access
 * 7. Automatically cleaned when method exits
 * 
 * Stack Frame Contains:
 * - Local variables
 * - Method parameters
 * - Return address
 * - Reference to 'this'
 * 
 * Common Issues:
 * - StackOverflowError: Too many method calls
 * - Solution: Increase stack size or use iteration
 */

