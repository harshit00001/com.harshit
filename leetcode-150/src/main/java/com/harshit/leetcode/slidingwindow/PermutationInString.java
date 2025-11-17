package com.harshit.leetcode.slidingwindow;

/**
 * Problem: Permutation in String
 * 
 * Given two strings s1 and s2, return true if s2 contains a permutation of s1, or false otherwise.
 * 
 * In other words, return true if one of s1's permutations is the substring of s2.
 * 
 * Example 1:
 * Input: s1 = "ab", s2 = "eidbaooo"
 * Output: true
 * Explanation: s2 contains one permutation of s1 ("ba").
 * 
 * Example 2:
 * Input: s1 = "ab", s2 = "eidboaoo"
 * Output: false
 * 
 * Constraints:
 * - 1 <= s1.length, s2.length <= 10^4
 * - s1 and s2 consist of lowercase English letters.
 */
public class PermutationInString {
    
    /**
     * Solution using sliding window (O(n) time, O(1) space)
     * 
     * @param s1 Pattern string
     * @param s2 Source string
     * @return true if s2 contains permutation of s1
     */
    public boolean checkInclusion(String s1, String s2) {
        // Write your logic here
        // Hint: Use sliding window of size s1.length()
        // Count character frequencies in s1
        // Slide window in s2 and check if frequencies match
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        PermutationInString solution = new PermutationInString();
        
        // Test case 1
        String s1_1 = "ab";
        String s2_1 = "eidbaooo";
        boolean result1 = solution.checkInclusion(s1_1, s2_1);
        System.out.println("Test 1 - s1=\"ab\", s2=\"eidbaooo\"");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        String s1_2 = "ab";
        String s2_2 = "eidboaoo";
        boolean result2 = solution.checkInclusion(s1_2, s2_2);
        System.out.println("Test 2 - s1=\"ab\", s2=\"eidboaoo\"");
        System.out.println("Expected: false, Got: " + result2);
    }
}

