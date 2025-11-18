package com.harshit.dsa.dp;

/**
 * Climbing Stairs - Classic DP Problem
 * 
 * Problem: You are climbing a staircase. It takes n steps to reach the top.
 * Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb?
 * 
 * Example:
 * n = 3
 * Ways: 1+1+1, 1+2, 2+1
 * Output: 3
 * 
 * Insight: This is actually Fibonacci!
 * - To reach step n, you can come from step (n-1) or step (n-2)
 * - Ways(n) = Ways(n-1) + Ways(n-2)
 * 
 * Time: O(n), Space: O(1) optimized
 */
public class ClimbingStairs {
    
    /**
     * Space Optimized Solution
     */
    public static int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }
        
        int prev2 = 1; // Ways to reach step 1
        int prev1 = 2; // Ways to reach step 2
        
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
    
    /**
     * Tabulation Approach
     */
    public static int climbStairsTabulation(int n) {
        if (n <= 2) {
            return n;
        }
        
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }
    
    public static void main(String[] args) {
        int n = 5;
        System.out.println("Ways to climb " + n + " stairs: " + climbStairs(n));
        
        System.out.println("\nWays for first 10 steps:");
        for (int i = 1; i <= 10; i++) {
            System.out.println("Step " + i + ": " + climbStairs(i) + " ways");
        }
    }
}

