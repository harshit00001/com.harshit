package com.harshit.dsa.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * ===================================================================
 * FIBONACCI - Introduction to Dynamic Programming
 * ===================================================================
 * 
 * PROBLEM STATEMENT:
 * Find the nth Fibonacci number.
 * 
 * Fibonacci Sequence: 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, ...
 * 
 * Definition:
 * F(0) = 0
 * F(1) = 1
 * F(n) = F(n-1) + F(n-2) for n > 1
 * 
 * ===================================================================
 * WHY FIBONACCI IS IMPORTANT:
 * ===================================================================
 * 
 * 1. It's the simplest example of Dynamic Programming
 * 2. Shows the difference between naive recursion and optimized DP
 * 3. Demonstrates memoization and tabulation techniques
 * 4. Many interview problems are variations of Fibonacci
 * 
 * ===================================================================
 * APPROACH 1: NAIVE RECURSION (Inefficient!)
 * ===================================================================
 * 
 * IDEA:
 * Directly implement the recurrence relation: F(n) = F(n-1) + F(n-2)
 * 
 * PROBLEM:
 * This leads to exponential time complexity because we recalculate
 * the same values many times!
 * 
 * Example: To calculate F(5):
 * 
 *                    F(5)
 *                   /    \
 *              F(4)        F(3)
 *             /    \      /    \
 *         F(3)    F(2)  F(2)  F(1)
 *        /   \   /  \  /  \
 *     F(2) F(1) F(1)F(0) F(1)F(0)
 *     /  \
 *  F(1) F(0)
 * 
 * Notice how F(3), F(2), F(1), F(0) are calculated MULTIPLE times!
 * 
 * TIME COMPLEXITY: O(2^n) - Exponential! Very slow!
 * SPACE COMPLEXITY: O(n) - Height of recursion tree
 * 
 * For n=40, this would take billions of operations!
 * 
 * ===================================================================
 * APPROACH 2: MEMOIZATION (Top-Down DP)
 * ===================================================================
 * 
 * IDEA:
 * Store results of subproblems in a map/array so we don't recalculate them.
 * 
 * HOW IT WORKS:
 * 1. Before calculating F(n), check if we've already calculated it
 * 2. If yes, return stored value
 * 3. If no, calculate it, store it, then return it
 * 
 * EXAMPLE: Calculating F(5) with memoization:
 * 
 * F(5) -> needs F(4) and F(3)
 *   F(4) -> needs F(3) and F(2)
 *     F(3) -> needs F(2) and F(1)
 *       F(2) -> needs F(1) and F(0)
 *         F(1) = 1 (base case, store it)
 *         F(0) = 0 (base case, store it)
 *       F(2) = 1 (calculate, store it)
 *     F(3) = 2 (calculate, store it)
 *     F(2) = 1 (already stored, return immediately!)
 *   F(4) = 3 (calculate, store it)
 *   F(3) = 2 (already stored, return immediately!)
 * F(5) = 5
 * 
 * Each value is calculated only ONCE!
 * 
 * TIME COMPLEXITY: O(n) - Each number calculated once
 * SPACE COMPLEXITY: O(n) - For memoization map + recursion stack
 * 
 * ===================================================================
 * APPROACH 3: TABULATION (Bottom-Up DP)
 * ===================================================================
 * 
 * IDEA:
 * Instead of recursion, build the solution from bottom up.
 * Start with base cases and build up to the answer.
 * 
 * HOW IT WORKS:
 * 1. Create array dp[] where dp[i] = F(i)
 * 2. Fill base cases: dp[0] = 0, dp[1] = 1
 * 3. For i from 2 to n: dp[i] = dp[i-1] + dp[i-2]
 * 4. Return dp[n]
 * 
 * EXAMPLE: Calculating F(5) with tabulation:
 * 
 * dp[0] = 0  (base case)
 * dp[1] = 1  (base case)
 * dp[2] = dp[1] + dp[0] = 1 + 0 = 1
 * dp[3] = dp[2] + dp[1] = 1 + 1 = 2
 * dp[4] = dp[3] + dp[2] = 2 + 1 = 3
 * dp[5] = dp[4] + dp[3] = 3 + 2 = 5
 * 
 * TIME COMPLEXITY: O(n)
 * SPACE COMPLEXITY: O(n) - For dp array
 * 
 * ===================================================================
 * APPROACH 4: SPACE OPTIMIZED (Best!)
 * ===================================================================
 * 
 * IDEA:
 * Notice we only need the last two values to calculate the next one!
 * We don't need the entire array.
 * 
 * Instead of: dp[0], dp[1], dp[2], ..., dp[n]
 * We only need: prev2 (F(i-2)), prev1 (F(i-1))
 * 
 * HOW IT WORKS:
 * 1. Start with prev2 = 0 (F(0)), prev1 = 1 (F(1))
 * 2. For i from 2 to n:
 *    - current = prev1 + prev2
 *    - prev2 = prev1
 *    - prev1 = current
 * 3. Return prev1
 * 
 * EXAMPLE: Calculating F(5) with space optimization:
 * 
 * i=2: prev2=0, prev1=1
 *      current = 1 + 0 = 1
 *      prev2 = 1, prev1 = 1
 * 
 * i=3: prev2=1, prev1=1
 *      current = 1 + 1 = 2
 *      prev2 = 1, prev1 = 2
 * 
 * i=4: prev2=1, prev1=2
 *      current = 2 + 1 = 3
 *      prev2 = 2, prev1 = 3
 * 
 * i=5: prev2=2, prev1=3
 *      current = 3 + 2 = 5
 *      prev2 = 3, prev1 = 5
 * 
 * Answer: 5
 * 
 * TIME COMPLEXITY: O(n)
 * SPACE COMPLEXITY: O(1) - Only two variables!
 * 
 * ===================================================================
 * COMPARISON:
 * ===================================================================
 * 
 * Approach          | Time    | Space   | When to Use
 * -----------------|---------|---------|------------------
 * Naive Recursion  | O(2^n)  | O(n)    | Never! (Too slow)
 * Memoization      | O(n)    | O(n)    | When you think recursively
 * Tabulation       | O(n)    | O(n)    | When you want to see all values
 * Space Optimized  | O(n)    | O(1)    | Best for interviews!
 * 
 * ===================================================================
 */
public class FibonacciDetailed {
    
    /**
     * APPROACH 1: NAIVE RECURSION
     * 
     * WARNING: This is extremely slow for large n!
     * Only use this to understand the problem, never in production!
     * 
     * @param n The position in Fibonacci sequence
     * @return The nth Fibonacci number
     */
    public static long fibonacciRecursive(int n) {
        // Base cases
        if (n <= 1) {
            return n;
        }
        
        // Recurrence relation
        // This causes exponential time complexity!
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }
    
    // Global map for memoization (could also be passed as parameter)
    private static Map<Integer, Long> memo = new HashMap<>();
    
    /**
     * APPROACH 2: MEMOIZATION (Top-Down DP)
     * 
     * This is much faster than naive recursion because we avoid
     * recalculating the same values.
     * 
     * @param n The position in Fibonacci sequence
     * @return The nth Fibonacci number
     */
    public static long fibonacciMemoization(int n) {
        // Base cases
        if (n <= 1) {
            return n;
        }
        
        // Check if we've already calculated this value
        if (memo.containsKey(n)) {
            return memo.get(n); // Return cached value
        }
        
        // Calculate and store the result
        long result = fibonacciMemoization(n - 1) + fibonacciMemoization(n - 2);
        memo.put(n, result);
        
        return result;
    }
    
    /**
     * APPROACH 3: TABULATION (Bottom-Up DP)
     * 
     * Build solution from bottom up using an array.
     * This is iterative, so no recursion stack overhead.
     * 
     * @param n The position in Fibonacci sequence
     * @return The nth Fibonacci number
     */
    public static long fibonacciTabulation(int n) {
        // Base cases
        if (n <= 1) {
            return n;
        }
        
        // Create array to store Fibonacci numbers
        long[] dp = new long[n + 1];
        
        // Base cases
        dp[0] = 0;
        dp[1] = 1;
        
        // Fill array from bottom up
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }
    
    /**
     * APPROACH 4: SPACE OPTIMIZED (BEST FOR INTERVIEWS!)
     * 
     * This is the solution you should use in interviews!
     * It's efficient in both time and space.
     * 
     * @param n The position in Fibonacci sequence
     * @return The nth Fibonacci number
     */
    public static long fibonacciOptimized(int n) {
        // Base cases
        if (n <= 1) {
            return n;
        }
        
        // We only need the last two values
        long prev2 = 0; // F(0)
        long prev1 = 1; // F(1)
        
        // Calculate from F(2) to F(n)
        for (int i = 2; i <= n; i++) {
            // Current Fibonacci number
            long current = prev1 + prev2;
            
            // Update for next iteration
            prev2 = prev1;  // F(i-2) becomes F(i-1)
            prev1 = current; // F(i-1) becomes F(i)
        }
        
        // prev1 now contains F(n)
        return prev1;
    }
    
    public static void main(String[] args) {
        int n = 40;
        
        System.out.println("=== Calculating F(" + n + ") ===\n");
        
        // Note: Naive recursion is too slow for n=40, so we skip it
        System.out.println("Memoization: " + fibonacciMemoization(n));
        
        // Reset memo for fair comparison
        memo.clear();
        
        System.out.println("Tabulation: " + fibonacciTabulation(n));
        System.out.println("Optimized: " + fibonacciOptimized(n));
        
        System.out.println("\n=== First 15 Fibonacci Numbers ===");
        for (int i = 0; i < 15; i++) {
            System.out.print("F(" + i + ")=" + fibonacciOptimized(i) + "  ");
            if ((i + 1) % 5 == 0) {
                System.out.println();
            }
        }
        System.out.println();
        
        // Demonstrate the difference in performance
        System.out.println("\n=== Performance Comparison (n=35) ===");
        long start, end;
        
        // Memoization
        memo.clear();
        start = System.currentTimeMillis();
        fibonacciMemoization(35);
        end = System.currentTimeMillis();
        System.out.println("Memoization: " + (end - start) + " ms");
        
        // Tabulation
        start = System.currentTimeMillis();
        fibonacciTabulation(35);
        end = System.currentTimeMillis();
        System.out.println("Tabulation: " + (end - start) + " ms");
        
        // Optimized
        start = System.currentTimeMillis();
        fibonacciOptimized(35);
        end = System.currentTimeMillis();
        System.out.println("Optimized: " + (end - start) + " ms");
    }
}

