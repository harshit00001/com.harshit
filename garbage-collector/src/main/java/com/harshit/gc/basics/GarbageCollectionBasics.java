package com.harshit.gc.basics;

/**
 * GARBAGE COLLECTION BASICS - Interview Explanation:
 * 
 * Garbage Collection (GC): Automatic memory management in Java.
 * 
 * Problem: In languages like C/C++, you must manually allocate and free memory.
 * If you forget to free memory, you get memory leaks.
 * 
 * Solution: Java's GC automatically finds and removes unused objects.
 * 
 * Simple Explanation:
 * - Java automatically cleans up objects you're no longer using
 * - Like a robot janitor that removes trash automatically
 * - You don't need to manually delete objects
 * 
 * Key Concepts:
 * 1. Heap Memory: Where objects are stored
 * 2. Stack Memory: Where method calls and local variables are stored
 * 3. GC Roots: Starting points for finding live objects
 * 4. Reachability: Object is reachable if there's a path from GC root
 */
public class GarbageCollectionBasics {

    public static void main(String[] args) {
        System.out.println("=== GARBAGE COLLECTION BASICS ===\n");
        
        demonstrateObjectCreation();
        demonstrateGarbageCollection();
        demonstrateNullReference();
        demonstrateReassignment();
        demonstrateIslandOfIsolation();
        demonstrateFinalizeMethod();
    }
    
    /**
     * Interview Point: Object Creation
     * Objects are created in heap memory
     */
    private static void demonstrateObjectCreation() {
        System.out.println("--- Object Creation ---");
        
        // Interview Point: Object created in heap
        Person person1 = new Person("John", 25);
        System.out.println("  Created: " + person1);
        
        // Interview Point: Reference stored in stack, object in heap
        Person person2 = new Person("Jane", 30);
        System.out.println("  Created: " + person2);
        
        System.out.println("  → person1 and person2 are references (in stack)");
        System.out.println("  → Actual Person objects are in heap\n");
    }
    
    /**
     * Interview Point: When objects become eligible for GC
     * 1. No references pointing to it
     * 2. All references are null
     * 3. Object is unreachable
     */
    private static void demonstrateGarbageCollection() {
        System.out.println("--- Garbage Collection Eligibility ---");
        
        Person person = new Person("Bob", 35);
        System.out.println("  Created: " + person);
        
        // Interview Point: Setting reference to null makes object eligible for GC
        person = null;
        System.out.println("  Set reference to null");
        System.out.println("  → Object is now eligible for garbage collection");
        System.out.println("  → GC will collect it when it runs\n");
    }
    
    /**
     * Interview Point: Null Reference
     * When reference is null, object becomes unreachable
     */
    private static void demonstrateNullReference() {
        System.out.println("--- Null Reference ---");
        
        Person person1 = new Person("Alice", 28);
        Person person2 = person1; // Both point to same object
        
        System.out.println("  person1: " + person1);
        System.out.println("  person2: " + person2);
        System.out.println("  → Both references point to same object");
        
        person1 = null; // person1 is null, but person2 still references object
        System.out.println("  Set person1 to null");
        System.out.println("  → Object still reachable via person2");
        
        person2 = null; // Now no references
        System.out.println("  Set person2 to null");
        System.out.println("  → Object is now eligible for GC\n");
    }
    
    /**
     * Interview Point: Reassignment
     * Reassigning reference makes old object eligible for GC
     */
    private static void demonstrateReassignment() {
        System.out.println("--- Reassignment ---");
        
        Person person = new Person("Charlie", 40);
        System.out.println("  Created: " + person);
        
        // Interview Point: Creating new object, old one becomes eligible for GC
        person = new Person("David", 45);
        System.out.println("  Reassigned to new object: " + person);
        System.out.println("  → Previous object is now eligible for GC\n");
    }
    
    /**
     * Interview Point: Island of Isolation
     * Objects referencing each other but no external reference
     */
    private static void demonstrateIslandOfIsolation() {
        System.out.println("--- Island of Isolation ---");
        
        Person person1 = new Person("Person1", 20);
        Person person2 = new Person("Person2", 25);
        
        // Interview Point: Create circular reference
        person1.setFriend(person2);
        person2.setFriend(person1);
        
        System.out.println("  Created circular reference");
        System.out.println("  person1.friend = person2");
        System.out.println("  person2.friend = person1");
        
        // Interview Point: Remove external references
        person1 = null;
        person2 = null;
        
        System.out.println("  Set both references to null");
        System.out.println("  → Objects reference each other but no external reference");
        System.out.println("  → This is 'Island of Isolation' - eligible for GC");
        System.out.println("  → Modern GC can handle circular references\n");
    }
    
    /**
     * Interview Point: finalize() method
     * Called by GC before object is collected (deprecated in Java 9+)
     */
    private static void demonstrateFinalizeMethod() {
        System.out.println("--- Finalize Method ---");
        
        System.out.println("  Creating object with finalize()...");
        FinalizableObject obj = new FinalizableObject("TestObject");
        obj = null;
        
        // Interview Point: Request GC (not guaranteed to run)
        System.gc();
        
        try {
            Thread.sleep(100); // Give GC time to run
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("  → finalize() may be called before object is collected");
        System.out.println("  → Deprecated in Java 9+, use try-with-resources instead\n");
    }
}

/**
 * Interview Point: Simple Person class for demonstration
 */
class Person {
    private String name;
    private int age;
    private Person friend;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public void setFriend(Person friend) {
        this.friend = friend;
    }
    
    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

/**
 * Interview Point: Class with finalize() method
 */
class FinalizableObject {
    private String name;
    
    public FinalizableObject(String name) {
        this.name = name;
    }
    
    /**
     * Interview Point: finalize() method
     * Called by GC before object is collected
     * Deprecated - don't rely on it
     */
    @Override
    protected void finalize() throws Throwable {
        System.out.println("  → finalize() called for: " + name);
        super.finalize();
    }
}

/**
 * INTERVIEW SUMMARY: Garbage Collection Basics
 * 
 * Key Points:
 * 1. Objects are created in heap memory
 * 2. References are stored in stack memory
 * 3. Object becomes eligible for GC when:
 *    - No references point to it
 *    - All references are null
 *    - Object is unreachable from GC roots
 * 4. GC runs automatically (you can't force it, only suggest)
 * 5. finalize() is deprecated - don't use it
 * 
 * GC Roots:
 * - Local variables in active methods
 * - Static variables
 * - Threads
 * - JNI references
 */

