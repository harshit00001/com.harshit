package com.harshit.leetcode.arrays;

import java.util.*;

/**
 * Problem: Find All Anagrams in a String
 * 
 * Given two strings s and p, return an array of all the start indices of p's anagrams in s.
 * You may return the answer in any order.
 * 
 * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase,
 * typically using all the original letters exactly once.
 * 
 * Example 1:
 * Input: s = "cbaebabacd", p = "abc"
 * Output: [0,6]
 * Explanation:
 * The substring with start index = 0 is "cba", which is an anagram of "abc".
 * The substring with start index = 6 is "bac", which is an anagram of "abc".
 * 
 * Example 2:
 * Input: s = "abab", p = "ab"
 * Output: [0,1,2]
 * Explanation:
 * The substring with start index = 0 is "ab", which is an anagram of "ab".
 * The substring with start index = 1 is "ba", which is an anagram of "ab".
 * The substring with start index = 2 is "ab", which is an anagram of "ab".
 * 
 * Constraints:
 * - 1 <= s.length, p.length <= 3 * 10^4
 * - s and p consist of lowercase English letters.
 */
public class FindAllAnagrams {
    
    /**
     * Solution using sliding window (O(n) time, O(1) space)
     * 
     * @param s Source string
     * @param p Pattern string
     * @return List of start indices of anagrams
     */
    public List<Integer> findAnagrams(String s, String p) {
        // Write your logic here
        // Hint: Use sliding window of size p.length()
        // Count character frequencies in p
        // Slide window and check if frequencies match
        return new ArrayList<>();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        FindAllAnagrams solution = new FindAllAnagrams();
        
        // Test case 1
        String s1 = "cbaebabacd";
        String p1 = "abc";
        List<Integer> result1 = solution.findAnagrams(s1, p1);
        System.out.println("Test 1 - Input: s=\"cbaebabacd\", p=\"abc\"");
        System.out.println("Result: " + result1);
        System.out.println("Expected: [0,6]");
        
        // Test case 2
        String s2 = "abab";
        String p2 = "ab";
        List<Integer> result2 = solution.findAnagrams(s2, p2);
        System.out.println("Test 2 - Input: s=\"abab\", p=\"ab\"");
        System.out.println("Result: " + result2);
        System.out.println("Expected: [0,1,2]");
    }
}

