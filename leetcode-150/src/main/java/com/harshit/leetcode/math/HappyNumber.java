package com.harshit.leetcode.math;

import java.util.*;

/**
 * Problem: Happy Number
 * 
 * Write an algorithm to determine if a number n is happy.
 * 
 * A happy number is a number defined by the following process:
 * - Starting with any positive integer, replace the number by the sum of the squares of its digits.
 * - Repeat the process until the number equals 1 (where it will stay), or it loops endlessly
 *   in a cycle which does not include 1.
 * - Those numbers for which this process ends in 1 are happy.
 * 
 * Return true if n is a happy number, and false if not.
 * 
 * Example 1:
 * Input: n = 19
 * Output: true
 * Explanation:
 * 1^2 + 9^2 = 82
 * 8^2 + 2^2 = 68
 * 6^2 + 8^2 = 100
 * 1^2 + 0^2 + 0^2 = 1
 * 
 * Example 2:
 * Input: n = 2
 * Output: false
 * 
 * Constraints:
 * - 1 <= n <= 2^31 - 1
 */
public class HappyNumber {
    
    /**
     * Solution using HashSet (O(log n) time, O(log n) space)
     * 
     * @param n Number to check
     * @return true if happy number, false otherwise
     */
    public boolean isHappy(int n) {
        // Write your logic here
        // Hint: Use HashSet to detect cycles
        // Calculate sum of squares of digits
        // If sum == 1, return true
        // If sum seen before, return false (cycle detected)
        return false;
    }
    
    /**
     * Solution using Floyd's cycle detection (O(log n) time, O(1) space)
     * 
     * @param n Number to check
     * @return true if happy number, false otherwise
     */
    public boolean isHappyFloyd(int n) {
        // Write your logic here
        // Hint: Use slow and fast pointers
        // Slow moves one step, fast moves two steps
        // If they meet and value != 1, cycle detected
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        HappyNumber solution = new HappyNumber();
        
        // Test case 1
        int n1 = 19;
        boolean result1 = solution.isHappy(n1);
        System.out.println("Test 1 - Input: n=19");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        int n2 = 2;
        boolean result2 = solution.isHappy(n2);
        System.out.println("Test 2 - Input: n=2");
        System.out.println("Expected: false, Got: " + result2);
    }
}

