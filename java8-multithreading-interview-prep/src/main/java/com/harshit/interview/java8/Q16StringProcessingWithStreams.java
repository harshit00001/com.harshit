package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * INTERVIEW Q: Reverse words in a string? Frequency of character / each character using Java 8?
 *
 * <p>Very common Accenture coding + verbal round.
 */
public final class Q16StringProcessingWithStreams implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q16: String Processing (Streams) ===\n");

        String sentence = "Java eight streams are powerful";

        // Reverse word order (not reverse letters inside words)
        String reversedWords = java.util.Arrays.stream(sentence.trim().split("\\s+"))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    java.util.Collections.reverse(list);
                    return String.join(" ", list);
                }));
        System.out.println("Reversed words: " + reversedWords);

        // Frequency of 's' in avinash
        String input = "avinash";
        long sCount = input.chars().filter(ch -> ch == 's').count();
        System.out.println("Frequency of 's' in avinash: " + sCount);

        // Frequency of each character
        Map<Character, Long> charFreq = input.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        System.out.println("Char frequency: " + charFreq);

        // Reverse each word letters
        String reverseEachWord = java.util.Arrays.stream(sentence.split("\\s+"))
                .map(word -> new StringBuilder(word).reverse().toString())
                .collect(Collectors.joining(" "));
        System.out.println("Each word reversed: " + reverseEachWord);

        // Palindrome check with stream (interview variant)
        String word = "madam";
        boolean palindrome = word.equals(new StringBuilder(word).reverse().toString());
        System.out.println(word + " palindrome? " + palindrome);

//        Optional<Integer> maxSalary = employees.stream()
//                .collect(Collectors.collectingAndThen(
//                        Collectors.maxBy(Comparator.naturalOrder()),
//                        result -> result
//                ));
    }

    public static void main(String[] args) throws Exception {
        new Q16StringProcessingWithStreams().run();
    }
}
