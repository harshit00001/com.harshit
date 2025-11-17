package com.harshit.memory.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * HEAP MEMORY DETAILS - Interview Explanation:
 * 
 * Heap Structure:
 * 
 * 1. YOUNG GENERATION:
 *    - Eden Space: New objects created here
 *    - Survivor Space S0: Objects that survived one GC
 *    - Survivor Space S1: Objects that survived one GC
 *    - Most objects die here (short-lived)
 * 
 * 2. OLD GENERATION (Tenured):
 *    - Long-lived objects
 *    - Objects that survived multiple GC cycles
 *    - GC runs less frequently here
 * 
 * Simple Explanation:
 * - Young Generation: Where new objects are born (most die quickly)
 * - Old Generation: Where long-lived objects live (survivors)
 * - Like a nursery (Young) and retirement home (Old)
 */
public class HeapMemoryDetails {

    public static void main(String[] args) {
        System.out.println("=== HEAP MEMORY DETAILS ===\n");
        
        explainHeapStructure();
        demonstrateObjectLifecycle();
        demonstrateMemoryRegions();
        demonstrateHeapSettings();
    }
    
    /**
     * Interview Point: Heap Structure
     * 
     * Technical Definition:
     * Heap is divided into generations for efficient GC:
     * - Young Generation: For new objects (frequent GC)
     * - Old Generation: For long-lived objects (less frequent GC)
     * 
     * Simple Explanation:
     * - Heap is like a building with two floors
     * - First floor (Young): New objects, cleaned often
     * - Second floor (Old): Old objects, cleaned less often
     */
    private static void explainHeapStructure() {
        System.out.println("--- HEAP STRUCTURE ---");
        System.out.println();
        System.out.println("  YOUNG GENERATION:");
        System.out.println("    ┌─────────────────┐");
        System.out.println("    │   Eden Space    │ ← New objects created here");
        System.out.println("    └─────────────────┘");
        System.out.println("    ┌──────┐  ┌──────┐");
        System.out.println("    │  S0   │  │  S1   │ ← Survivors (alternating)");
        System.out.println("    └──────┘  └──────┘");
        System.out.println();
        System.out.println("  OLD GENERATION:");
        System.out.println("    ┌─────────────────┐");
        System.out.println("    │  Tenured Space  │ ← Long-lived objects");
        System.out.println("    └─────────────────┘");
        System.out.println();
        System.out.println("  Key Points:");
        System.out.println("    → Most objects die in Young Generation");
        System.out.println("    → Only survivors reach Old Generation");
        System.out.println("    → GC runs more often in Young Generation");
        System.out.println();
    }
    
    /**
     * Interview Point: Object Lifecycle in Heap
     * 
     * Technical Definition:
     * 1. Object created → Eden Space
     * 2. Eden fills → Minor GC
     * 3. Surviving objects → Survivor Space
     * 4. After multiple survivals → Old Generation
     * 5. Old Gen fills → Major GC (Full GC)
     * 
     * Simple Explanation:
     * - Objects start in Eden (nursery)
     * - If they survive, move to Survivor
     * - If they keep surviving, move to Old Generation
     * - Like promotion through ranks
     */
    private static void demonstrateObjectLifecycle() {
        System.out.println("--- OBJECT LIFECYCLE ---");
        System.out.println();
        System.out.println("  Step 1: Object Created");
        System.out.println("    Person p = new Person(\"John\", 25);");
        System.out.println("    → Object created in EDEN SPACE");
        System.out.println();
        
        System.out.println("  Step 2: Eden Fills Up");
        System.out.println("    → Minor GC triggered");
        System.out.println("    → Unreachable objects collected");
        System.out.println("    → Surviving objects move to SURVIVOR SPACE");
        System.out.println();
        
        System.out.println("  Step 3: Multiple Survivals");
        System.out.println("    → Object survives multiple Minor GCs");
        System.out.println("    → Object promoted to OLD GENERATION");
        System.out.println();
        
        System.out.println("  Step 4: Old Generation Fills");
        System.out.println("    → Major GC (Full GC) triggered");
        System.out.println("    → Cleans entire heap (Young + Old)");
        System.out.println("    → Takes longer than Minor GC");
        System.out.println();
        
        // Interview Point: Demonstrate with code
        System.out.println("  Code Example:");
        List<Person> longLivedObjects = new ArrayList<>();
        
        // Create many objects (most will die, some will survive)
        for (int i = 0; i < 1000; i++) {
            Person p = new Person("Person" + i, i);
            if (i % 100 == 0) {
                // Keep reference to some objects (they survive)
                longLivedObjects.add(p);
            }
            // Most objects become eligible for GC here
        }
        
        System.out.println("    → Created 1000 objects");
        System.out.println("    → Most died (no reference)");
        System.out.println("    → " + longLivedObjects.size() + " survived (have reference)");
        System.out.println("    → Survivors may reach Old Generation");
        System.out.println();
    }
    
    /**
     * Interview Point: Memory Regions
     * Different regions for different purposes
     */
    private static void demonstrateMemoryRegions() {
        System.out.println("--- MEMORY REGIONS ---");
        System.out.println();
        System.out.println("  EDEN SPACE:");
        System.out.println("    → Where new objects are allocated");
        System.out.println("    → Fast allocation");
        System.out.println("    → Most objects die here");
        System.out.println("    → GC runs when Eden is full");
        System.out.println();
        
        System.out.println("  SURVIVOR SPACE (S0/S1):");
        System.out.println("    → Objects that survived Minor GC");
        System.out.println("    → S0 and S1 alternate (copying algorithm)");
        System.out.println("    → Objects copied between S0 and S1");
        System.out.println("    → After multiple survivals → promoted to Old");
        System.out.println();
        
        System.out.println("  OLD GENERATION:");
        System.out.println("    → Long-lived objects");
        System.out.println("    → Objects that survived many GC cycles");
        System.out.println("    → GC runs less frequently");
        System.out.println("    → Major GC cleans this (takes longer)");
        System.out.println();
    }
    
    /**
     * Interview Point: Heap Settings
     * How to configure heap size
     */
    private static void demonstrateHeapSettings() {
        System.out.println("--- HEAP SETTINGS ---");
        System.out.println();
        System.out.println("  JVM Flags for Heap:");
        System.out.println();
        System.out.println("  -Xms512m");
        System.out.println("    → Initial heap size: 512 MB");
        System.out.println("    → Heap starts at this size");
        System.out.println();
        System.out.println("  -Xmx2g");
        System.out.println("    → Maximum heap size: 2 GB");
        System.out.println("    → Heap can grow up to this size");
        System.out.println();
        System.out.println("  -XX:NewRatio=2");
        System.out.println("    → Old:Young ratio = 2:1");
        System.out.println("    → Old Gen is 2x size of Young Gen");
        System.out.println();
        System.out.println("  -XX:NewSize=256m");
        System.out.println("    → Initial Young Generation size: 256 MB");
        System.out.println();
        System.out.println("  -XX:MaxNewSize=512m");
        System.out.println("    → Maximum Young Generation size: 512 MB");
        System.out.println();
        System.out.println("  Example:");
        System.out.println("    java -Xms1g -Xmx2g -XX:NewRatio=2 MyApp");
        System.out.println("    → Initial: 1GB, Max: 2GB, Old:Young = 2:1");
        System.out.println();
        
        // Interview Point: Get current heap info
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        System.out.println("  Current Heap Info:");
        System.out.println("    Max Heap: " + (maxMemory / 1024 / 1024) + " MB");
        System.out.println("    Total Heap: " + (totalMemory / 1024 / 1024) + " MB");
        System.out.println("    Used Heap: " + (usedMemory / 1024 / 1024) + " MB");
        System.out.println("    Free Heap: " + (freeMemory / 1024 / 1024) + " MB");
        System.out.println();
    }
}

/**
 * Person class for demonstration
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
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

/**
 * INTERVIEW SUMMARY: Heap Memory
 * 
 * Key Points:
 * 1. Heap divided into Young and Old Generation
 * 2. Young Gen: Eden + Survivors (S0, S1)
 * 3. Most objects die in Young Generation
 * 4. Long-lived objects reach Old Generation
 * 5. Minor GC: Cleans Young Generation (fast)
 * 6. Major GC: Cleans entire heap (slower)
 * 
 * Settings:
 * - -Xms: Initial heap size
 * - -Xmx: Maximum heap size
 * - -XX:NewRatio: Old:Young ratio
 */

