package com.harshit.leetcode.arrays;

import java.util.*;

/**
 * Problem: Isomorphic Strings
 * 
 * Given two strings s and t, determine if they are isomorphic.
 * 
 * Two strings s and t are isomorphic if the characters in s can be replaced to get t.
 * 
 * All occurrences of a character must be replaced with another character while preserving
 * the order of characters. No two characters may map to the same character, but a character
 * may map to itself.
 * 
 * Example 1:
 * Input: s = "egg", t = "add"
 * Output: true
 * 
 * Example 2:
 * Input: s = "foo", t = "bar"
 * Output: false
 * 
 * Example 3:
 * Input: s = "paper", t = "title"
 * Output: true
 * 
 * Constraints:
 * - 1 <= s.length <= 5 * 10^4
 * - t.length == s.length
 * - s and t consist of any valid ascii character.
 */
public class IsomorphicStrings {
    
    /**
     * Solution using HashMap (O(n) time, O(n) space)
     * 
     * @param s First string
     * @param t Second string
     * @return true if isomorphic, false otherwise
     */
    public boolean isIsomorphic(String s, String t) {
        // Write your logic here
        // Hint: Use two HashMaps: s->t and t->s
        // Check bijection: each char in s maps to one char in t, and vice versa
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        IsomorphicStrings solution = new IsomorphicStrings();
        
        // Test case 1
        String s1 = "egg";
        String t1 = "add";
        boolean result1 = solution.isIsomorphic(s1, t1);
        System.out.println("Test 1 - s=\"egg\", t=\"add\"");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        String s2 = "foo";
        String t2 = "bar";
        boolean result2 = solution.isIsomorphic(s2, t2);
        System.out.println("Test 2 - s=\"foo\", t=\"bar\"");
        System.out.println("Expected: false, Got: " + result2);
    }
}

