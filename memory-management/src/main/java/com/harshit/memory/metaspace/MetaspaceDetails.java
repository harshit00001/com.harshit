package com.harshit.memory.metaspace;

/**
 * METASPACE - Interview Explanation:
 * 
 * Metaspace (Java 8+):
 * 
 * Technical Definition:
 * - Stores class metadata (replaces PermGen)
 * - Stores class definitions, method metadata, constant pool
 * - Not part of heap memory
 * - Grows automatically (no fixed size limit)
 * - GC can collect unused classes
 * 
 * Simple Explanation:
 * - Library of class information
 * - Stores information about classes (not objects)
 * - Can grow as needed
 * - Replaces old PermGen (which had fixed size)
 * 
 * What's Stored:
 * - Class definitions
 * - Method bytecode
 * - Constant pool
 * - Field metadata
 * - Method metadata
 */
public class MetaspaceDetails {

    public static void main(String[] args) {
        System.out.println("=== METASPACE ===\n");
        
        explainMetaspace();
        explainPermGenVsMetaspace();
        demonstrateMetaspaceSettings();
        explainWhatIsStored();
    }
    
    /**
     * Interview Point: What is Metaspace
     * 
     * Technical Definition:
     * - Memory area for class metadata
     * - Introduced in Java 8 (replaces PermGen)
     * - Not part of heap
     * - Managed by native memory
     * - Can grow automatically
     * 
     * Simple Explanation:
     * - Stores information about classes
     * - Like a library catalog
     * - Can expand as needed
     */
    private static void explainMetaspace() {
        System.out.println("--- METASPACE ---");
        System.out.println();
        System.out.println("  What is Metaspace?");
        System.out.println("    → Memory area for class metadata");
        System.out.println("    → Stores information about classes");
        System.out.println("    → Not part of heap memory");
        System.out.println("    → Managed by native memory");
        System.out.println();
        System.out.println("  Key Characteristics:");
        System.out.println("    ✓ Grows automatically");
        System.out.println("    ✓ No fixed size limit (unlike PermGen)");
        System.out.println("    ✓ GC can collect unused classes");
        System.out.println("    ✓ Reduces OutOfMemoryError risk");
        System.out.println();
    }
    
    /**
     * Interview Point: PermGen vs Metaspace
     * 
     * Technical Comparison:
     * 
     * PermGen (Java 7 and earlier):
     * - Fixed size (set with -XX:MaxPermSize)
     * - Part of heap
     * - Could cause OutOfMemoryError
     * - Stores class metadata
     * 
     * Metaspace (Java 8+):
     * - Grows automatically
     * - Not part of heap
     * - Uses native memory
     * - Can be limited with -XX:MaxMetaspaceSize
     * 
     * Simple Explanation:
     * - PermGen: Fixed-size box (could overflow)
     * - Metaspace: Expandable box (grows as needed)
     */
    private static void explainPermGenVsMetaspace() {
        System.out.println("--- PERMGEN vs METASPACE ---");
        System.out.println();
        System.out.println("  PERMGEN (Java 7 and earlier):");
        System.out.println("    ┌─────────────────────┐");
        System.out.println("    │   Fixed Size        │");
        System.out.println("    │   Part of Heap       │");
        System.out.println("    │   -XX:MaxPermSize   │");
        System.out.println("    │   Could overflow    │");
        System.out.println("    └─────────────────────┘");
        System.out.println();
        System.out.println("  METASPACE (Java 8+):");
        System.out.println("    ┌─────────────────────┐");
        System.out.println("    │   Auto-growing      │");
        System.out.println("    │   Native Memory     │");
        System.out.println("    │   -XX:MaxMetaspace  │");
        System.out.println("    │   More flexible     │");
        System.out.println("    └─────────────────────┘");
        System.out.println();
        System.out.println("  Benefits of Metaspace:");
        System.out.println("    → No more OutOfMemoryError: PermGen space");
        System.out.println("    → Automatic growth");
        System.out.println("    → Better memory management");
        System.out.println("    → GC can collect unused classes");
        System.out.println();
    }
    
    /**
     * Interview Point: Metaspace Settings
     * How to configure Metaspace
     */
    private static void demonstrateMetaspaceSettings() {
        System.out.println("--- METASPACE SETTINGS ---");
        System.out.println();
        System.out.println("  JVM Flags for Metaspace:");
        System.out.println();
        System.out.println("  -XX:MetaspaceSize=256m");
        System.out.println("    → Initial Metaspace size: 256 MB");
        System.out.println("    → Metaspace starts at this size");
        System.out.println();
        System.out.println("  -XX:MaxMetaspaceSize=512m");
        System.out.println("    → Maximum Metaspace size: 512 MB");
        System.out.println("    → Metaspace can grow up to this size");
        System.out.println("    → If not set, can grow until native memory limit");
        System.out.println();
        System.out.println("  Example:");
        System.out.println("    java -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m MyApp");
        System.out.println();
        System.out.println("  When to Set MaxMetaspaceSize:");
        System.out.println("    → Prevent unlimited growth");
        System.out.println("    → Control memory usage");
        System.out.println("    → Catch memory leaks early");
        System.out.println();
    }
    
    /**
     * Interview Point: What's Stored in Metaspace
     * 
     * Technical Definition:
     * - Class definitions
     * - Method bytecode
     * - Constant pool
     * - Field metadata
     * - Method metadata
     * - Annotations
     * 
     * Simple Explanation:
     * - Everything about classes (not objects)
     * - Like a blueprint library
     */
    private static void explainWhatIsStored() {
        System.out.println("--- WHAT'S STORED IN METASPACE ---");
        System.out.println();
        System.out.println("  Class Metadata:");
        System.out.println("    → Class name");
        System.out.println("    → Superclass information");
        System.out.println("    → Interfaces implemented");
        System.out.println("    → Access modifiers");
        System.out.println();
        System.out.println("  Method Information:");
        System.out.println("    → Method names");
        System.out.println("    → Method signatures");
        System.out.println("    → Method bytecode");
        System.out.println("    → Return types");
        System.out.println();
        System.out.println("  Field Information:");
        System.out.println("    → Field names");
        System.out.println("    → Field types");
        System.out.println("    → Field modifiers");
        System.out.println();
        System.out.println("  Constant Pool:");
        System.out.println("    → String literals");
        System.out.println("    → Numeric constants");
        System.out.println("    → Class references");
        System.out.println();
        System.out.println("  What's NOT Stored:");
        System.out.println("    ✗ Object instances (stored in heap)");
        System.out.println("    ✗ Object data (stored in heap)");
        System.out.println("    ✗ Static variable values (stored in heap)");
        System.out.println();
    }
}

/**
 * INTERVIEW SUMMARY: Metaspace
 * 
 * Key Points:
 * 1. Stores class metadata (not objects)
 * 2. Replaces PermGen (Java 8+)
 * 3. Grows automatically
 * 4. Not part of heap
 * 5. Uses native memory
 * 6. GC can collect unused classes
 * 
 * Settings:
 * - -XX:MetaspaceSize: Initial size
 * - -XX:MaxMetaspaceSize: Maximum size
 * 
 * Benefits:
 * - No fixed size limit
 * - Automatic growth
 * - Better memory management
 * - Reduces OutOfMemoryError risk
 */

