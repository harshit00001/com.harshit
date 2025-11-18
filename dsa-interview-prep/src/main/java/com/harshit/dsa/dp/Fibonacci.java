package com.harshit.dsa.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * Fibonacci - Introduction to Dynamic Programming
 * 
 * Problem: Find nth Fibonacci number
 * F(0) = 0, F(1) = 1, F(n) = F(n-1) + F(n-2)
 * 
 * Approaches:
 * 1. Recursive: O(2^n) time - Too slow!
 * 2. Memoization (Top-down): O(n) time, O(n) space
 * 3. Tabulation (Bottom-up): O(n) time, O(n) space
 * 4. Space Optimized: O(n) time, O(1) space
 */
public class Fibonacci {
    
    /**
     * Approach 1: Naive Recursive
     * Time: O(2^n), Space: O(n) - Very inefficient!
     */
    public static long fibonacciRecursive(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }
    
    /**
     * Approach 2: Memoization (Top-down DP)
     * Time: O(n), Space: O(n)
     */
    private static Map<Integer, Long> memo = new HashMap<>();
    
    public static long fibonacciMemoization(int n) {
        if (n <= 1) {
            return n;
        }
        
        if (memo.containsKey(n)) {
            return memo.get(n);
        }
        
        long result = fibonacciMemoization(n - 1) + fibonacciMemoization(n - 2);
        memo.put(n, result);
        return result;
    }
    
    /**
     * Approach 3: Tabulation (Bottom-up DP)
     * Time: O(n), Space: O(n)
     */
    public static long fibonacciTabulation(int n) {
        if (n <= 1) {
            return n;
        }
        
        long[] dp = new long[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }
    
    /**
     * Approach 4: Space Optimized
     * Time: O(n), Space: O(1)
     * Only need last two values, not entire array
     */
    public static long fibonacciOptimized(int n) {
        if (n <= 1) {
            return n;
        }
        
        long prev2 = 0; // F(0)
        long prev1 = 1; // F(1)
        
        for (int i = 2; i <= n; i++) {
            long current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
    
    public static void main(String[] args) {
        int n = 40;
        
        System.out.println("Calculating F(" + n + ")");
        
        // Note: Recursive is too slow for n=40, so we skip it
        System.out.println("Memoization: " + fibonacciMemoization(n));
        System.out.println("Tabulation: " + fibonacciTabulation(n));
        System.out.println("Optimized: " + fibonacciOptimized(n));
        
        // Show first 10 Fibonacci numbers
        System.out.println("\nFirst 10 Fibonacci numbers:");
        for (int i = 0; i < 10; i++) {
            System.out.print(fibonacciOptimized(i) + " ");
        }
        System.out.println();
    }
}

