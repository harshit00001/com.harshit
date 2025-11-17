package com.harshit.leetcode.arrays;

/**
 * Problem: Best Time to Buy and Sell Stock
 * 
 * You are given an array prices where prices[i] is the price of a given stock on the ith day.
 * 
 * You want to maximize your profit by choosing a single day to buy one stock and choosing
 * a different day in the future to sell that stock.
 * 
 * Return the maximum profit you can achieve from this transaction. If you cannot achieve
 * any profit, return 0.
 * 
 * Example 1:
 * Input: prices = [7,1,5,3,6,4]
 * Output: 5
 * Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
 * Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
 * 
 * Example 2:
 * Input: prices = [7,6,4,3,1]
 * Output: 0
 * Explanation: In this case, no transactions are done and the max profit = 0.
 * 
 * Constraints:
 * - 1 <= prices.length <= 10^5
 * - 0 <= prices[i] <= 10^4
 */
public class BestTimeToBuyAndSellStock {
    
    /**
     * Solution using one pass (O(n) time, O(1) space)
     * 
     * @param prices Array of stock prices
     * @return Maximum profit
     */
    public int maxProfit(int[] prices) {
        // Write your logic here
        // Hint: Track minimum price seen so far
        // For each day, calculate profit if selling today and update max profit
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        BestTimeToBuyAndSellStock solution = new BestTimeToBuyAndSellStock();
        
        // Test case 1
        int[] prices1 = {7, 1, 5, 3, 6, 4};
        int result1 = solution.maxProfit(prices1);
        System.out.println("Test 1 - Input: [7,1,5,3,6,4]");
        System.out.println("Expected: 5, Got: " + result1);
        
        // Test case 2
        int[] prices2 = {7, 6, 4, 3, 1};
        int result2 = solution.maxProfit(prices2);
        System.out.println("Test 2 - Input: [7,6,4,3,1]");
        System.out.println("Expected: 0, Got: " + result2);
        
        // Test case 3
        int[] prices3 = {1, 2};
        int result3 = solution.maxProfit(prices3);
        System.out.println("Test 3 - Input: [1,2]");
        System.out.println("Expected: 1, Got: " + result3);
    }
}

