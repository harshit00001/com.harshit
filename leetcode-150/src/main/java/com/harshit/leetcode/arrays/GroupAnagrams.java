package com.harshit.leetcode.arrays;

import java.util.*;

/**
 * Problem: Group Anagrams
 * 
 * Given an array of strings strs, group the anagrams together. You can return the answer in any order.
 * 
 * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase,
 * typically using all the original letters exactly once.
 * 
 * Example 1:
 * Input: strs = ["eat","tea","tan","ate","nat","bat"]
 * Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
 * 
 * Example 2:
 * Input: strs = [""]
 * Output: [[""]]
 * 
 * Example 3:
 * Input: strs = ["a"]
 * Output: [["a"]]
 * 
 * Constraints:
 * - 1 <= strs.length <= 10^4
 * - 0 <= strs[i].length <= 100
 * - strs[i] consists of lowercase English letters.
 */
public class GroupAnagrams {
    
    /**
     * Solution using HashMap with sorted string as key (O(n*k*log(k)) time, O(n*k) space)
     * 
     * @param strs Array of strings
     * @return List of grouped anagrams
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        // Write your logic here
        // Hint: Use HashMap where key is sorted string, value is list of anagrams
        // Sort each string and use as key
        return new ArrayList<>();
    }
    
    /**
     * Solution using HashMap with character count as key (O(n*k) time, O(n*k) space)
     * 
     * @param strs Array of strings
     * @return List of grouped anagrams
     */
    public List<List<String>> groupAnagramsCount(String[] strs) {
        // Write your logic here
        // Hint: Use character frequency array as key instead of sorting
        // Convert frequency array to string for HashMap key
        return new ArrayList<>();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        GroupAnagrams solution = new GroupAnagrams();
        
        // Test case 1
        String[] strs1 = {"eat","tea","tan","ate","nat","bat"};
        List<List<String>> result1 = solution.groupAnagrams(strs1);
        System.out.println("Test 1 - Input: [\"eat\",\"tea\",\"tan\",\"ate\",\"nat\",\"bat\"]");
        System.out.println("Result: " + result1);
        
        // Test case 2
        String[] strs2 = {""};
        List<List<String>> result2 = solution.groupAnagrams(strs2);
        System.out.println("Test 2 - Input: [\"\"]");
        System.out.println("Result: " + result2);
        
        // Test case 3
        String[] strs3 = {"a"};
        List<List<String>> result3 = solution.groupAnagrams(strs3);
        System.out.println("Test 3 - Input: [\"a\"]");
        System.out.println("Result: " + result3);
    }
}

