package com.harshit.memory.advanced;

import java.util.*;

/**
 * MEMORY LEAKS - Interview Explanation:
 * 
 * Memory Leak: When objects are no longer needed but still referenced,
 * preventing garbage collection, causing memory usage to grow over time.
 * 
 * Technical Definition:
 * - Objects that should be garbage collected are not
 * - Memory is allocated but never freed
 * - Application memory usage keeps growing
 * - Eventually leads to OutOfMemoryError
 * 
 * Simple Explanation:
 * - Like leaving lights on - memory is used but not needed
 * - Objects stay in memory even though you're done with them
 * - Memory keeps growing until application crashes
 * 
 * Common Causes:
 * 1. Static collections holding references
 * 2. Listeners not removed
 * 3. ThreadLocal not cleaned
 * 4. Unclosed resources (files, connections)
 * 5. Circular references (though modern GC handles this)
 */
public class MemoryLeaks {

    public static void main(String[] args) {
        System.out.println("=== MEMORY LEAKS ===\n");
        
        demonstrateStaticCollectionLeak();
        demonstrateListenerLeak();
        demonstrateThreadLocalLeak();
        demonstrateResourceLeak();
        demonstratePrevention();
    }
    
    /**
     * Interview Point: Static Collection Memory Leak
     * 
     * Problem: Static collections hold references forever
     * Objects added to static collection never get GC'd
     * 
     * Simple Explanation:
     * - Static variables live for entire application lifetime
     * - Objects in static collections never become unreachable
     * - Memory keeps growing
     */
    private static void demonstrateStaticCollectionLeak() {
        System.out.println("--- Static Collection Leak ---");
        System.out.println();
        System.out.println("  Problem Code:");
        System.out.println("    private static List<Object> cache = new ArrayList<>();");
        System.out.println("    cache.add(largeObject); // Never removed!");
        System.out.println();
        System.out.println("  Why it's a leak:");
        System.out.println("    → Static variable lives forever");
        System.out.println("    → Objects in cache never become unreachable");
        System.out.println("    → Memory keeps growing");
        System.out.println();
        System.out.println("  Solution:");
        System.out.println("    → Use WeakHashMap");
        System.out.println("    → Remove objects when done");
        System.out.println("    → Use size limits");
        System.out.println("    → Use proper caching libraries");
        System.out.println();
    }
    
    /**
     * Interview Point: Listener Memory Leak
     * 
     * Problem: Event listeners not removed
     * Listener holds reference to object, preventing GC
     * 
     * Simple Explanation:
     * - You register a listener
     * - Listener holds reference to your object
     * - If you don't remove listener, object can't be GC'd
     */
    private static void demonstrateListenerLeak() {
        System.out.println("--- Listener Leak ---");
        System.out.println();
        System.out.println("  Problem Code:");
        System.out.println("    button.addActionListener(new ActionListener() {");
        System.out.println("        // Listener holds reference to outer class");
        System.out.println("    });");
        System.out.println("    // Never removed!");
        System.out.println();
        System.out.println("  Why it's a leak:");
        System.out.println("    → Listener holds reference to object");
        System.out.println("    → Object can't be garbage collected");
        System.out.println("    → Memory keeps growing");
        System.out.println();
        System.out.println("  Solution:");
        System.out.println("    → Remove listeners when done");
        System.out.println("    → Use WeakReference");
        System.out.println("    → Use proper lifecycle management");
        System.out.println();
    }
    
    /**
     * Interview Point: ThreadLocal Memory Leak
     * 
     * Problem: ThreadLocal not cleaned
     * ThreadLocal values stay in memory even after thread dies
     * 
     * Simple Explanation:
     * - ThreadLocal stores data per thread
     * - If not removed, data stays in memory
     * - Especially problematic in thread pools
     */
    private static void demonstrateThreadLocalLeak() {
        System.out.println("--- ThreadLocal Leak ---");
        System.out.println();
        System.out.println("  Problem Code:");
        System.out.println("    ThreadLocal<Object> threadLocal = new ThreadLocal<>();");
        System.out.println("    threadLocal.set(largeObject);");
        System.out.println("    // Never removed!");
        System.out.println();
        System.out.println("  Why it's a leak:");
        System.out.println("    → ThreadLocal values stay in memory");
        System.out.println("    → In thread pools, threads are reused");
        System.out.println("    → Old values never get cleared");
        System.out.println();
        System.out.println("  Solution:");
        System.out.println("    → Always call threadLocal.remove()");
        System.out.println("    → Use try-finally to ensure cleanup");
        System.out.println("    → Use ThreadLocal with initialValue()");
        System.out.println();
        
        // Interview Point: Correct usage
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        try {
            threadLocal.set("Value");
            // Use value
        } finally {
            threadLocal.remove(); // Always clean up
        }
        System.out.println("  Correct usage: Always remove in finally block");
        System.out.println();
    }
    
    /**
     * Interview Point: Resource Leak
     * 
     * Problem: Unclosed resources (files, connections)
     * Resources stay open, consuming memory
     * 
     * Simple Explanation:
     * - You open a file or connection
     * - If you don't close it, it stays open
     * - Memory and system resources are consumed
     */
    private static void demonstrateResourceLeak() {
        System.out.println("--- Resource Leak ---");
        System.out.println();
        System.out.println("  Problem Code:");
        System.out.println("    FileReader file = new FileReader(\"file.txt\");");
        System.out.println("    // Never closed!");
        System.out.println();
        System.out.println("  Why it's a leak:");
        System.out.println("    → File handle stays open");
        System.out.println("    → System resources consumed");
        System.out.println("    → Can't open more files (file descriptor limit)");
        System.out.println();
        System.out.println("  Solution:");
        System.out.println("    → Use try-with-resources");
        System.out.println("    → Always close in finally block");
        System.out.println("    → Use connection pooling");
        System.out.println();
        
        // Interview Point: Correct usage with try-with-resources
        System.out.println("  Correct Usage (try-with-resources):");
        System.out.println("    try (FileReader file = new FileReader(\"file.txt\")) {");
        System.out.println("        // Use file");
        System.out.println("    } // Automatically closed");
        System.out.println();
    }
    
    /**
     * Interview Point: Memory Leak Prevention
     * Best practices to avoid memory leaks
     */
    private static void demonstratePrevention() {
        System.out.println("--- Memory Leak Prevention ---");
        System.out.println();
        System.out.println("  Best Practices:");
        System.out.println();
        System.out.println("  1. Remove from Collections:");
        System.out.println("     → Remove objects when done");
        System.out.println("     → Use WeakHashMap for caches");
        System.out.println("     → Set size limits");
        System.out.println();
        System.out.println("  2. Remove Listeners:");
        System.out.println("     → Always remove listeners");
        System.out.println("     → Use WeakReference if appropriate");
        System.out.println();
        System.out.println("  3. Clean ThreadLocal:");
        System.out.println("     → Always call remove()");
        System.out.println("     → Use try-finally");
        System.out.println();
        System.out.println("  4. Close Resources:");
        System.out.println("     → Use try-with-resources");
        System.out.println("     → Always close in finally");
        System.out.println();
        System.out.println("  5. Use Profiling Tools:");
        System.out.println("     → VisualVM");
        System.out.println("     → JProfiler");
        System.out.println("     → Memory Analyzer (MAT)");
        System.out.println();
        System.out.println("  6. Monitor Memory:");
        System.out.println("     → Watch heap usage");
        System.out.println("     → Set up alerts");
        System.out.println("     → Regular GC analysis");
        System.out.println();
    }
}

/**
 * INTERVIEW SUMMARY: Memory Leaks
 * 
 * Key Points:
 * 1. Objects not GC'd when they should be
 * 2. Memory usage keeps growing
 * 3. Eventually causes OutOfMemoryError
 * 
 * Common Causes:
 * - Static collections
 * - Unremoved listeners
 * - ThreadLocal not cleaned
 * - Unclosed resources
 * 
 * Prevention:
 * - Remove references when done
 * - Use try-with-resources
 * - Clean ThreadLocal
 * - Use profiling tools
 * - Monitor memory usage
 */

