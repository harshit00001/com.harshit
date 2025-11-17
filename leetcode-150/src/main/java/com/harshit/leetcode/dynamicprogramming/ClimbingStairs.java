package com.harshit.leetcode.dynamicprogramming;

/**
 * Problem: Climbing Stairs
 * 
 * You are climbing a staircase. It takes n steps to reach the top.
 * 
 * Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
 * 
 * Example 1:
 * Input: n = 2
 * Output: 2
 * Explanation: There are two ways to climb to the top.
 * 1. 1 step + 1 step
 * 2. 2 steps
 * 
 * Example 2:
 * Input: n = 3
 * Output: 3
 * Explanation: There are three ways to climb to the top.
 * 1. 1 step + 1 step + 1 step
 * 2. 1 step + 2 steps
 * 3. 2 steps + 1 step
 * 
 * Constraints:
 * - 1 <= n <= 45
 */
public class ClimbingStairs {
    
    /**
     * Solution using Dynamic Programming (O(n) time, O(n) space)
     * 
     * @param n Number of steps
     * @return Number of distinct ways to climb
     */
    public int climbStairs(int n) {
        // Write your logic here
        // Hint: dp[i] = ways to reach step i
        // dp[i] = dp[i-1] + dp[i-2]
        // Base cases: dp[0] = 1, dp[1] = 1
        return 0;
    }
    
    /**
     * Solution using space optimization (O(n) time, O(1) space)
     * 
     * @param n Number of steps
     * @return Number of distinct ways to climb
     */
    public int climbStairsOptimized(int n) {
        // Write your logic here
        // Hint: Only need previous two values, not entire array
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ClimbingStairs solution = new ClimbingStairs();
        
        // Test case 1
        int n1 = 2;
        int result1 = solution.climbStairs(n1);
        System.out.println("Test 1 - Input: n=2");
        System.out.println("Expected: 2, Got: " + result1);
        
        // Test case 2
        int n2 = 3;
        int result2 = solution.climbStairs(n2);
        System.out.println("Test 2 - Input: n=3");
        System.out.println("Expected: 3, Got: " + result2);
        
        // Test case 3
        int n3 = 5;
        int result3 = solution.climbStairs(n3);
        System.out.println("Test 3 - Input: n=5");
        System.out.println("Expected: 8, Got: " + result3);
    }
}

