package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * INTERVIEW Q: Top Streams coding questions (strings) — Accenture 4 YOE level.
 *
 * <p>Consolidates common hands-on patterns from real interviews.
 */
public final class Q09StreamsCodingInterviewQuestions implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q09: Streams Coding Q&A ===\n");

        List<String> fruits = Arrays.asList("Apple", "Orange", "Papaya", "apple", "Banana", "Apricot");

        // 1) Uppercase, sort, print
        System.out.println("1) Uppercase sorted:");
        fruits.stream()
                .map(String::toUpperCase)
                .sorted()
                .forEach(s -> System.out.println("   " + s));

        // 2) Count length > 5
        long count = fruits.stream().filter(s -> s.length() > 5).count();
        System.out.println("\n2) Count length > 5: " + count);

        // 3) Distinct (case-sensitive; use map to lower for case-insensitive)
        System.out.println("\n3) Distinct:");
        fruits.stream().distinct().forEach(s -> System.out.println("   " + s));

        // 4) Contains word
        System.out.println("\n4) Contains 'Apple':");
        fruits.stream().filter(s -> s.contains("Apple")).forEach(s -> System.out.println("   " + s));

        // 5) Remove starting with prefix (keep others)
        System.out.println("\n5) Not starting with 'A':");
        fruits.stream().filter(s -> !s.startsWith("A")).forEach(s -> System.out.println("   " + s));

        // 6) First starting with letter
        fruits.stream()
                .filter(s -> s.toLowerCase().startsWith("b"))
                .findFirst()
                .ifPresent(s -> System.out.println("\n6) First with 'b': " + s));

        // 7) Length of each
        System.out.println("\n7) Lengths:");
        fruits.stream().map(String::length).forEach(len -> System.out.println("   " + len));

        // 8) Sort by length
        System.out.println("\n8) Sort by length asc:");
        fruits.stream()
                .sorted((a, b) -> Integer.compare(a.length(), b.length()))
                .forEach(s -> System.out.println("   " + s));

        // 10) Vowels
        System.out.println("\n10) Has vowel:");
        fruits.stream()
                .filter(s -> s.matches("(?i).*[aeiou].*"))
                .forEach(s -> System.out.println("   " + s));

        // 11) Join
        String joined = fruits.stream().collect(Collectors.joining(", "));
        System.out.println("\n11) Joined: " + joined);

        // 12) Reverse each string
        System.out.println("\n12) Reversed strings:");
        fruits.stream()
                .map(s -> new StringBuilder(s).reverse().toString())
                .forEach(s -> System.out.println("   " + s));

        // 14) Group by first letter
        Map<Character, List<String>> byFirst = fruits.stream()
                .collect(Collectors.groupingBy(s -> s.charAt(0)));
        System.out.println("\n14) Group by first letter keys: " + byFirst.keySet());

        // 17) Partition vowel / no vowel
        Map<Boolean, List<String>> vowelParts = fruits.stream()
                .collect(Collectors.partitioningBy(s -> s.matches("(?i).*[aeiou].*")));
        System.out.println("\n17) With vowels: " + vowelParts.get(true));

        // Character frequency in a string (bonus — very common)
        String input = "accenture";
        Map<Character, Long> freq = input.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("\nBonus — char frequency in 'accenture': " + freq);
    }

    public static void main(String[] args) throws Exception {
        new Q09StreamsCodingInterviewQuestions().run();
    }
}
