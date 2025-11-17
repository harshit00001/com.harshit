package com.harshit.leetcode.twopointers;

/**
 * Problem: Is Subsequence
 * 
 * Given two strings s and t, return true if s is a subsequence of t, or false otherwise.
 * 
 * A subsequence of a string is a new string that is formed from the original string by deleting
 * some (can be none) of the characters without disturbing the relative positions of the remaining
 * characters. (i.e., "ace" is a subsequence of "abcde" while "aec" is not).
 * 
 * Example 1:
 * Input: s = "abc", t = "ahbgdc"
 * Output: true
 * 
 * Example 2:
 * Input: s = "axc", t = "ahbgdc"
 * Output: false
 * 
 * Constraints:
 * - 0 <= s.length <= 100
 * - 0 <= t.length <= 10^4
 * - s and t consist only of lowercase English letters.
 * 
 * Follow up: Suppose there are lots of incoming s, say s1, s2, ..., sk where k >= 10^9, and
 * you want to check one by one to see if t has its subsequence. In this scenario, how would
 * you change your code?
 */
public class IsSubsequence {
    
    /**
     * Solution using two pointers (O(n) time, O(1) space)
     * 
     * @param s Subsequence string
     * @param t Target string
     * @return true if s is subsequence of t
     */
    public boolean isSubsequence(String s, String t) {
        // Write your logic here
        // Hint: Use two pointers for s and t
        // If characters match, move both pointers
        // Otherwise, only move t pointer
        // If s pointer reaches end, return true
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        IsSubsequence solution = new IsSubsequence();
        
        // Test case 1
        String s1 = "abc";
        String t1 = "ahbgdc";
        boolean result1 = solution.isSubsequence(s1, t1);
        System.out.println("Test 1 - s=\"abc\", t=\"ahbgdc\"");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        String s2 = "axc";
        String t2 = "ahbgdc";
        boolean result2 = solution.isSubsequence(s2, t2);
        System.out.println("Test 2 - s=\"axc\", t=\"ahbgdc\"");
        System.out.println("Expected: false, Got: " + result2);
    }
}

