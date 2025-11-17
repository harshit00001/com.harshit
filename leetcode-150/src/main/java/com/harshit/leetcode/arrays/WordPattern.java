package com.harshit.leetcode.arrays;

import java.util.*;

/**
 * Problem: Word Pattern
 * 
 * Given a pattern and a string s, find if s follows the same pattern.
 * 
 * Here follow means a full match, such that there is a bijection between a letter in pattern
 * and a non-empty word in s.
 * 
 * Example 1:
 * Input: pattern = "abba", s = "dog cat cat dog"
 * Output: true
 * 
 * Example 2:
 * Input: pattern = "abba", s = "dog cat cat fish"
 * Output: false
 * 
 * Example 3:
 * Input: pattern = "aaaa", s = "dog cat cat dog"
 * Output: false
 * 
 * Constraints:
 * - 1 <= pattern.length <= 300
 * - pattern contains only lower-case English letters.
 * - 1 <= s.length <= 3000
 * - s contains only lowercase English letters and spaces ' '.
 * - s does not contain any leading or trailing spaces.
 * - All the words in s are separated by a single space.
 */
public class WordPattern {
    
    /**
     * Solution using HashMap (O(n) time, O(n) space)
     * 
     * @param pattern Pattern string
     * @param s String to check
     * @return true if s follows pattern, false otherwise
     */
    public boolean wordPattern(String pattern, String s) {
        // Write your logic here
        // Hint: Use two HashMaps: pattern char -> word, and word -> pattern char
        // Check bijection: each char maps to one word, each word maps to one char
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        WordPattern solution = new WordPattern();
        
        // Test case 1
        String pattern1 = "abba";
        String s1 = "dog cat cat dog";
        boolean result1 = solution.wordPattern(pattern1, s1);
        System.out.println("Test 1 - pattern=\"abba\", s=\"dog cat cat dog\"");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        String pattern2 = "abba";
        String s2 = "dog cat cat fish";
        boolean result2 = solution.wordPattern(pattern2, s2);
        System.out.println("Test 2 - pattern=\"abba\", s=\"dog cat cat fish\"");
        System.out.println("Expected: false, Got: " + result2);
    }
}

