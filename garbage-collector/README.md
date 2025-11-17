# Garbage Collector - Complete Guide

A comprehensive Java project covering garbage collection from basics to advanced topics, with interview-friendly explanations.

## 📚 Topics Covered

### 1. **Basics** (`basics/`)
- What is garbage collection
- When objects become eligible for GC
- Heap vs Stack memory
- GC Roots and reachability
- finalize() method

### 2. **Advanced** (`advanced/`)
- Different GC algorithms (Serial, Parallel, G1, ZGC)
- When to use which GC
- GC tuning and flags
- Performance considerations

### 3. **Memory Management** (`memory/`)
- Heap structure (Young/Old generation)
- Object lifecycle
- Memory areas (Heap, Stack, Metaspace)
- Memory settings

## 🎯 Interview Questions & Answers

### Q: What is Garbage Collection in Java?

**Technical:**
- Automatic memory management process
- Identifies and removes unused objects from heap
- Runs automatically in background
- Prevents memory leaks

**Simple:**
- Java automatically cleans up objects you're no longer using
- Like a robot janitor that removes trash
- You don't need to manually delete objects

---

### Q: When does an object become eligible for garbage collection?

**Technical:**
- When no references point to it
- When all references are set to null
- When object is unreachable from any GC root
- When it's part of an "island of isolation"

**Simple:**
- When nothing points to it anymore
- Like when you throw something away and no one references it

**Example:**
```java
Person person = new Person("John");
person = null; // Object is now eligible for GC
```

---

### Q: What are the different types of garbage collectors in Java?

**Technical:**

1. **Serial GC**: Single-threaded, mark-sweep-compact
2. **Parallel GC**: Multi-threaded, good for throughput
3. **G1 GC**: Region-based, balanced (default in Java 9+)
4. **ZGC**: Ultra-low latency, concurrent
5. **Shenandoah**: Low pause times

**Simple:**
- Different algorithms for different needs
- Some prioritize speed, some prioritize low pauses
- Choose based on your application

**Flags:**
```bash
-XX:+UseSerialGC    # Serial GC
-XX:+UseParallelGC  # Parallel GC
-XX:+UseG1GC        # G1 GC (default in Java 9+)
-XX:+UseZGC         # ZGC (Java 11+)
```

---

### Q: What is the difference between Minor GC and Major GC?

**Technical:**
- **Minor GC**: Cleans Young Generation (Eden + Survivors)
  - Runs frequently
  - Fast (usually < 100ms)
  - Most objects die here
  
- **Major GC (Full GC)**: Cleans entire heap (Young + Old)
  - Runs less frequently
  - Slower (can be seconds)
  - Stop-the-World pause

**Simple:**
- Minor GC: Quick cleanup of new objects
- Major GC: Deep cleanup of everything (takes longer)

---

### Q: What is the heap structure in Java?

**Technical:**
- **Young Generation**:
  - Eden Space: New objects
  - Survivor S0/S1: Surviving objects
- **Old Generation**: Long-lived objects

**Simple:**
- Young Generation: Where new objects are born
- Old Generation: Where long-lived objects live

---

### Q: What is Metaspace?

**Technical:**
- Stores class metadata (replaces PermGen in Java 8+)
- Grows automatically
- Not part of heap
- GC can collect unused classes

**Simple:**
- Stores information about classes
- Replaces old PermGen (which had fixed size)
- Can grow as needed

---

### Q: How do you tune garbage collection?

**Technical:**
- Set heap size: `-Xms` and `-Xmx`
- Choose GC algorithm: `-XX:+UseG1GC`
- Set target pause time: `-XX:MaxGCPauseMillis=200`
- Enable GC logging: `-Xlog:gc*:file=gc.log`

**Simple:**
- Give enough memory: `-Xmx2g`
- Choose right GC: G1 for most cases
- Monitor and adjust based on metrics

**Example:**
```bash
java -Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 MyApp
```

---

### Q: What is Stop-the-World in GC?

**Technical:**
- GC pauses all application threads
- Application is completely stopped
- Happens during GC execution
- Minimizing this is key to performance

**Simple:**
- Everything stops while GC cleans up
- Like pausing a movie to clean the room
- Modern GCs try to minimize this

---

### Q: What are GC Roots?

**Technical:**
- Starting points for finding live objects
- Objects reachable from GC roots are not collected
- Types: Local variables, static variables, threads, JNI references

**Simple:**
- Starting points for finding what's still in use
- If you can reach an object from a root, it's alive

---

### Q: Can you force garbage collection?

**Technical:**
- `System.gc()` suggests GC, but doesn't guarantee it
- `Runtime.getRuntime().gc()` same as above
- GC runs automatically when needed
- Don't rely on forcing it

**Simple:**
- You can ask, but Java decides when to run
- Like asking someone to clean, but they decide when

**Best Practice:**
- Don't call `System.gc()` manually
- Let JVM handle it automatically
- Tune GC settings instead

---

## 📝 Key Concepts

### Memory Areas:
- **Heap**: Objects (Young + Old generation)
- **Stack**: Method calls, local variables
- **Metaspace**: Class metadata

### GC Algorithms:
- **Serial**: Single-threaded, small apps
- **Parallel**: Multi-threaded, throughput
- **G1**: Balanced, large heaps (default Java 9+)
- **ZGC**: Ultra-low latency (Java 11+)

### Object Lifecycle:
1. Created in Eden
2. Minor GC → Survivor (if survives)
3. Multiple survivals → Old Generation
4. Major GC → Collected (if unreachable)

---

## 🚀 Common JVM Flags

```bash
# Heap Size
-Xms512m              # Initial heap
-Xmx2g                # Max heap

# GC Selection
-XX:+UseG1GC          # Use G1 GC
-XX:+UseZGC           # Use ZGC

# GC Tuning
-XX:MaxGCPauseMillis=200  # Target pause time
-XX:NewRatio=2        # Old:Young ratio

# GC Logging
-Xlog:gc*:file=gc.log  # GC logs
-XX:+PrintGCDetails    # Detailed GC info

# Metaspace
-XX:MetaspaceSize=256m
-XX:MaxMetaspaceSize=512m
```

---

**Happy Learning! 🎓**

