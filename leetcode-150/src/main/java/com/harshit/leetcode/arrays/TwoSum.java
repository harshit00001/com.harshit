package com.harshit.leetcode.arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Two Sum
 * 
 * Given an array of integers nums and an integer target, return indices of the two numbers
 * such that they add up to target.
 * 
 * You may assume that each input would have exactly one solution, and you may not use the
 * same element twice.
 * 
 * You can return the answer in any order.
 * 
 * Example 1:
 * Input: nums = [2,7,11,15], target = 9
 * Output: [0,1]
 * Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
 * 
 * Example 2:
 * Input: nums = [3,2,4], target = 6
 * Output: [1,2]
 * 
 * Example 3:
 * Input: nums = [3,3], target = 6
 * Output: [0,1]
 * 
 * Constraints:
 * - 2 <= nums.length <= 10^4
 * - -10^9 <= nums[i] <= 10^9
 * - -10^9 <= target <= 10^9
 * - Only one valid answer exists.
 */
public class TwoSum {
    
    /**
     * Solution using HashMap (O(n) time, O(n) space)
     * 
     * @param nums Array of integers
     * @param target Target sum
     * @return Array containing indices of two numbers that sum to target
     */
    public int[] twoSum(int[] nums, int target) {
        // Write your logic here
        // Hint: Use HashMap to store number -> index mapping
        // For each number, check if (target - number) exists in map
        return new int[0];
    }
    
    /**
     * Brute force solution (O(n^2) time, O(1) space)
     * 
     * @param nums Array of integers
     * @param target Target sum
     * @return Array containing indices of two numbers that sum to target
     */
    public int[] twoSumBruteForce(int[] nums, int target) {
        // Write your logic here
        // Hint: Use nested loops to check all pairs
        return new int[0];
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        TwoSum solution = new TwoSum();
        
        // Test case 1
        int[] nums1 = {2, 7, 11, 15};
        int target1 = 9;
        int[] result1 = solution.twoSum(nums1, target1);
        System.out.println("Test 1 - Input: [2,7,11,15], Target: 9");
        System.out.println("Expected: [0,1], Got: [" + result1[0] + "," + result1[1] + "]");
        
        // Test case 2
        int[] nums2 = {3, 2, 4};
        int target2 = 6;
        int[] result2 = solution.twoSum(nums2, target2);
        System.out.println("Test 2 - Input: [3,2,4], Target: 6");
        System.out.println("Expected: [1,2], Got: [" + result2[0] + "," + result2[1] + "]");
        
        // Test case 3
        int[] nums3 = {3, 3};
        int target3 = 6;
        int[] result3 = solution.twoSum(nums3, target3);
        System.out.println("Test 3 - Input: [3,3], Target: 6");
        System.out.println("Expected: [0,1], Got: [" + result3[0] + "," + result3[1] + "]");
    }
}

