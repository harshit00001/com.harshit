package com.harshit.leetcode.dynamicprogramming;

/**
 * Problem: House Robber
 * 
 * You are a professional robber planning to rob houses along a street. Each house has a certain
 * amount of money stashed, the only constraint stopping you from robbing each of them is that
 * adjacent houses have security systems connected and it will automatically contact the police
 * if two adjacent houses were broken into on the same night.
 * 
 * Given an integer array nums representing the amount of money of each house, return the maximum
 * amount of money you can rob tonight without alerting the police.
 * 
 * Example 1:
 * Input: nums = [1,2,3,1]
 * Output: 4
 * Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
 * Total amount you can rob = 1 + 3 = 4.
 * 
 * Example 2:
 * Input: nums = [2,7,9,3,1]
 * Output: 12
 * Explanation: Rob house 1 (money = 2), rob house 3 (money = 9) and rob house 5 (money = 1).
 * Total amount you can rob = 2 + 9 + 1 = 12.
 * 
 * Constraints:
 * - 1 <= nums.length <= 100
 * - 0 <= nums[i] <= 400
 */
public class HouseRobber {
    
    /**
     * Solution using Dynamic Programming (O(n) time, O(n) space)
     * 
     * @param nums Array of money in each house
     * @return Maximum amount that can be robbed
     */
    public int rob(int[] nums) {
        // Write your logic here
        // Hint: dp[i] = max money robbing up to house i
        // dp[i] = max(dp[i-1], dp[i-2] + nums[i])
        // Either skip current house or rob it (can't rob previous)
        return 0;
    }
    
    /**
     * Solution using space optimization (O(n) time, O(1) space)
     * 
     * @param nums Array of money in each house
     * @return Maximum amount that can be robbed
     */
    public int robOptimized(int[] nums) {
        // Write your logic here
        // Hint: Only need previous two values
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        HouseRobber solution = new HouseRobber();
        
        // Test case 1
        int[] nums1 = {1,2,3,1};
        int result1 = solution.rob(nums1);
        System.out.println("Test 1 - Input: [1,2,3,1]");
        System.out.println("Expected: 4, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {2,7,9,3,1};
        int result2 = solution.rob(nums2);
        System.out.println("Test 2 - Input: [2,7,9,3,1]");
        System.out.println("Expected: 12, Got: " + result2);
    }
}

