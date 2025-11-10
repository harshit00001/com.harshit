package com.learning.threads.advanced;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * ATOMIC CLASSES - Interview Explanation:
 * 
 * Problem: Operations like count++ are not atomic (read-modify-write)
 * Even with volatile, we need synchronization for compound operations.
 * 
 * Solution: Atomic classes from java.util.concurrent.atomic package
 * - Provide atomic operations without explicit synchronization
 * - Use Compare-And-Swap (CAS) operations at hardware level
 * - More efficient than synchronized blocks for simple operations
 * 
 * Common Atomic Classes:
 * - AtomicInteger, AtomicLong, AtomicBoolean
 * - AtomicReference (for object references)
 * - AtomicIntegerArray, AtomicLongArray
 */
public class AtomicClasses {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Problem: Non-atomic operations ===");
        demonstrateProblem();
        
        Thread.sleep(2000);
        
        System.out.println("\n=== Solution: AtomicInteger ===");
        demonstrateAtomicSolution();
        
        System.out.println("\n=== AtomicReference Example ===");
        demonstrateAtomicReference();
    }
    
    /**
     * Interview Point: Demonstrates the problem
     * Even with volatile, count++ is not atomic.
     * It involves: read -> increment -> write (3 steps)
     */
    private static void demonstrateProblem() throws InterruptedException {
        NonAtomicCounter counter = new NonAtomicCounter();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment(); // Not atomic!
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment(); // Not atomic!
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("Final count: " + counter.getCount());
        System.out.println("Expected: 2000, but might get less due to race condition");
    }
    
    /**
     * Interview Point: Demonstrates AtomicInteger solution
     * AtomicInteger provides atomic operations using CAS (Compare-And-Swap)
     */
    private static void demonstrateAtomicSolution() throws InterruptedException {
        AtomicCounter counter = new AtomicCounter();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment(); // Atomic operation!
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment(); // Atomic operation!
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("Final count: " + counter.getCount());
        System.out.println("Expected: 2000, got: " + counter.getCount() + " ✓");
        
        // Interview Point: Demonstrate other atomic operations
        System.out.println("\nOther AtomicInteger operations:");
        AtomicInteger atomicInt = new AtomicInteger(10);
        System.out.println("Initial: " + atomicInt.get());
        System.out.println("Increment and get: " + atomicInt.incrementAndGet()); // ++i
        System.out.println("Get and increment: " + atomicInt.getAndIncrement()); // i++
        System.out.println("Add and get: " + atomicInt.addAndGet(5)); // += 5
        System.out.println("Compare and set (10, 20): " + 
            atomicInt.compareAndSet(16, 20)); // CAS operation
        System.out.println("Final value: " + atomicInt.get());
    }
    
    /**
     * Interview Point: AtomicReference for object references
     * Useful when you need to atomically update object references
     */
    private static void demonstrateAtomicReference() {
        Person person1 = new Person("Alice", 25);
        Person person2 = new Person("Bob", 30);
        
        AtomicReference<Person> atomicPerson = new AtomicReference<>(person1);
        
        System.out.println("Initial person: " + atomicPerson.get());
        
        // Interview Point: Atomically update the reference
        boolean updated = atomicPerson.compareAndSet(person1, person2);
        System.out.println("Updated: " + updated);
        System.out.println("New person: " + atomicPerson.get());
        
        // Interview Point: Try to update with wrong expected value
        updated = atomicPerson.compareAndSet(person1, person2);
        System.out.println("Update with wrong expected value: " + updated); // false
    }
}

/**
 * Interview Point: Non-atomic counter
 * Even with volatile, increment is not atomic
 */
class NonAtomicCounter {
    private volatile int count = 0; // volatile doesn't make increment atomic!
    
    public void increment() {
        count++; // This is NOT atomic: read -> increment -> write
    }
    
    public int getCount() {
        return count;
    }
}

/**
 * Interview Point: Atomic counter using AtomicInteger
 * All operations are atomic and thread-safe
 */
class AtomicCounter {
    // Interview Point: AtomicInteger provides atomic operations
    private AtomicInteger count = new AtomicInteger(0);
    
    public void increment() {
        // Interview Point: incrementAndGet() is atomic
        // Uses CAS (Compare-And-Swap) at hardware level
        count.incrementAndGet();
    }
    
    public int getCount() {
        return count.get();
    }
}

/**
 * Simple Person class for AtomicReference example
 */
class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    @Override
    public String toString() {
        return name + " (" + age + ")";
    }
}

/**
 * Interview Point: Key Methods of AtomicInteger
 * 
 * 1. get(): Get current value
 * 2. set(int): Set value
 * 3. getAndSet(int): Get old value and set new value (atomic)
 * 4. incrementAndGet(): ++i (atomic)
 * 5. getAndIncrement(): i++ (atomic)
 * 6. addAndGet(int): += value (atomic)
 * 7. compareAndSet(expected, update): CAS operation (atomic)
 *    - Updates only if current value equals expected
 *    - Returns true if update was successful
 * 
 * Interview Point: How CAS works
 * 1. Read current value
 * 2. Compare with expected value
 * 3. If equal, update to new value (all atomically)
 * 4. If not equal, retry or return false
 * 
 * This is more efficient than synchronization because:
 * - No blocking (no thread suspension)
 * - Hardware-level atomicity
 * - Better performance for low contention scenarios
 */

