package com.harshit.gc.memory;

/**
 * MEMORY MANAGEMENT - Interview Explanation:
 * 
 * Java Memory Structure:
 * 
 * 1. Heap Memory:
 *    - Young Generation (Eden, Survivor S0, Survivor S1)
 *    - Old Generation (Tenured)
 * 
 * 2. Non-Heap Memory:
 *    - Method Area (Metaspace in Java 8+)
 *    - Stack Memory
 *    - Code Cache
 * 
 * Simple Explanation:
 * - Heap: Where objects live
 * - Young Generation: New objects
 * - Old Generation: Long-lived objects
 * - Stack: Method calls and local variables
 */
public class MemoryManagement {

    public static void main(String[] args) {
        System.out.println("=== MEMORY MANAGEMENT ===\n");
        
        explainHeapStructure();
        explainObjectLifecycle();
        explainMemoryAreas();
        demonstrateMemorySettings();
    }
    
    /**
     * Interview Point: Heap Structure
     */
    private static void explainHeapStructure() {
        System.out.println("--- Heap Memory Structure ---");
        System.out.println("  Heap is divided into:");
        System.out.println();
        System.out.println("  1. Young Generation:");
        System.out.println("     - Eden Space: New objects created here");
        System.out.println("     - Survivor S0: Objects that survived one GC");
        System.out.println("     - Survivor S1: Objects that survived one GC");
        System.out.println("     → Most objects die young (die in Young Gen)");
        System.out.println();
        System.out.println("  2. Old Generation (Tenured):");
        System.out.println("     - Long-lived objects");
        System.out.println("     - Objects that survived multiple GC cycles");
        System.out.println("     → GC runs less frequently here");
        System.out.println();
    }
    
    /**
     * Interview Point: Object Lifecycle
     */
    private static void explainObjectLifecycle() {
        System.out.println("--- Object Lifecycle ---");
        System.out.println("  1. Object created → Eden Space");
        System.out.println("  2. Eden fills up → Minor GC");
        System.out.println("  3. Surviving objects → Survivor Space");
        System.out.println("  4. After multiple survivals → Old Generation");
        System.out.println("  5. Old Gen fills up → Major GC (Full GC)");
        System.out.println("  6. Unreachable objects → Collected");
        System.out.println();
        System.out.println("  → Most objects die in Young Generation");
        System.out.println("  → Only long-lived objects reach Old Generation");
        System.out.println();
    }
    
    /**
     * Interview Point: Memory Areas
     */
    private static void explainMemoryAreas() {
        System.out.println("--- Memory Areas ---");
        System.out.println("  Heap Memory:");
        System.out.println("    - Stores objects");
        System.out.println("    - Shared by all threads");
        System.out.println("    - Managed by GC");
        System.out.println();
        System.out.println("  Stack Memory:");
        System.out.println("    - Stores method calls and local variables");
        System.out.println("    - One stack per thread");
        System.out.println("    - Fast access, limited size");
        System.out.println();
        System.out.println("  Metaspace (Java 8+):");
        System.out.println("    - Stores class metadata");
        System.out.println("    - Replaces PermGen (Java 7 and earlier)");
        System.out.println("    - Grows automatically");
        System.out.println();
    }
    
    /**
     * Interview Point: Memory Settings
     */
    private static void demonstrateMemorySettings() {
        System.out.println("--- Memory Settings ---");
        System.out.println("  Heap Size:");
        System.out.println("    -Xms512m: Initial heap size (512 MB)");
        System.out.println("    -Xmx2g: Maximum heap size (2 GB)");
        System.out.println("    → Set both to same value for stable performance");
        System.out.println();
        System.out.println("  Young Generation:");
        System.out.println("    -XX:NewRatio=2: Old:Young = 2:1");
        System.out.println("    -XX:NewSize=256m: Initial young gen size");
        System.out.println("    -XX:MaxNewSize=512m: Max young gen size");
        System.out.println();
        System.out.println("  Metaspace:");
        System.out.println("    -XX:MetaspaceSize=256m: Initial metaspace");
        System.out.println("    -XX:MaxMetaspaceSize=512m: Max metaspace");
        System.out.println();
        System.out.println("  Example:");
        System.out.println("    java -Xms1g -Xmx2g -XX:+UseG1GC MyApp");
        System.out.println();
    }
}

/**
 * INTERVIEW SUMMARY: Memory Management
 * 
 * Heap Structure:
 * - Young Generation (Eden + Survivors)
 * - Old Generation
 * 
 * Object Journey:
 * 1. Created in Eden
 * 2. Minor GC → Survivor (if survives)
 * 3. Multiple survivals → Old Generation
 * 4. Major GC → Collected (if unreachable)
 * 
 * Key Settings:
 * - -Xms: Initial heap
 * - -Xmx: Max heap
 * - -XX:+UseG1GC: Use G1 collector
 */

