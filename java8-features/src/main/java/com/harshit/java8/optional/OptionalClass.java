package com.harshit.java8.optional;

import java.util.Optional;

/**
 * OPTIONAL CLASS - Interview Explanation:
 * 
 * Problem: NullPointerException is common when dealing with null values.
 * 
 * Solution: Optional<T> is a container that may or may not contain a value.
 * It forces you to handle the null case explicitly.
 * 
 * Simple Explanation:
 * - Wrapper around a value that might be null
 * - Forces you to check if value exists before using
 * - Prevents NullPointerException
 * 
 * Key Methods:
 * - of(): Create Optional with non-null value
 * - ofNullable(): Create Optional that might be null
 * - isPresent(): Check if value exists
 * - get(): Get value (throws exception if empty)
 * - orElse(): Get value or default
 * - orElseGet(): Get value or compute default
 * - map(): Transform value if present
 */
public class OptionalClass {

    public static void main(String[] args) {
        System.out.println("=== OPTIONAL CLASS ===\n");
        
        demonstrateOptionalCreation();
        demonstrateOptionalMethods();
        demonstrateOptionalWithStreams();
        demonstrateBestPractices();
    }
    
    /**
     * Interview Point: Creating Optional
     */
    private static void demonstrateOptionalCreation() {
        System.out.println("--- Creating Optional ---");
        
        // Interview Point: Optional.of() - value must not be null
        Optional<String> optional1 = Optional.of("Hello");
        System.out.println("  Optional.of('Hello'): " + optional1);
        
        // Interview Point: Optional.ofNullable() - value can be null
        String name = null;
        Optional<String> optional2 = Optional.ofNullable(name);
        System.out.println("  Optional.ofNullable(null): " + optional2);
        
        // Interview Point: Optional.empty() - empty optional
        Optional<String> optional3 = Optional.empty();
        System.out.println("  Optional.empty(): " + optional3);
        
        System.out.println();
    }
    
    /**
     * Interview Point: Optional Methods
     */
    private static void demonstrateOptionalMethods() {
        System.out.println("--- Optional Methods ---");
        
        Optional<String> optional = Optional.of("World");
        
        // Interview Point: isPresent() - check if value exists
        if (optional.isPresent()) {
            System.out.println("  Value is present: " + optional.get());
        }
        
        // Interview Point: ifPresent() - execute if value exists
        optional.ifPresent(value -> System.out.println("  ifPresent: " + value));
        
        // Interview Point: orElse() - get value or default
        String value1 = optional.orElse("Default");
        System.out.println("  orElse: " + value1);
        
        Optional<String> empty = Optional.empty();
        String value2 = empty.orElse("Default");
        System.out.println("  orElse (empty): " + value2);
        
        // Interview Point: orElseGet() - get value or compute default
        String value3 = empty.orElseGet(() -> "Computed Default");
        System.out.println("  orElseGet: " + value3);
        
        // Interview Point: orElseThrow() - get value or throw exception
        try {
            String value4 = empty.orElseThrow(() -> new RuntimeException("No value"));
        } catch (RuntimeException e) {
            System.out.println("  orElseThrow: " + e.getMessage());
        }
        
        // Interview Point: map() - transform if present
        Optional<Integer> length = optional.map(String::length);
        System.out.println("  map (length): " + length.orElse(0));
        
        // Interview Point: flatMap() - flatten nested Optional
        Optional<String> upper = optional.flatMap(s -> Optional.of(s.toUpperCase()));
        System.out.println("  flatMap (uppercase): " + upper.orElse(""));
        
        System.out.println();
    }
    
    /**
     * Interview Point: Optional with Streams
     */
    private static void demonstrateOptionalWithStreams() {
        System.out.println("--- Optional with Streams ---");
        
        Optional<String> optional = Optional.of("Hello");
        
        // Interview Point: stream() - convert Optional to Stream
        optional.stream()
            .map(String::toUpperCase)
            .forEach(s -> System.out.println("  Stream: " + s));
        
        Optional<String> empty = Optional.empty();
        empty.stream()
            .forEach(s -> System.out.println("  This won't print"));
        
        System.out.println();
    }
    
    /**
     * Interview Point: Best Practices
     */
    private static void demonstrateBestPractices() {
        System.out.println("--- Best Practices ---");
        
        // Interview Point: Don't use Optional.get() without checking
        Optional<String> optional = Optional.of("Test");
        
        // Good: Use orElse or ifPresent
        String value = optional.orElse("Default");
        System.out.println("  Good: " + value);
        
        // Bad: Don't do this
        // String bad = optional.get(); // Can throw NoSuchElementException
        
        // Interview Point: Use Optional for return types, not parameters
        System.out.println("  → Use Optional as return type");
        System.out.println("  → Don't use Optional as method parameter");
        System.out.println("  → Don't use Optional for fields");
        
        System.out.println();
    }
}

/**
 * INTERVIEW SUMMARY: Optional
 * 
 * Key Points:
 * 1. Container for value that might be null
 * 2. Forces explicit null handling
 * 3. Prevents NullPointerException
 * 
 * Common Methods:
 * - of(), ofNullable(), empty()
 * - isPresent(), ifPresent()
 * - orElse(), orElseGet(), orElseThrow()
 * - map(), flatMap()
 * 
 * Best Practices:
 * - Use as return type
 * - Don't use as parameter or field
 * - Always provide default with orElse/orElseGet
 */

