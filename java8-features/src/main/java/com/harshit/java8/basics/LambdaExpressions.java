package com.harshit.java8.basics;

import java.util.*;
import java.util.function.Predicate;

/**
 * LAMBDA EXPRESSIONS - Interview Explanation:
 * 
 * Lambda: Anonymous function (function without a name)
 * 
 * Problem: Before Java 8, we needed to create anonymous inner classes for
 * simple operations, which was verbose.
 * 
 * Solution: Lambda expressions provide a concise way to represent
 * functional interfaces (interfaces with single abstract method).
 * 
 * Simple Explanation:
 * - Short way to write a function
 * - Like a mini-function you can pass around
 * - Makes code more readable and concise
 * 
 * Syntax: (parameters) -> expression
 *         (parameters) -> { statements; }
 */
public class LambdaExpressions {

    public static void main(String[] args) {
        System.out.println("=== LAMBDA EXPRESSIONS ===\n");
        
        demonstrateBasicLambda();
        demonstrateLambdaWithCollections();
        demonstrateFunctionalInterfaces();
        demonstrateMethodReferences();
    }
    
    /**
     * Interview Point: Basic Lambda Syntax
     */
    private static void demonstrateBasicLambda() {
        System.out.println("--- Basic Lambda Syntax ---");
        
        // Interview Point: Old way - Anonymous inner class
        Runnable oldWay = new Runnable() {
            @Override
            public void run() {
                System.out.println("  Old way: Anonymous inner class");
            }
        };
        oldWay.run();
        
        // Interview Point: New way - Lambda expression
        Runnable newWay = () -> System.out.println("  New way: Lambda expression");
        newWay.run();
        
        // Interview Point: Lambda with parameters
        java.util.function.Consumer<String> printer = (name) -> 
            System.out.println("  Hello, " + name);
        printer.accept("John");
        
        // Interview Point: Lambda with multiple statements
        java.util.function.Consumer<Integer> calculator = (num) -> {
            int square = num * num;
            System.out.println("  Square of " + num + " is " + square);
        };
        calculator.accept(5);
        
        System.out.println();
    }
    
    /**
     * Interview Point: Lambda with Collections
     */
    private static void demonstrateLambdaWithCollections() {
        System.out.println("--- Lambda with Collections ---");
        
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
        
        // Interview Point: forEach with lambda
        System.out.println("  Using forEach:");
        names.forEach(name -> System.out.println("    " + name));
        
        // Interview Point: Filter with lambda (using Stream - covered later)
        System.out.println("  Filtering names starting with 'C':");
        names.stream()
            .filter(name -> name.startsWith("C"))
            .forEach(name -> System.out.println("    " + name));
        
        // Interview Point: Sort with lambda
        System.out.println("  Sorting by length:");
        names.sort((a, b) -> a.length() - b.length());
        names.forEach(name -> System.out.println("    " + name));
        
        System.out.println();
    }
    
    /**
     * Interview Point: Functional Interfaces with Lambda
     */
    private static void demonstrateFunctionalInterfaces() {
        System.out.println("--- Functional Interfaces ---");
        
        // Interview Point: Predicate - returns boolean
        Predicate<Integer> isEven = num -> num % 2 == 0;
        System.out.println("  Is 10 even? " + isEven.test(10));
        System.out.println("  Is 7 even? " + isEven.test(7));
        
        // Interview Point: Function - takes input, returns output
        java.util.function.Function<String, Integer> length = str -> str.length();
        System.out.println("  Length of 'Hello': " + length.apply("Hello"));
        
        // Interview Point: Consumer - takes input, returns nothing
        java.util.function.Consumer<String> greet = name -> 
            System.out.println("  Hello, " + name);
        greet.accept("World");
        
        // Interview Point: Supplier - takes no input, returns output
        java.util.function.Supplier<String> supplier = () -> "Hello from Supplier";
        System.out.println("  " + supplier.get());
        
        System.out.println();
    }
    
    /**
     * Interview Point: Method References
     * Shorthand for lambda when calling existing method
     */
    private static void demonstrateMethodReferences() {
        System.out.println("--- Method References ---");
        
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        
        // Interview Point: Lambda way
        names.forEach(name -> System.out.println(name));
        
        // Interview Point: Method reference way (shorter)
        names.forEach(System.out::println);
        
        // Interview Point: Static method reference
        names.forEach(LambdaExpressions::printUpperCase);
        
        System.out.println();
    }
    
    private static void printUpperCase(String str) {
        System.out.println(str.toUpperCase());
    }
}

/**
 * INTERVIEW SUMMARY: Lambda Expressions
 * 
 * Key Points:
 * 1. Syntax: (params) -> expression
 * 2. Used with functional interfaces
 * 3. Makes code concise and readable
 * 4. Can replace anonymous inner classes
 * 
 * Common Functional Interfaces:
 * - Predicate<T>: boolean test(T t)
 * - Function<T,R>: R apply(T t)
 * - Consumer<T>: void accept(T t)
 * - Supplier<T>: T get()
 * - BiFunction<T,U,R>: R apply(T t, U u)
 */

