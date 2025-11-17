package com.harshit.leetcode.stack;

import java.util.*;

/**
 * Problem: Valid Parentheses
 * 
 * Given a string s containing just the characters '(', ')', '{', '}', '[' and ']',
 * determine if the input string is valid.
 * 
 * An input string is valid if:
 * 1. Open brackets must be closed by the same type of brackets.
 * 2. Open brackets must be closed in the correct order.
 * 3. Every close bracket has a corresponding open bracket of the same type.
 * 
 * Example 1:
 * Input: s = "()"
 * Output: true
 * 
 * Example 2:
 * Input: s = "()[]{}"
 * Output: true
 * 
 * Example 3:
 * Input: s = "(]"
 * Output: false
 * 
 * Constraints:
 * - 1 <= s.length <= 10^4
 * - s consists of parentheses only '()[]{}'.
 */
public class ValidParentheses {
    
    /**
     * Solution using Stack (O(n) time, O(n) space)
     * 
     * @param s String containing parentheses
     * @return true if valid, false otherwise
     */
    public boolean isValid(String s) {
        // Write your logic here
        // Hint: Use stack to track opening brackets
        // When closing bracket found, check if it matches top of stack
        // Stack should be empty at the end
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ValidParentheses solution = new ValidParentheses();
        
        // Test case 1
        String s1 = "()";
        boolean result1 = solution.isValid(s1);
        System.out.println("Test 1 - Input: \"()\"");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        String s2 = "()[]{}";
        boolean result2 = solution.isValid(s2);
        System.out.println("Test 2 - Input: \"()[]{}\"");
        System.out.println("Expected: true, Got: " + result2);
        
        // Test case 3
        String s3 = "(]";
        boolean result3 = solution.isValid(s3);
        System.out.println("Test 3 - Input: \"(]\"");
        System.out.println("Expected: false, Got: " + result3);
    }
}

