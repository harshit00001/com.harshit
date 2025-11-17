# Memory Management - Complete Guide

A comprehensive Java project covering memory management from basics to advanced topics, with interview-friendly explanations and code examples.

## 📚 Topics Covered

### 1. **Basics** (`basics/`)
- What is memory management
- Heap vs Stack memory
- Memory allocation
- Reference types
- Comparison of memory areas

### 2. **Heap Memory** (`heap/`)
- Heap structure (Young/Old generation)
- Object lifecycle
- Memory regions (Eden, Survivor, Old)
- Heap settings and configuration

### 3. **Stack Memory** (`stack/`)
- Stack structure (LIFO)
- Stack frames
- Local variables
- Stack overflow
- Stack settings

### 4. **Metaspace** (`metaspace/`)
- What is Metaspace
- PermGen vs Metaspace
- What's stored in Metaspace
- Metaspace settings

### 5. **Advanced** (`advanced/`)
- Memory leaks
- Common leak causes
- Prevention strategies
- Best practices

## 🎯 Interview Questions & Answers

### Q: What is the difference between Heap and Stack memory?

**Technical:**
- **Heap**: Stores objects, shared by all threads, managed by GC, larger size, slower access
- **Stack**: Stores method calls and local variables, one per thread, auto-cleaned, smaller size, faster access

**Simple:**
- Heap: Big warehouse where objects live (shared)
- Stack: Personal workspace for each thread (fast, small)

**Key Differences:**

| Feature | Heap | Stack |
|---------|------|-------|
| Stores | Objects | Primitives, references |
| Threading | Shared | One per thread |
| Management | GC | Auto-cleaned |
| Size | Large (GBs) | Small (1-2 MB) |
| Speed | Slower | Faster |
| Lifetime | Until GC | Until method returns |

---

### Q: Where are objects stored in Java?

**Technical:**
- Objects are always stored in **Heap memory**
- Object references are stored in **Stack memory**
- References point to objects in Heap

**Simple:**
- Objects live in Heap (the warehouse)
- References live in Stack (pointers to warehouse)

**Example:**
```java
Person person = new Person("John", 25);
// 'person' reference → stored in STACK
// Person object → stored in HEAP
// Reference points to object in HEAP
```

---

### Q: What is the Heap structure in Java?

**Technical:**
- **Young Generation**:
  - Eden Space: New objects
  - Survivor S0/S1: Surviving objects
- **Old Generation**: Long-lived objects

**Simple:**
- Young Generation: Nursery for new objects
- Old Generation: Home for long-lived objects

**Object Journey:**
1. Created in Eden
2. Minor GC → Survivor (if survives)
3. Multiple survivals → Old Generation
4. Major GC → Collected (if unreachable)

---

### Q: What is Stack Memory?

**Technical:**
- Stores method calls and local variables
- LIFO (Last In First Out) structure
- One stack per thread
- Fast access, limited size (1-2 MB)
- Automatically cleaned when method exits

**Simple:**
- Personal workspace for each thread
- Like a stack of plates (last added, first removed)
- Stores method calls and local variables

**Stack Frame Contains:**
- Local variables
- Method parameters
- Return address
- Reference to 'this'

---

### Q: What is Metaspace?

**Technical:**
- Stores class metadata (replaces PermGen in Java 8+)
- Not part of heap
- Grows automatically
- GC can collect unused classes

**Simple:**
- Library of class information
- Stores information about classes (not objects)
- Can grow as needed

**What's Stored:**
- Class definitions
- Method bytecode
- Constant pool
- Field metadata

---

### Q: What is a Memory Leak?

**Technical:**
- Objects that should be garbage collected are not
- Memory is allocated but never freed
- Application memory usage keeps growing
- Eventually leads to OutOfMemoryError

**Simple:**
- Objects stay in memory even though you're done with them
- Like leaving lights on - memory used but not needed
- Memory keeps growing until application crashes

**Common Causes:**
1. Static collections holding references
2. Listeners not removed
3. ThreadLocal not cleaned
4. Unclosed resources

---

### Q: How do you prevent Memory Leaks?

**Technical:**
1. Remove objects from collections when done
2. Remove event listeners
3. Clean ThreadLocal (call remove())
4. Close resources (use try-with-resources)
5. Use WeakHashMap for caches
6. Monitor memory usage

**Simple:**
- Clean up after yourself
- Remove references when done
- Close files/connections
- Use proper lifecycle management

**Example:**
```java
// Good: Clean up ThreadLocal
ThreadLocal<String> tl = new ThreadLocal<>();
try {
    tl.set("value");
    // use value
} finally {
    tl.remove(); // Always clean up
}

// Good: Close resources
try (FileReader file = new FileReader("file.txt")) {
    // use file
} // Automatically closed
```

---

### Q: What is Stack Overflow?

**Technical:**
- Occurs when stack size exceeds limit
- Usually caused by deep recursion
- Throws StackOverflowError
- Each method call adds a stack frame

**Simple:**
- Too many method calls (like calling yourself too many times)
- Stack runs out of space
- Application crashes

**Prevention:**
- Use iteration instead of deep recursion
- Increase stack size: `-Xss2m`
- Limit recursion depth

---

### Q: What are the JVM flags for memory management?

**Technical:**

**Heap:**
- `-Xms512m`: Initial heap size
- `-Xmx2g`: Maximum heap size
- `-XX:NewRatio=2`: Old:Young ratio

**Stack:**
- `-Xss1m`: Stack size per thread

**Metaspace:**
- `-XX:MetaspaceSize=256m`: Initial Metaspace
- `-XX:MaxMetaspaceSize=512m`: Max Metaspace

**Example:**
```bash
java -Xms1g -Xmx2g -Xss2m -XX:MetaspaceSize=256m MyApp
```

---

## 📝 Key Concepts

### Memory Areas:
1. **Heap**: Objects (Young + Old generation)
2. **Stack**: Method calls, local variables (per thread)
3. **Metaspace**: Class metadata (Java 8+)

### Object Lifecycle:
1. Created in Eden (Young Generation)
2. Minor GC → Survivor (if survives)
3. Multiple survivals → Old Generation
4. Major GC → Collected (if unreachable)

### Memory Leak Prevention:
- Remove references when done
- Close resources properly
- Clean ThreadLocal
- Use WeakHashMap for caches
- Monitor memory usage

---

## 🚀 Common JVM Flags

```bash
# Heap Size
-Xms512m              # Initial heap
-Xmx2g                # Max heap

# Stack Size
-Xss1m                # Stack per thread

# Metaspace
-XX:MetaspaceSize=256m
-XX:MaxMetaspaceSize=512m

# Young Generation
-XX:NewRatio=2        # Old:Young = 2:1
-XX:NewSize=256m     # Initial young gen
```

---

## 🎓 Best Practices

1. **Understand Memory Areas**: Know where data is stored
2. **Monitor Memory**: Use profiling tools
3. **Prevent Leaks**: Clean up references and resources
4. **Tune Appropriately**: Set heap/stack sizes based on needs
5. **Use Try-With-Resources**: Automatic resource cleanup
6. **Profile Regularly**: Find memory issues early

---

**Happy Learning! 🎓**

