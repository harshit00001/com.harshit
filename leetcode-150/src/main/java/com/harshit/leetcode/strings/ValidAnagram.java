package com.harshit.leetcode.strings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Valid Anagram
 * 
 * Given two strings s and t, return true if t is an anagram of s, and false otherwise.
 * 
 * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase,
 * typically using all the original letters exactly once.
 * 
 * Example 1:
 * Input: s = "anagram", t = "nagaram"
 * Output: true
 * 
 * Example 2:
 * Input: s = "rat", t = "car"
 * Output: false
 * 
 * Constraints:
 * - 1 <= s.length, t.length <= 5 * 10^4
 * - s and t consist of lowercase English letters.
 * 
 * Follow up: What if the inputs contain Unicode characters? How would you adapt your solution?
 */
public class ValidAnagram {
    
    /**
     * Solution using sorting (O(n log n) time, O(1) space if ignoring sort space)
     * 
     * @param s First string
     * @param t Second string
     * @return true if strings are anagrams, false otherwise
     */
    public boolean isAnagram(String s, String t) {
        // Write your logic here
        // Hint: Sort both strings and compare
        // If sorted strings are equal, they are anagrams
        return false;
    }
    
    /**
     * Solution using HashMap (O(n) time, O(n) space)
     * 
     * @param s First string
     * @param t Second string
     * @return true if strings are anagrams, false otherwise
     */
    public boolean isAnagramHashMap(String s, String t) {
        // Write your logic here
        // Hint: Count frequency of each character in both strings
        // Compare character frequencies
        return false;
    }
    
    /**
     * Solution using array for character counting (O(n) time, O(1) space)
     * 
     * @param s First string
     * @param t Second string
     * @return true if strings are anagrams, false otherwise
     */
    public boolean isAnagramArray(String s, String t) {
        // Write your logic here
        // Hint: Use int[26] array to count characters
        // Increment for s, decrement for t
        // All counts should be 0 if anagrams
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ValidAnagram solution = new ValidAnagram();
        
        // Test case 1
        String s1 = "anagram";
        String t1 = "nagaram";
        boolean result1 = solution.isAnagram(s1, t1);
        System.out.println("Test 1 - Input: s=\"anagram\", t=\"nagaram\"");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        String s2 = "rat";
        String t2 = "car";
        boolean result2 = solution.isAnagram(s2, t2);
        System.out.println("Test 2 - Input: s=\"rat\", t=\"car\"");
        System.out.println("Expected: false, Got: " + result2);
        
        // Test case 3
        String s3 = "listen";
        String t3 = "silent";
        boolean result3 = solution.isAnagram(s3, t3);
        System.out.println("Test 3 - Input: s=\"listen\", t=\"silent\"");
        System.out.println("Expected: true, Got: " + result3);
    }
}

