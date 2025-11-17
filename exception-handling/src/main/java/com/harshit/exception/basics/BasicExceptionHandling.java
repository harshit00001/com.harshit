package com.harshit.exception.basics;

/**
 * BASIC EXCEPTION HANDLING - Interview Explanation:
 * 
 * Exception: An event that disrupts the normal flow of program execution.
 * 
 * Types of Exceptions:
 * 1. Checked Exceptions: Must be handled (compile-time)
 *    - Examples: IOException, SQLException, FileNotFoundException
 * 2. Unchecked Exceptions: Don't need to be handled (runtime)
 *    - Examples: NullPointerException, ArrayIndexOutOfBoundsException
 * 3. Errors: Serious problems (usually not recoverable)
 *    - Examples: OutOfMemoryError, StackOverflowError
 * 
 * Simple Explanation:
 * - Checked: Like a contract - you MUST handle it
 * - Unchecked: Like a surprise - might happen, but not required to handle
 * - Error: Like a system crash - usually can't recover
 */
public class BasicExceptionHandling {

    public static void main(String[] args) {
        System.out.println("=== BASIC EXCEPTION HANDLING ===\n");
        
        demonstrateTryCatch();
        demonstrateFinally();
        demonstrateMultipleCatch();
        demonstrateNestedTryCatch();
        demonstrateThrow();
        demonstrateThrows();
    }
    
    /**
     * Interview Point: try-catch block
     * 
     * Technical: Catches and handles exceptions
     * Simple: Try to do something, if it fails, catch the error and handle it
     */
    private static void demonstrateTryCatch() {
        System.out.println("--- Try-Catch Block ---");
        
        try {
            int result = 10 / 0; // This will throw ArithmeticException
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("  ✗ Caught exception: " + e.getMessage());
            System.out.println("  → Division by zero is not allowed");
        }
        
        System.out.println("Program continues after exception handling\n");
    }
    
    /**
     * Interview Point: finally block
     * 
     * Technical: Always executes, regardless of exception
     * Simple: Code that MUST run no matter what (like closing files)
     */
    private static void demonstrateFinally() {
        System.out.println("--- Finally Block ---");
        
        try {
            int[] numbers = {1, 2, 3};
            System.out.println("  Accessing array: " + numbers[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("  ✗ Array index out of bounds");
        } finally {
            System.out.println("  ✓ Finally block always executes");
            System.out.println("  → Use for cleanup: close files, release resources");
        }
        
        System.out.println();
    }
    
    /**
     * Interview Point: Multiple catch blocks
     * 
     * Technical: Handle different exception types differently
     * Simple: Different errors need different handling
     */
    private static void demonstrateMultipleCatch() {
        System.out.println("--- Multiple Catch Blocks ---");
        
        try {
            String str = null;
            int length = str.length(); // NullPointerException
            int result = 10 / 0; // ArithmeticException (won't reach here)
        } catch (NullPointerException e) {
            System.out.println("  ✗ Caught NullPointerException: " + e.getMessage());
            System.out.println("  → Object is null");
        } catch (ArithmeticException e) {
            System.out.println("  ✗ Caught ArithmeticException: " + e.getMessage());
            System.out.println("  → Division by zero");
        } catch (Exception e) {
            System.out.println("  ✗ Caught general Exception: " + e.getMessage());
            System.out.println("  → Catch-all for any other exception");
        }
        
        System.out.println();
    }
    
    /**
     * Interview Point: Nested try-catch
     * 
     * Technical: try-catch inside another try-catch
     * Simple: Handling errors at different levels
     */
    private static void demonstrateNestedTryCatch() {
        System.out.println("--- Nested Try-Catch ---");
        
        try {
            System.out.println("  Outer try block");
            try {
                int result = 10 / 0;
            } catch (ArithmeticException e) {
                System.out.println("    ✗ Inner catch: " + e.getMessage());
                throw new RuntimeException("Re-throwing as RuntimeException", e);
            }
        } catch (RuntimeException e) {
            System.out.println("  ✗ Outer catch: " + e.getMessage());
            System.out.println("  → Caught exception from inner block");
        }
        
        System.out.println();
    }
    
    /**
     * Interview Point: throw keyword
     * 
     * Technical: Explicitly throw an exception
     * Simple: Intentionally create and throw an error
     */
    private static void demonstrateThrow() {
        System.out.println("--- Throw Keyword ---");
        
        try {
            validateAge(15); // This will throw exception
        } catch (IllegalArgumentException e) {
            System.out.println("  ✗ Caught: " + e.getMessage());
        }
        
        try {
            validateAge(25); // This is valid
            System.out.println("  ✓ Age validated successfully");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✗ Caught: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void validateAge(int age) {
        if (age < 18) {
            // Interview Point: throw creates and throws exception
            throw new IllegalArgumentException("Age must be 18 or older");
        }
    }
    
    /**
     * Interview Point: throws keyword
     * 
     * Technical: Declares that method might throw exception
     * Simple: Warning that this method can cause an error
     */
    private static void demonstrateThrows() {
        System.out.println("--- Throws Keyword ---");
        
        try {
            readFile("nonexistent.txt");
        } catch (Exception e) {
            System.out.println("  ✗ Caught: " + e.getMessage());
            System.out.println("  → Method declared it might throw exception");
        }
        
        System.out.println();
    }
    
    /**
     * Interview Point: Method declares it throws Exception
     * Caller must handle it
     */
    private static void readFile(String filename) throws Exception {
        // Simulate file reading
        if (filename == null || filename.isEmpty()) {
            throw new Exception("Filename cannot be null or empty");
        }
        // In real code, this would read file
    }
}

/**
 * INTERVIEW SUMMARY: Basic Exception Handling
 * 
 * Key Points:
 * 1. try: Code that might throw exception
 * 2. catch: Handle the exception
 * 3. finally: Always executes (cleanup code)
 * 4. throw: Create and throw exception
 * 5. throws: Declare exception in method signature
 * 
 * Best Practices:
 * - Always close resources in finally or use try-with-resources
 * - Catch specific exceptions first, then general
 * - Don't catch and ignore (at least log it)
 * - Provide meaningful error messages
 */

