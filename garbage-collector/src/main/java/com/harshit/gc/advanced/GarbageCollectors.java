package com.harshit.gc.advanced;

/**
 * GARBAGE COLLECTORS - Interview Explanation:
 * 
 * Java provides different garbage collectors, each optimized for different scenarios:
 * 
 * 1. Serial GC: Single-threaded, good for small applications
 * 2. Parallel GC: Multi-threaded, good for throughput
 * 3. CMS (Concurrent Mark Sweep): Low pause times (deprecated in Java 9)
 * 4. G1 GC: Balanced, good for large heaps
 * 5. ZGC: Ultra-low latency (Java 11+)
 * 6. Shenandoah: Low pause times (Java 12+)
 * 
 * Simple Explanation:
 * - Different GC algorithms for different needs
 * - Some prioritize speed, some prioritize low pauses
 * - Choose based on your application requirements
 */
public class GarbageCollectors {

    public static void main(String[] args) {
        System.out.println("=== GARBAGE COLLECTORS ===\n");
        
        explainSerialGC();
        explainParallelGC();
        explainG1GC();
        explainZGC();
        explainGCSelection();
        demonstrateGCFlags();
    }
    
    /**
     * Interview Point: Serial GC
     * 
     * Technical:
     * - Single-threaded collector
     * - Uses mark-sweep-compact algorithm
     * - Stops all application threads during GC (Stop-the-World)
     * - Good for small applications (< 100MB heap)
     * 
     * Simple:
     * - One worker cleaning up
     * - Pauses everything while cleaning
     * - Good for small apps
     */
    private static void explainSerialGC() {
        System.out.println("--- Serial GC ---");
        System.out.println("  Algorithm: Mark-Sweep-Compact");
        System.out.println("  Threads: Single-threaded");
        System.out.println("  Pause: Stop-the-World (pauses application)");
        System.out.println("  Best for: Small applications (< 100MB heap)");
        System.out.println("  JVM Flag: -XX:+UseSerialGC");
        System.out.println("  → Simple, but pauses application\n");
    }
    
    /**
     * Interview Point: Parallel GC
     * 
     * Technical:
     * - Multi-threaded collector
     * - Uses multiple threads for GC
     * - Also Stop-the-World, but faster due to parallelism
     * - Default in Java 8 and earlier
     * - Good for throughput (maximizing work done)
     * 
     * Simple:
     * - Multiple workers cleaning up
     * - Still pauses, but faster
     * - Good when you want maximum work done
     */
    private static void explainParallelGC() {
        System.out.println("--- Parallel GC ---");
        System.out.println("  Algorithm: Parallel Mark-Sweep-Compact");
        System.out.println("  Threads: Multi-threaded");
        System.out.println("  Pause: Stop-the-World (but parallel)");
        System.out.println("  Best for: Throughput-focused applications");
        System.out.println("  JVM Flag: -XX:+UseParallelGC");
        System.out.println("  → Default in Java 8, good for batch processing\n");
    }
    
    /**
     * Interview Point: G1 GC (Garbage First)
     * 
     * Technical:
     * - Designed for large heaps (> 4GB)
     * - Divides heap into regions
     * - Concurrent marking phase
     * - Predictable pause times
     * - Default in Java 9+ for server-class machines
     * - Good balance between throughput and latency
     * 
     * Simple:
     * - Works on large heaps efficiently
     * - Tries to keep pause times predictable
     * - Good general-purpose choice
     */
    private static void explainG1GC() {
        System.out.println("--- G1 GC (Garbage First) ---");
        System.out.println("  Algorithm: Region-based, concurrent marking");
        System.out.println("  Threads: Multi-threaded");
        System.out.println("  Pause: Mostly concurrent, predictable pauses");
        System.out.println("  Best for: Large heaps (> 4GB), low latency requirements");
        System.out.println("  JVM Flag: -XX:+UseG1GC");
        System.out.println("  → Default in Java 9+ for server-class machines");
        System.out.println("  → Good balance of throughput and latency\n");
    }
    
    /**
     * Interview Point: ZGC (Z Garbage Collector)
     * 
     * Technical:
     * - Ultra-low latency (< 10ms pauses)
     * - Scalable to very large heaps (TB)
     * - Concurrent (doesn't stop application threads)
     * - Available from Java 11+
     * - Good for real-time applications
     * 
     * Simple:
     * - Extremely fast, almost no pauses
     * - Can handle huge amounts of memory
     * - Good for applications that can't pause
     */
    private static void explainZGC() {
        System.out.println("--- ZGC (Z Garbage Collector) ---");
        System.out.println("  Algorithm: Concurrent, region-based");
        System.out.println("  Threads: Multi-threaded, concurrent");
        System.out.println("  Pause: < 10ms (ultra-low latency)");
        System.out.println("  Best for: Large heaps, low latency requirements");
        System.out.println("  JVM Flag: -XX:+UseZGC");
        System.out.println("  → Available from Java 11+");
        System.out.println("  → Good for real-time systems\n");
    }
    
    /**
     * Interview Point: How to choose GC
     */
    private static void explainGCSelection() {
        System.out.println("--- GC Selection Guide ---");
        System.out.println("  Small heap (< 100MB):");
        System.out.println("    → Serial GC");
        System.out.println("  Medium heap, throughput priority:");
        System.out.println("    → Parallel GC");
        System.out.println("  Large heap (> 4GB), balanced:");
        System.out.println("    → G1 GC (default in Java 9+)");
        System.out.println("  Very large heap, low latency:");
        System.out.println("    → ZGC or Shenandoah");
        System.out.println("  Real-time systems:");
        System.out.println("    → ZGC or Shenandoah\n");
    }
    
    /**
     * Interview Point: Common GC JVM Flags
     */
    private static void demonstrateGCFlags() {
        System.out.println("--- Common GC JVM Flags ---");
        System.out.println("  Select GC:");
        System.out.println("    -XX:+UseSerialGC");
        System.out.println("    -XX:+UseParallelGC");
        System.out.println("    -XX:+UseG1GC");
        System.out.println("    -XX:+UseZGC");
        System.out.println();
        System.out.println("  Heap size:");
        System.out.println("    -Xms512m (initial heap size)");
        System.out.println("    -Xmx2g (maximum heap size)");
        System.out.println();
        System.out.println("  GC logging:");
        System.out.println("    -XX:+PrintGC");
        System.out.println("    -XX:+PrintGCDetails");
        System.out.println("    -Xlog:gc*:file=gc.log");
        System.out.println();
        System.out.println("  G1 specific:");
        System.out.println("    -XX:MaxGCPauseMillis=200 (target pause time)");
        System.out.println("    -XX:G1HeapRegionSize=16m (region size)");
        System.out.println();
    }
}

/**
 * INTERVIEW SUMMARY: Garbage Collectors
 * 
 * Key Points:
 * 1. Serial GC: Single-threaded, small apps
 * 2. Parallel GC: Multi-threaded, throughput
 * 3. G1 GC: Large heaps, balanced (default in Java 9+)
 * 4. ZGC: Ultra-low latency, very large heaps
 * 
 * Selection Criteria:
 * - Heap size
 * - Latency requirements
 * - Throughput requirements
 * - Java version
 * 
 * Common Flags:
 * - -XX:+UseG1GC: Use G1 collector
 * - -Xmx2g: Max heap 2GB
 * - -Xms512m: Initial heap 512MB
 * - -XX:MaxGCPauseMillis=200: Target pause time
 */

