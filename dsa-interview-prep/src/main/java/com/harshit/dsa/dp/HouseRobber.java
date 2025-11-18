package com.harshit.dsa.dp;

/**
 * ===================================================================
 * HOUSE ROBBER - Classic Dynamic Programming Problem
 * ===================================================================
 * 
 * PROBLEM STATEMENT:
 * You are a professional robber planning to rob houses along a street.
 * Each house has a certain amount of money stashed. The only constraint
 * stopping you from robbing all of them is that adjacent houses have
 * security systems connected, and it will automatically contact the police
 * if two adjacent houses were broken into on the same night.
 * 
 * Given an integer array nums representing the amount of money of each house,
 * return the maximum amount of money you can rob tonight without alerting the police.
 * 
 * ===================================================================
 * UNDERSTANDING THE PROBLEM:
 * ===================================================================
 * 
 * Example 1:
 * Input: nums = [2, 7, 9, 3, 1]
 * Output: 12
 * 
 * Explanation:
 * Let's visualize the houses and their money:
 * 
 * House:  0   1   2   3   4
 * Money:  2   7   9   3   1
 * 
 * We cannot rob two adjacent houses. Let's explore all possibilities:
 * 
 * Option 1: Rob house 0 (2) + house 2 (9) + house 4 (1) = 12 ✓
 * Option 2: Rob house 0 (2) + house 3 (3) = 5
 * Option 3: Rob house 1 (7) + house 3 (3) = 10
 * Option 4: Rob house 1 (7) + house 4 (1) = 8
 * 
 * Maximum: 12
 * 
 * Example 2:
 * Input: nums = [1, 2, 3, 1]
 * Output: 4
 * Explanation: Rob house 1 (2) + house 3 (1) = 3, OR
 *               Rob house 0 (1) + house 2 (3) = 4
 * Maximum: 4
 * 
 * ===================================================================
 * INTUITION & APPROACH:
 * ===================================================================
 * 
 * This is a classic Dynamic Programming problem. The key insight is:
 * 
 * At each house i, we have TWO choices:
 * 1. ROB the current house (i):
 *    - We can only do this if we didn't rob house (i-1)
 *    - Money = maximum money from houses 0 to (i-2) + money in house i
 * 
 * 2. DON'T ROB the current house (i):
 *    - We can take the maximum money from houses 0 to (i-1)
 * 
 * RECURRENCE RELATION:
 * dp[i] = maximum money we can rob from houses 0 to i
 * 
 * dp[i] = max(
 *     dp[i-1],              // Don't rob house i (take best from 0 to i-1)
 *     dp[i-2] + nums[i]     // Rob house i (take best from 0 to i-2 + current)
 * )
 * 
 * BASE CASES:
 * - dp[0] = nums[0]  (Only one house, rob it)
 * - dp[1] = max(nums[0], nums[1])  (Two houses, rob the one with more money)
 * 
 * ===================================================================
 * STEP-BY-STEP EXAMPLE:
 * ===================================================================
 * 
 * Let's trace through nums = [2, 7, 9, 3, 1]
 * 
 * Step 0: dp[0] = 2
 *         (Only house 0, we rob it: 2)
 * 
 * Step 1: dp[1] = max(2, 7) = 7
 *         (Two houses: rob house 0 (2) or house 1 (7)? Choose 7)
 * 
 * Step 2: dp[2] = max(dp[1], dp[0] + nums[2])
 *                = max(7, 2 + 9)
 *                = max(7, 11) = 11
 *         (Don't rob house 2: 7, OR rob house 2: 2+9=11. Choose 11)
 * 
 * Step 3: dp[3] = max(dp[2], dp[1] + nums[3])
 *                = max(11, 7 + 3)
 *                = max(11, 10) = 11
 *         (Don't rob house 3: 11, OR rob house 3: 7+3=10. Choose 11)
 * 
 * Step 4: dp[4] = max(dp[3], dp[2] + nums[4])
 *                = max(11, 11 + 1)
 *                = max(11, 12) = 12
 *         (Don't rob house 4: 11, OR rob house 4: 11+1=12. Choose 12)
 * 
 * Final Answer: 12
 * 
 * ===================================================================
 * OPTIMIZATION:
 * ===================================================================
 * 
 * Notice that we only need dp[i-1] and dp[i-2] to calculate dp[i].
 * We don't need the entire dp array! We can use just two variables:
 * - prev1 = dp[i-1]
 * - prev2 = dp[i-2]
 * 
 * This reduces space from O(n) to O(1)!
 * 
 * ===================================================================
 * TIME & SPACE COMPLEXITY:
 * ===================================================================
 * 
 * Time Complexity: O(n)
 *   - We iterate through the array once
 *   - Each iteration does constant work
 * 
 * Space Complexity: O(1) for optimized version
 *   - We only use two variables (prev1, prev2)
 *   - O(n) if we use the full dp array
 * 
 * ===================================================================
 */
public class HouseRobber {
    
    /**
     * SPACE OPTIMIZED SOLUTION
     * 
     * Instead of storing all dp[i] values, we only keep track of:
     * - prev2: maximum money from houses 0 to (i-2)
     * - prev1: maximum money from houses 0 to (i-1)
     * 
     * @param nums Array representing money in each house
     * @return Maximum money that can be robbed
     */
    public static int rob(int[] nums) {
        // Edge case: No houses
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // Base case: Only one house - rob it
        if (nums.length == 1) {
            return nums[0];
        }
        
        // Base case: Two houses - rob the one with more money
        // prev2 represents dp[0] = money from first house
        int prev2 = nums[0];
        
        // prev1 represents dp[1] = max(money from first house, money from second house)
        int prev1 = Math.max(nums[0], nums[1]);
        
        // Process remaining houses from index 2 onwards
        for (int i = 2; i < nums.length; i++) {
            // At house i, we have two choices:
            // 1. Don't rob house i -> take prev1 (best from houses 0 to i-1)
            // 2. Rob house i -> take prev2 (best from houses 0 to i-2) + nums[i]
            int current = Math.max(prev1, prev2 + nums[i]);
            
            // Update for next iteration:
            // - prev2 becomes the old prev1 (best from 0 to i-1)
            // - prev1 becomes current (best from 0 to i)
            prev2 = prev1;
            prev1 = current;
        }
        
        // prev1 now contains the maximum money from all houses
        return prev1;
    }
    
    /**
     * TABULATION APPROACH (Bottom-Up DP with Full Array)
     * 
     * This version uses a full dp array for better understanding.
     * It's easier to visualize but uses O(n) space instead of O(1).
     * 
     * How it works:
     * 1. Create dp array where dp[i] = max money from houses 0 to i
     * 2. Fill base cases: dp[0] and dp[1]
     * 3. For each house i (from 2 to n-1):
     *    - dp[i] = max(dp[i-1], dp[i-2] + nums[i])
     * 4. Return dp[n-1]
     * 
     * @param nums Array representing money in each house
     * @return Maximum money that can be robbed
     */
    public static int robTabulation(int[] nums) {
        // Edge cases
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        
        // dp[i] = maximum money we can rob from houses 0 to i
        int[] dp = new int[nums.length];
        
        // Base case 1: Only one house available
        dp[0] = nums[0];
        
        // Base case 2: Two houses available - choose the better one
        dp[1] = Math.max(nums[0], nums[1]);
        
        // Fill dp array for remaining houses
        for (int i = 2; i < nums.length; i++) {
            // Recurrence relation:
            // Option 1: Don't rob house i -> take dp[i-1]
            // Option 2: Rob house i -> take dp[i-2] + nums[i]
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        
        // dp[n-1] contains the answer for all houses
        return dp[nums.length - 1];
    }
    
    public static void main(String[] args) {
        int[] houses = {2, 7, 9, 3, 1};
        System.out.println("Houses: " + java.util.Arrays.toString(houses));
        System.out.println("Maximum money: " + rob(houses));
        
        int[] houses2 = {1, 2, 3, 1};
        System.out.println("\nHouses: " + java.util.Arrays.toString(houses2));
        System.out.println("Maximum money: " + rob(houses2));
    }
}

