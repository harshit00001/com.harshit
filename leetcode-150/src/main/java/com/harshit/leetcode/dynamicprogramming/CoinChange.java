package com.harshit.leetcode.dynamicprogramming;

import java.util.Arrays;

/**
 * Problem: Coin Change
 * 
 * You are given an integer array coins representing coins of different denominations and an
 * integer amount representing a total amount of money.
 * 
 * Return the fewest number of coins that you need to make up that amount. If that amount of
 * money cannot be made up by any combination of the coins, return -1.
 * 
 * You may assume that you have an infinite number of each kind of coin.
 * 
 * Example 1:
 * Input: coins = [1,2,5], amount = 11
 * Output: 3
 * Explanation: 11 = 5 + 5 + 1
 * 
 * Example 2:
 * Input: coins = [2], amount = 3
 * Output: -1
 * 
 * Example 3:
 * Input: coins = [1], amount = 0
 * Output: 0
 * 
 * Constraints:
 * - 1 <= coins.length <= 12
 * - 1 <= coins[i] <= 2^31 - 1
 * - 0 <= amount <= 10^4
 */
public class CoinChange {
    
    /**
     * Solution using Dynamic Programming (O(amount * coins.length) time, O(amount) space)
     * 
     * @param coins Array of coin denominations
     * @param amount Target amount
     * @return Minimum number of coins needed, or -1 if impossible
     */
    public int coinChange(int[] coins, int amount) {
        // Write your logic here
        // Hint: dp[i] = min coins to make amount i
        // For each coin, dp[i] = min(dp[i], dp[i-coin] + 1)
        // Initialize dp[0] = 0, others = amount + 1
        return -1;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        CoinChange solution = new CoinChange();
        
        // Test case 1
        int[] coins1 = {1,2,5};
        int amount1 = 11;
        int result1 = solution.coinChange(coins1, amount1);
        System.out.println("Test 1 - Input: coins=[1,2,5], amount=11");
        System.out.println("Expected: 3, Got: " + result1);
        
        // Test case 2
        int[] coins2 = {2};
        int amount2 = 3;
        int result2 = solution.coinChange(coins2, amount2);
        System.out.println("Test 2 - Input: coins=[2], amount=3");
        System.out.println("Expected: -1, Got: " + result2);
        
        // Test case 3
        int[] coins3 = {1};
        int amount3 = 0;
        int result3 = solution.coinChange(coins3, amount3);
        System.out.println("Test 3 - Input: coins=[1], amount=0");
        System.out.println("Expected: 0, Got: " + result3);
    }
}

