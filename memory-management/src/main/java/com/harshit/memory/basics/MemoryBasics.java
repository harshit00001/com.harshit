package com.harshit.memory.basics;

/**
 * MEMORY MANAGEMENT BASICS - Interview Explanation:
 * 
 * Memory Management: How Java allocates and manages memory for your program.
 * 
 * Java Memory Structure:
 * 
 * 1. HEAP MEMORY:
 *    - Where objects are stored
 *    - Shared by all threads
 *    - Managed by Garbage Collector
 *    - Divided into: Young Generation and Old Generation
 * 
 * 2. STACK MEMORY:
 *    - Where method calls and local variables are stored
 *    - One stack per thread
 *    - Fast access, limited size
 *    - Stores primitive types and object references
 * 
 * 3. METHOD AREA (Metaspace in Java 8+):
 *    - Stores class metadata
 *    - Stores static variables
 *    - Stores method code
 * 
 * Simple Explanation:
 * - Heap: Big warehouse where objects live (shared by everyone)
 * - Stack: Personal workspace for each thread (fast, small)
 * - Metaspace: Library of class information
 */
public class MemoryBasics {

    public static void main(String[] args) {
        System.out.println("=== MEMORY MANAGEMENT BASICS ===\n");
        
        demonstrateHeapMemory();
        demonstrateStackMemory();
        demonstrateMemoryAllocation();
        demonstrateReferenceTypes();
        demonstrateMemoryComparison();
    }
    
    /**
     * Interview Point: HEAP MEMORY
     * 
     * Technical Definition:
     * - Runtime data area from which memory for objects is allocated
     * - Created when JVM starts, shared by all threads
     * - Objects are allocated here
     * - Managed by Garbage Collector
     * 
     * Simple Explanation:
     * - Big storage area where all objects live
     * - Like a warehouse shared by everyone
     * - Objects stay here until garbage collected
     */
    private static void demonstrateHeapMemory() {
        System.out.println("--- HEAP MEMORY ---");
        
        // Interview Point: Objects are created in HEAP
        Person person1 = new Person("John", 25);
        Person person2 = new Person("Jane", 30);
        
        System.out.println("  Created objects in HEAP:");
        System.out.println("    person1: " + person1);
        System.out.println("    person2: " + person2);
        
        // Interview Point: References are stored in STACK, objects in HEAP
        System.out.println("  → person1 and person2 are references (in STACK)");
        System.out.println("  → Actual Person objects are in HEAP");
        System.out.println("  → HEAP is shared by all threads");
        System.out.println("  → HEAP is managed by Garbage Collector\n");
    }
    
    /**
     * Interview Point: STACK MEMORY
     * 
     * Technical Definition:
     * - Stores method calls and local variables
     * - One stack per thread
     * - Stores primitive types and object references
     * - Fast access, limited size (usually 1-2 MB per thread)
     * - Automatically cleaned when method exits
     * 
     * Simple Explanation:
     * - Personal workspace for each thread
     * - Stores method calls (like a call stack)
     * - Stores local variables
     * - Very fast but limited space
     */
    private static void demonstrateStackMemory() {
        System.out.println("--- STACK MEMORY ---");
        
        // Interview Point: Primitive types stored in STACK
        int age = 25;           // Stored in STACK
        double salary = 50000;  // Stored in STACK
        boolean isActive = true; // Stored in STACK
        
        System.out.println("  Primitive variables in STACK:");
        System.out.println("    age: " + age);
        System.out.println("    salary: " + salary);
        System.out.println("    isActive: " + isActive);
        
        // Interview Point: Object references stored in STACK
        Person person = new Person("Bob", 28);
        // 'person' reference is in STACK
        // Person object is in HEAP
        
        System.out.println("  → Primitive types: stored directly in STACK");
        System.out.println("  → Object references: stored in STACK");
        System.out.println("  → Actual objects: stored in HEAP");
        System.out.println("  → STACK is per-thread (each thread has its own)");
        System.out.println("  → STACK is automatically cleaned when method exits\n");
        
        // Interview Point: Method calls create stack frames
        callMethod();
    }
    
    private static void callMethod() {
        // Interview Point: Each method call creates a stack frame
        // Stack frame contains:
        // - Local variables
        // - Method parameters
        // - Return address
        int localVar = 10; // Stored in this method's stack frame
        System.out.println("    → Method call creates stack frame in STACK");
        System.out.println("    → Stack frame contains local variables");
    }
    
    /**
     * Interview Point: Memory Allocation
     * How memory is allocated for different types
     */
    private static void demonstrateMemoryAllocation() {
        System.out.println("--- MEMORY ALLOCATION ---");
        
        // Interview Point: Primitive allocation in STACK
        int number = 42;
        System.out.println("  Primitive (int):");
        System.out.println("    → Allocated in STACK");
        System.out.println("    → Size: 4 bytes");
        System.out.println("    → Directly stores value: " + number);
        
        // Interview Point: Object allocation in HEAP
        String text = new String("Hello");
        System.out.println("  Object (String):");
        System.out.println("    → Reference 'text' in STACK");
        System.out.println("    → String object in HEAP");
        System.out.println("    → Reference points to HEAP object");
        
        // Interview Point: Array allocation
        int[] numbers = new int[5];
        System.out.println("  Array:");
        System.out.println("    → Reference 'numbers' in STACK");
        System.out.println("    → Array object in HEAP");
        System.out.println("    → Array elements stored in HEAP");
        
        System.out.println();
    }
    
    /**
     * Interview Point: Reference Types
     * How references work in memory
     */
    private static void demonstrateReferenceTypes() {
        System.out.println("--- REFERENCE TYPES ---");
        
        // Interview Point: Reference points to object in HEAP
        Person person1 = new Person("Alice", 25);
        System.out.println("  person1 reference → points to Person object in HEAP");
        
        // Interview Point: Multiple references can point to same object
        Person person2 = person1; // Both point to same object
        System.out.println("  person2 = person1 → both references point to same object");
        System.out.println("  → Changing person1 affects person2 (same object)");
        
        person1.setAge(30);
        System.out.println("  Changed person1.age to 30");
        System.out.println("  person2.age is also: " + person2.getAge());
        
        // Interview Point: New object creates new HEAP allocation
        Person person3 = new Person("Bob", 35);
        System.out.println("  person3 → new Person object in HEAP (different object)");
        
        System.out.println();
    }
    
    /**
     * Interview Point: Heap vs Stack Comparison
     */
    private static void demonstrateMemoryComparison() {
        System.out.println("--- HEAP vs STACK COMPARISON ---");
        System.out.println();
        System.out.println("  HEAP MEMORY:");
        System.out.println("    ✓ Stores objects");
        System.out.println("    ✓ Shared by all threads");
        System.out.println("    ✓ Managed by Garbage Collector");
        System.out.println("    ✓ Slower access");
        System.out.println("    ✓ Larger size (can be GBs)");
        System.out.println("    ✓ Objects stay until GC");
        System.out.println();
        System.out.println("  STACK MEMORY:");
        System.out.println("    ✓ Stores primitives and references");
        System.out.println("    ✓ One per thread");
        System.out.println("    ✓ Automatically cleaned");
        System.out.println("    ✓ Faster access");
        System.out.println("    ✓ Limited size (1-2 MB)");
        System.out.println("    ✓ Cleared when method exits");
        System.out.println();
    }
}

/**
 * Interview Point: Person class for demonstration
 */
class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public int getAge() {
        return age;
    }
    
    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

/**
 * INTERVIEW SUMMARY: Memory Basics
 * 
 * Key Points:
 * 1. HEAP: Objects, shared, GC managed, larger, slower
 * 2. STACK: Primitives/references, per-thread, auto-cleaned, smaller, faster
 * 3. References in STACK point to objects in HEAP
 * 4. Primitive types stored directly in STACK
 * 5. Objects always stored in HEAP
 */

