# Quick Start Guide

## Prerequisites
- Java 11 or higher
- Maven (optional, but recommended)

## Running the Examples

### Using Maven

1. Navigate to the project directory:
   ```bash
   cd java-multithreading-learning
   ```

2. Compile the project:
   ```bash
   mvn compile
   ```

3. Run a specific example:
   ```bash
   # Run basic thread creation example
   mvn exec:java -Dexec.mainClass="com.learning.threads.basics.BasicThreadCreation"
   
   # Run thread lifecycle example
   mvn exec:java -Dexec.mainClass="com.learning.threads.basics.ThreadLifecycle"
   
   # Run synchronization example
   mvn exec:java -Dexec.mainClass="com.learning.threads.synchronization.SynchronizationBasics"
   ```

### Using Java Directly

1. Compile all Java files:
   ```bash
   javac -d target/classes src/main/java/com/learning/threads/**/*.java
   ```

2. Run a specific example:
   ```bash
   java -cp target/classes com.learning.threads.basics.BasicThreadCreation
   ```

### Using IDE (IntelliJ IDEA / Eclipse)

1. Open the project in your IDE
2. Wait for Maven to sync (if using Maven)
3. Navigate to any example class
4. Right-click and select "Run" or press the run button

## Learning Order

1. **Start Here**: `BasicThreadCreation.java`
   - Learn how to create threads
   - Understand Thread vs Runnable

2. **Next**: `ThreadLifecycle.java`
   - Understand thread states
   - Learn when threads transition between states

3. **Then**: `SynchronizationBasics.java`
   - Learn about race conditions
   - Understand synchronized keyword

4. **Continue**: `DeadlockExample.java`
   - Understand deadlock
   - Learn prevention strategies

5. **Progress**: `WaitNotifyExample.java`
   - Learn thread communication
   - Understand producer-consumer pattern

6. **Advanced**: Explore `threadpools/` and `advanced/` packages
   - Thread pools for efficient thread management
   - Advanced concepts for interviews

## Tips for Learning

- **Read the comments**: Each file has detailed comments explaining concepts
- **Run the code**: Execute each example to see it in action
- **Modify the code**: Change parameters, add more threads, experiment
- **Explain out loud**: Practice explaining the code as if in an interview
- **Take notes**: Write down key concepts and interview questions

## Interview Practice

For each example:
1. Read the code and comments
2. Run the code to see the output
3. Try to explain the concept without looking at comments
4. Practice answering: "What does this code demonstrate?"
5. Practice answering: "Why is this important?"

## Common Issues

**Issue**: Package errors in IDE
- **Solution**: Make sure your IDE recognizes this as a Maven project
- Right-click `pom.xml` → "Add as Maven Project" (IntelliJ)
- Or: File → Import → Existing Maven Project (Eclipse)

**Issue**: Cannot find main class
- **Solution**: Make sure you're running from the project root directory
- Check that the package structure matches the directory structure

**Issue**: Compilation errors
- **Solution**: Ensure Java 11+ is installed and configured
- Check: `java -version` should show 11 or higher

---

Happy Learning! 🎓

