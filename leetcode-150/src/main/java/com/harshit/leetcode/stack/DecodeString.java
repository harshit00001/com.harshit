package com.harshit.leetcode.stack;

import java.util.*;

/**
 * Problem: Decode String
 * 
 * Given an encoded string, return its decoded string.
 * 
 * The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets
 * is being repeated exactly k times. Note that k is guaranteed to be a positive integer.
 * 
 * You may assume that the input string is always valid; there are no extra white spaces, square
 * brackets are well-formed, etc. Furthermore, you may assume that the original data does not
 * contain any digits and that digits are only for those repeat numbers, k. For example, there
 * will not be input like 3a or 2[4].
 * 
 * Example 1:
 * Input: s = "3[a]2[bc]"
 * Output: "aaabcbc"
 * 
 * Example 2:
 * Input: s = "3[a2[c]]"
 * Output: "accaccacc"
 * 
 * Example 3:
 * Input: s = "2[abc]3[cd]ef"
 * Output: "abcabccdcdcdef"
 * 
 * Constraints:
 * - 1 <= s.length <= 30
 * - s consists of lowercase English letters, digits, and square brackets '[]'.
 * - s is a valid encoded string.
 */
public class DecodeString {
    
    /**
     * Solution using Stack (O(n) time, O(n) space)
     * 
     * @param s Encoded string
     * @return Decoded string
     */
    public String decodeString(String s) {
        // Write your logic here
        // Hint: Use stack to store strings and numbers
        // When '[' found, push current string and number
        // When ']' found, pop and repeat string
        return "";
    }
    
    /**
     * Solution using recursion (O(n) time, O(n) space)
     * 
     * @param s Encoded string
     * @return Decoded string
     */
    public String decodeStringRecursive(String s) {
        // Write your logic here
        // Hint: Recursively decode nested brackets
        return "";
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        DecodeString solution = new DecodeString();
        
        // Test case 1
        String s1 = "3[a]2[bc]";
        String result1 = solution.decodeString(s1);
        System.out.println("Test 1 - Input: \"3[a]2[bc]\"");
        System.out.println("Result: \"" + result1 + "\"");
        System.out.println("Expected: \"aaabcbc\"");
        
        // Test case 2
        String s2 = "3[a2[c]]";
        String result2 = solution.decodeString(s2);
        System.out.println("Test 2 - Input: \"3[a2[c]]\"");
        System.out.println("Result: \"" + result2 + "\"");
        System.out.println("Expected: \"accaccacc\"");
    }
}

