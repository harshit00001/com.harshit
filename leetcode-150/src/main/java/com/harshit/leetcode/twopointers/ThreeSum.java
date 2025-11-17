package com.harshit.leetcode.twopointers;

import java.util.*;

/**
 * Problem: 3Sum
 * 
 * Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that
 * i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
 * 
 * Notice that the solution set must not contain duplicate triplets.
 * 
 * Example 1:
 * Input: nums = [-1,0,1,2,-1,-4]
 * Output: [[-1,-1,2],[-1,0,1]]
 * Explanation:
 * nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0.
 * nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0.
 * The distinct triplets are [-1,0,1] and [-1,-1,2].
 * Notice that the order of the output and the order of the triplets does not matter.
 * 
 * Example 2:
 * Input: nums = [0,1,1]
 * Output: []
 * Explanation: The only possible triplet does not sum up to 0.
 * 
 * Example 3:
 * Input: nums = [0,0,0]
 * Output: [[0,0,0]]
 * Explanation: The only possible triplet sums up to 0.
 * 
 * Constraints:
 * - 3 <= nums.length <= 3000
 * - -10^5 <= nums[i] <= 10^5
 */
public class ThreeSum {
    
    /**
     * Solution using two pointers (O(n^2) time, O(1) space excluding output)
     * 
     * @param nums Array of integers
     * @return List of unique triplets that sum to zero
     */
    public List<List<Integer>> threeSum(int[] nums) {
        // Write your logic here
        // Hint: Sort array first
        // For each number, use two pointers to find pairs that sum to -number
        // Skip duplicates to avoid duplicate triplets
        return new ArrayList<>();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ThreeSum solution = new ThreeSum();
        
        // Test case 1
        int[] nums1 = {-1,0,1,2,-1,-4};
        List<List<Integer>> result1 = solution.threeSum(nums1);
        System.out.println("Test 1 - Input: [-1,0,1,2,-1,-4]");
        System.out.println("Result: " + result1);
        
        // Test case 2
        int[] nums2 = {0,1,1};
        List<List<Integer>> result2 = solution.threeSum(nums2);
        System.out.println("Test 2 - Input: [0,1,1]");
        System.out.println("Result: " + result2);
        
        // Test case 3
        int[] nums3 = {0,0,0};
        List<List<Integer>> result3 = solution.threeSum(nums3);
        System.out.println("Test 3 - Input: [0,0,0]");
        System.out.println("Result: " + result3);
    }
}

