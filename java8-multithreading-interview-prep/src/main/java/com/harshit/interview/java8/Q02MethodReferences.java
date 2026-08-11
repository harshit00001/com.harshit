package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * INTERVIEW Q: Explain four types of method references in Java 8.
 *
 * <ol>
 *   <li>Static: {@code ClassName::staticMethod}</li>
 *   <li>Instance on particular object: {@code obj::instanceMethod}</li>
 *   <li>Instance on arbitrary object: {@code ClassName::instanceMethod} (first arg becomes receiver)</li>
 *   <li>Constructor: {@code ClassName::new}</li>
 * </ol>
 *
 * <p><b>Rule:</b> Method reference is shorthand when lambda only delegates to one method:
 * {@code x -> System.out.println(x)} → {@code System.out::println}
 */
public final class Q02MethodReferences implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q02: Method References ===\n");

        List<String> names = List.of("raj", "priya", "amit");

        // 1) Static method reference
        names.stream()
                .map(String::valueOf) // redundant here, but shows static ref pattern
                .forEach(System.out::println); // actually instance ref on System.out

        System.out.println();

        // 3) Instance method on arbitrary object: String::compareToIgnoreCase
        names.stream()
                .sorted(String::compareToIgnoreCase)
                .forEach(System.out::println);

        System.out.println();

        // 4) Constructor reference
        Function<String, StringBuilder> toBuilder = StringBuilder::new;
        System.out.println("Built: " + toBuilder.apply("Accenture"));

        // Constructor reference with args → array constructor
        Function<Integer, int[]> arrayMaker = int[]::new;
        int[] data = arrayMaker.apply(5);
        System.out.println("Array length: " + data.length);

        // BiFunction + constructor
        BiFunction<String, Integer, String> repeat = String::repeat; // Java 11+, illustrates pattern
        System.out.println("Repeat: " + repeat.apply("Ha", 3));

        System.out.println("\n→ Interview tip: If lambda has extra logic, keep lambda; don't force method ref.");
    }

    public static void main(String[] args) throws Exception {
        new Q02MethodReferences().run();
    }
}
