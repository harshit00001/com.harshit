package com.harshit.leetcode.slidingwindow;

import java.util.*;

/**
 * Problem: Longest Substring Without Repeating Characters
 * 
 * Given a string s, find the length of the longest substring without repeating characters.
 * 
 * Example 1:
 * Input: s = "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 * 
 * Example 2:
 * Input: s = "bbbbb"
 * Output: 1
 * Explanation: The answer is "b", with the length of 1.
 * 
 * Example 3:
 * Input: s = "pwwkew"
 * Output: 3
 * Explanation: The answer is "wke", with the length of 3.
 * Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
 * 
 * Constraints:
 * - 0 <= s.length <= 5 * 10^4
 * - s consists of English letters, digits, symbols and spaces.
 */
public class LongestSubstringWithoutRepeatingCharacters {
    
    /**
     * Solution using sliding window with HashMap (O(n) time, O(min(n,m)) space)
     * 
     * @param s Input string
     * @return Length of longest substring without repeating characters
     */
    public int lengthOfLongestSubstring(String s) {
        // Write your logic here
        // Hint: Use sliding window with HashMap to track character positions
        // Expand window by moving right pointer
        // When duplicate found, move left pointer to position after duplicate
        return 0;
    }
    
    /**
     * Solution using sliding window with HashSet (O(n) time, O(min(n,m)) space)
     * 
     * @param s Input string
     * @return Length of longest substring without repeating characters
     */
    public int lengthOfLongestSubstringSet(String s) {
        // Write your logic here
        // Hint: Use HashSet to track characters in current window
        // Remove characters from set when shrinking window
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        LongestSubstringWithoutRepeatingCharacters solution = new LongestSubstringWithoutRepeatingCharacters();
        
        // Test case 1
        String s1 = "abcabcbb";
        int result1 = solution.lengthOfLongestSubstring(s1);
        System.out.println("Test 1 - Input: \"abcabcbb\"");
        System.out.println("Expected: 3, Got: " + result1);
        
        // Test case 2
        String s2 = "bbbbb";
        int result2 = solution.lengthOfLongestSubstring(s2);
        System.out.println("Test 2 - Input: \"bbbbb\"");
        System.out.println("Expected: 1, Got: " + result2);
        
        // Test case 3
        String s3 = "pwwkew";
        int result3 = solution.lengthOfLongestSubstring(s3);
        System.out.println("Test 3 - Input: \"pwwkew\"");
        System.out.println("Expected: 3, Got: " + result3);
    }
}

