# Exception Handling - Complete Guide

A comprehensive Java project covering exception handling from basics to Spring Boot, with interview-friendly explanations and code examples.

## 📚 Topics Covered

### 1. **Basics** (`basics/`)
- Try-catch-finally blocks
- Multiple catch blocks
- Nested try-catch
- throw and throws keywords
- Checked vs Unchecked exceptions

### 2. **Advanced** (`advanced/`)
- Custom exceptions (Checked and Unchecked)
- Exception chaining
- Exception with additional information
- Best practices

### 3. **Spring Boot** (`springboot/`)
- @ExceptionHandler
- @ControllerAdvice for global handling
- @ResponseStatus
- ResponseEntity for error responses
- Validation exception handling

## 🎯 Interview Questions & Answers

### Q: What is the difference between checked and unchecked exceptions?

**Technical:**
- **Checked Exceptions**: Must be handled at compile-time. Extend `Exception` (but not `RuntimeException`). Compiler forces you to handle them.
- **Unchecked Exceptions**: Don't need to be handled. Extend `RuntimeException` or `Error`. Compiler doesn't force handling.

**Simple:**
- Checked: Like a contract - you MUST handle it or declare it
- Unchecked: Like a surprise - might happen, but not required to handle

**Examples:**
```java
// Checked - must handle
try {
    FileReader file = new FileReader("file.txt");
} catch (IOException e) { // Must catch
    e.printStackTrace();
}

// Unchecked - optional to handle
int[] arr = new int[5];
int value = arr[10]; // ArrayIndexOutOfBoundsException - no need to catch
```

---

### Q: What is the difference between throw and throws?

**Technical:**
- **throw**: Explicitly throws an exception object. Used inside method body.
- **throws**: Declares that a method might throw an exception. Used in method signature.

**Simple:**
- throw: "I'm creating and throwing an error right now"
- throws: "This method might cause an error, be warned"

**Example:**
```java
// throws in method signature
public void readFile() throws IOException {
    // throw inside method
    if (file == null) {
        throw new IOException("File not found");
    }
}
```

---

### Q: What is the finally block and when is it used?

**Technical:**
- `finally` block always executes, regardless of whether exception occurred or not.
- Used for cleanup code (closing files, releasing resources).
- Even if return statement is in try or catch, finally executes.

**Simple:**
- Code that MUST run no matter what happens
- Like cleaning up after yourself, even if something goes wrong

**Example:**
```java
FileReader file = null;
try {
    file = new FileReader("file.txt");
    // read file
} catch (IOException e) {
    e.printStackTrace();
} finally {
    // Always executes
    if (file != null) {
        file.close(); // Cleanup
    }
}
```

---

### Q: What is try-with-resources?

**Technical:**
- Introduced in Java 7
- Automatically closes resources that implement `AutoCloseable`
- No need for finally block to close resources
- Resources are closed in reverse order

**Simple:**
- Automatic cleanup - you don't need to remember to close files
- Java does it for you automatically

**Example:**
```java
// Old way
FileReader file = null;
try {
    file = new FileReader("file.txt");
} finally {
    if (file != null) file.close();
}

// New way (try-with-resources)
try (FileReader file = new FileReader("file.txt")) {
    // use file
} // Automatically closed here
```

---

### Q: What is exception chaining?

**Technical:**
- Wrapping one exception inside another
- Preserves original exception as the cause
- Helps in debugging by maintaining full stack trace

**Simple:**
- Catching an error, wrapping it in a new error, but keeping the original error inside
- Like putting a box inside another box - both are there

**Example:**
```java
try {
    // some code
} catch (SQLException e) {
    throw new DataAccessException("Database error", e); // e is the cause
}
```

---

### Q: How do you handle exceptions in Spring Boot?

**Technical:**
1. **@ExceptionHandler**: Handle exceptions in a specific controller
2. **@ControllerAdvice**: Global exception handler for all controllers
3. **@ResponseStatus**: Set HTTP status code on exception class
4. **ResponseEntity**: Return custom error responses

**Simple:**
- Instead of try-catch everywhere, handle errors in one place
- Return proper HTTP status codes and error messages to clients

**Example:**
```java
@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundException e) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
        return ResponseEntity.status(404).body(error);
    }
}
```

---

### Q: What is @ControllerAdvice?

**Technical:**
- Spring annotation for global exception handling
- Applies to all controllers in the application
- Can handle exceptions from any controller
- Centralized error handling

**Simple:**
- One place to handle all errors in your entire application
- Like a central error handling department

**Example:**
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception e) {
        // Handle all exceptions here
    }
}
```

---

### Q: When should you create custom exceptions?

**Technical:**
- When built-in exceptions don't clearly describe the error
- For domain-specific errors
- When you need to carry additional information
- For better code readability and maintainability

**Simple:**
- When generic errors don't explain your specific problem
- Like creating your own error types for your specific business logic

**Example:**
```java
// Instead of generic Exception
throw new Exception("Error");

// Use custom exception
throw new InsufficientFundsException(balance, amount);
```

---

## 📝 Best Practices

1. **Always close resources**: Use try-with-resources
2. **Catch specific exceptions first**: More specific before general
3. **Don't catch and ignore**: At least log the exception
4. **Provide meaningful messages**: Help with debugging
5. **Use appropriate exception types**: Checked for recoverable, unchecked for programming errors
6. **Don't expose internal details**: In production, don't show stack traces to users
7. **Log exceptions**: For debugging and monitoring
8. **Use global exception handlers**: In Spring Boot, use @ControllerAdvice

---

## 🚀 Running the Examples

1. Navigate to project directory
2. Compile: `javac -d target/classes src/main/java/com/harshit/exception/**/*.java`
3. Run: `java -cp target/classes com.harshit.exception.basics.BasicExceptionHandling`

---

**Happy Learning! 🎓**

