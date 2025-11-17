package com.harshit.leetcode.arrays;

import java.util.*;

/**
 * Problem: Word Break
 * 
 * Given a string s and a dictionary of strings wordDict, return true if s can be segmented
 * into a space-separated sequence of one or more dictionary words.
 * 
 * Note that the same word in the dictionary may be reused multiple times in the segmentation.
 * 
 * Example 1:
 * Input: s = "leetcode", wordDict = ["leet","code"]
 * Output: true
 * Explanation: Return true because "leetcode" can be segmented as "leet code".
 * 
 * Example 2:
 * Input: s = "applepenapple", wordDict = ["apple","pen"]
 * Output: true
 * Explanation: Return true because "applepenapple" can be segmented as "apple pen apple".
 * Note that you are allowed to reuse a dictionary word.
 * 
 * Example 3:
 * Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
 * Output: false
 * 
 * Constraints:
 * - 1 <= s.length <= 300
 * - 1 <= wordDict.length <= 1000
 * - 1 <= wordDict[i].length <= 20
 * - s and wordDict[i] consist of only lowercase English letters.
 * - All the strings of wordDict are unique.
 */
public class WordBreak {
    
    /**
     * Solution using Dynamic Programming (O(n^2) time, O(n) space)
     * 
     * @param s Input string
     * @param wordDict Dictionary of words
     * @return true if can be segmented, false otherwise
     */
    public boolean wordBreak(String s, List<String> wordDict) {
        // Write your logic here
        // Hint: dp[i] = true if s[0..i-1] can be segmented
        // For each position, check if substring ending here is in dict
        // and previous part can be segmented
        return false;
    }
    
    /**
     * Solution using BFS (O(n^2) time, O(n) space)
     * 
     * @param s Input string
     * @param wordDict Dictionary of words
     * @return true if can be segmented, false otherwise
     */
    public boolean wordBreakBFS(String s, List<String> wordDict) {
        // Write your logic here
        // Hint: Use queue to track positions
        // For each position, try all words from dict
        // If word matches, add next position to queue
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        WordBreak solution = new WordBreak();
        
        // Test case 1
        String s1 = "leetcode";
        List<String> dict1 = Arrays.asList("leet","code");
        boolean result1 = solution.wordBreak(s1, dict1);
        System.out.println("Test 1 - s=\"leetcode\", wordDict=[\"leet\",\"code\"]");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        String s2 = "applepenapple";
        List<String> dict2 = Arrays.asList("apple","pen");
        boolean result2 = solution.wordBreak(s2, dict2);
        System.out.println("Test 2 - s=\"applepenapple\", wordDict=[\"apple\",\"pen\"]");
        System.out.println("Expected: true, Got: " + result2);
    }
}

