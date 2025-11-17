package com.harshit.leetcode.backtracking;

import java.util.*;

/**
 * Problem: Generate Parentheses
 * 
 * Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
 * 
 * Example 1:
 * Input: n = 3
 * Output: ["((()))","(()())","(())()","()(())","()()()"]
 * 
 * Example 2:
 * Input: n = 1
 * Output: ["()"]
 * 
 * Constraints:
 * - 1 <= n <= 8
 */
public class GenerateParentheses {
    
    /**
     * Solution using backtracking (O(4^n / sqrt(n)) time, O(n) space)
     * 
     * @param n Number of pairs of parentheses
     * @return List of all valid combinations
     */
    public List<String> generateParenthesis(int n) {
        // Write your logic here
        // Hint: Use backtracking
        // Add '(' if open < n
        // Add ')' if close < open
        // Base case: open == n && close == n
        return new ArrayList<>();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        GenerateParentheses solution = new GenerateParentheses();
        
        // Test case 1
        int n1 = 3;
        List<String> result1 = solution.generateParenthesis(n1);
        System.out.println("Test 1 - Input: n=3");
        System.out.println("Result: " + result1);
        System.out.println("Expected: [\"((()))\",\"(()())\",\"(())()\",\"()(())\",\"()()()\"]");
        
        // Test case 2
        int n2 = 1;
        List<String> result2 = solution.generateParenthesis(n2);
        System.out.println("Test 2 - Input: n=1");
        System.out.println("Result: " + result2);
        System.out.println("Expected: [\"()\"]");
    }
}

