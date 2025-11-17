package com.harshit.leetcode.slidingwindow;

/**
 * Problem: Longest Repeating Character Replacement
 * 
 * You are given a string s and an integer k. You can choose any character of the string and
 * change it to any other uppercase English letter. You can perform this operation at most k times.
 * 
 * Return the length of the longest substring containing the same letter you can get after
 * performing the above operations.
 * 
 * Example 1:
 * Input: s = "ABAB", k = 2
 * Output: 4
 * Explanation: Replace the two 'A's with two 'B's or vice versa.
 * 
 * Example 2:
 * Input: s = "AABABBA", k = 1
 * Output: 4
 * Explanation: Replace the one 'A' in the middle with 'B' and form "AABBBBA".
 * The substring "BBBB" has the longest repeating letters, which is 4.
 * 
 * Constraints:
 * - 1 <= s.length <= 10^5
 * - s consists of only uppercase English letters.
 * - 0 <= k <= s.length
 */
public class LongestRepeatingCharacterReplacement {
    
    /**
     * Solution using sliding window (O(n) time, O(1) space)
     * 
     * @param s Input string
     * @param k Maximum replacements allowed
     * @return Length of longest substring with same character
     */
    public int characterReplacement(String s, int k) {
        // Write your logic here
        // Hint: Use sliding window
        // Track frequency of each character in window
        // If (window_size - max_frequency) > k, shrink window
        // Update max length
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        LongestRepeatingCharacterReplacement solution = new LongestRepeatingCharacterReplacement();
        
        // Test case 1
        String s1 = "ABAB";
        int k1 = 2;
        int result1 = solution.characterReplacement(s1, k1);
        System.out.println("Test 1 - Input: s=\"ABAB\", k=2");
        System.out.println("Expected: 4, Got: " + result1);
        
        // Test case 2
        String s2 = "AABABBA";
        int k2 = 1;
        int result2 = solution.characterReplacement(s2, k2);
        System.out.println("Test 2 - Input: s=\"AABABBA\", k=1");
        System.out.println("Expected: 4, Got: " + result2);
    }
}

