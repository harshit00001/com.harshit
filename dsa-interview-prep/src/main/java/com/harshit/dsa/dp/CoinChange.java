package com.harshit.dsa.dp;

import java.util.Arrays;

/**
 * Coin Change - Classic DP Problem
 * 
 * Problem: Given coins of different denominations and a total amount,
 * find the minimum number of coins needed to make that amount.
 * 
 * Example:
 * Input: coins = [1,2,5], amount = 11
 * Output: 3 (5 + 5 + 1 = 11)
 * 
 * Approach: Bottom-up DP
 * dp[i] = minimum coins needed to make amount i
 * dp[i] = min(dp[i], dp[i - coin] + 1) for each coin
 * 
 * Time: O(amount * coins.length), Space: O(amount)
 */
public class CoinChange {
    
    /**
     * Find minimum coins to make amount
     */
    public static int coinChange(int[] coins, int amount) {
        // dp[i] = minimum coins to make amount i
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // Initialize with impossible value
        dp[0] = 0; // 0 coins needed for amount 0
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    /**
     * Count number of ways to make amount (different problem)
     * Example: coins = [1,2,5], amount = 5
     * Ways: 1+1+1+1+1, 1+1+1+2, 1+2+2, 5
     * Output: 4
     */
    public static int coinChangeWays(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        dp[0] = 1; // One way to make amount 0 (use no coins)
        
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
    
    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        int amount = 11;
        
        System.out.println("Coins: " + Arrays.toString(coins));
        System.out.println("Amount: " + amount);
        System.out.println("Minimum coins needed: " + coinChange(coins, amount));
        
        System.out.println("\nNumber of ways to make " + amount + ": " + coinChangeWays(coins, amount));
    }
}

