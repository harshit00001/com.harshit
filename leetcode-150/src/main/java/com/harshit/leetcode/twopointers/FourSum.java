package com.harshit.leetcode.twopointers;

import java.util.*;

/**
 * Problem: 4Sum
 * 
 * Given an array nums of n integers, return an array of all the unique quadruplets
 * [nums[a], nums[b], nums[c], nums[d]] such that:
 * - 0 <= a, b, c, d < n
 * - a, b, c, and d are distinct.
 * - nums[a] + nums[b] + nums[c] + nums[d] == target
 * 
 * You may return the answer in any order.
 * 
 * Example 1:
 * Input: nums = [1,0,-1,0,-2,2], target = 0
 * Output: [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
 * 
 * Example 2:
 * Input: nums = [2,2,2,2,2], target = 8
 * Output: [[2,2,2,2]]
 * 
 * Constraints:
 * - 1 <= nums.length <= 200
 * - -10^9 <= nums[i] <= 10^9
 * - -10^9 <= target <= 10^9
 */
public class FourSum {
    
    /**
     * Solution using two pointers (O(n^3) time, O(1) space excluding output)
     * 
     * @param nums Array of integers
     * @param target Target sum
     * @return List of unique quadruplets that sum to target
     */
    public List<List<Integer>> fourSum(int[] nums, int target) {
        // Write your logic here
        // Hint: Sort array first
        // Use two nested loops for first two numbers
        // Use two pointers for last two numbers
        // Skip duplicates to avoid duplicate quadruplets
        return new ArrayList<>();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        FourSum solution = new FourSum();
        
        // Test case 1
        int[] nums1 = {1,0,-1,0,-2,2};
        int target1 = 0;
        List<List<Integer>> result1 = solution.fourSum(nums1, target1);
        System.out.println("Test 1 - Input: [1,0,-1,0,-2,2], target=0");
        System.out.println("Result: " + result1);
        System.out.println("Expected: [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]");
        
        // Test case 2
        int[] nums2 = {2,2,2,2,2};
        int target2 = 8;
        List<List<Integer>> result2 = solution.fourSum(nums2, target2);
        System.out.println("Test 2 - Input: [2,2,2,2,2], target=8");
        System.out.println("Result: " + result2);
        System.out.println("Expected: [[2,2,2,2]]");
    }
}

