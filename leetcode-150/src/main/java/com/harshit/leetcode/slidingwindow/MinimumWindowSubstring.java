package com.harshit.leetcode.slidingwindow;

/**
 * Problem: Minimum Window Substring
 * 
 * Given two strings s and t, return the minimum window substring of s such that every character
 * in t (including duplicates) is included in the window. If there is no such substring, return
 * the empty string "".
 * 
 * The testcases will be generated such that the answer is unique.
 * 
 * Example 1:
 * Input: s = "ADOBECODEBANC", t = "ABC"
 * Output: "BANC"
 * Explanation: The minimum window substring "BANC" includes 'A', 'B', and 'C' from string t.
 * 
 * Example 2:
 * Input: s = "a", t = "a"
 * Output: "a"
 * Explanation: The entire string s is the minimum window.
 * 
 * Example 3:
 * Input: s = "a", t = "aa"
 * Output: ""
 * Explanation: Both 'a's from t must be included in the window.
 * Since the largest window of s only has one 'a', return empty string.
 * 
 * Constraints:
 * - m == s.length
 * - n == t.length
 * - 1 <= m, n <= 10^5
 * - s and t consist of uppercase and lowercase English letters.
 * 
 * Follow up: Could you find an algorithm that runs in O(m + n) time?
 */
public class MinimumWindowSubstring {
    
    /**
     * Solution using sliding window (O(m + n) time, O(m + n) space)
     * 
     * @param s Source string
     * @param t Target string
     * @return Minimum window substring containing all characters of t
     */
    public String minWindow(String s, String t) {
        // Write your logic here
        // Hint: Use sliding window with character frequency map
        // Expand window until all characters of t are included
        // Then shrink window from left to find minimum
        return "";
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        MinimumWindowSubstring solution = new MinimumWindowSubstring();
        
        // Test case 1
        String s1 = "ADOBECODEBANC";
        String t1 = "ABC";
        String result1 = solution.minWindow(s1, t1);
        System.out.println("Test 1 - Input: s=\"ADOBECODEBANC\", t=\"ABC\"");
        System.out.println("Expected: \"BANC\", Got: \"" + result1 + "\"");
        
        // Test case 2
        String s2 = "a";
        String t2 = "a";
        String result2 = solution.minWindow(s2, t2);
        System.out.println("Test 2 - Input: s=\"a\", t=\"a\"");
        System.out.println("Expected: \"a\", Got: \"" + result2 + "\"");
        
        // Test case 3
        String s3 = "a";
        String t3 = "aa";
        String result3 = solution.minWindow(s3, t3);
        System.out.println("Test 3 - Input: s=\"a\", t=\"aa\"");
        System.out.println("Expected: \"\", Got: \"" + result3 + "\"");
    }
}

