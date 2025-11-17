package com.harshit.leetcode.dynamicprogramming;

import java.util.*;

/**
 * Problem: Longest Increasing Subsequence
 * 
 * Given an integer array nums, return the length of the longest strictly increasing subsequence.
 * 
 * Example 1:
 * Input: nums = [10,9,2,5,3,7,101,18]
 * Output: 4
 * Explanation: The longest increasing subsequence is [2,3,7,18], therefore the length is 4.
 * 
 * Example 2:
 * Input: nums = [0,1,0,3,2,3]
 * Output: 4
 * 
 * Example 3:
 * Input: nums = [7,7,7,7,7,7,7]
 * Output: 1
 * 
 * Constraints:
 * - 1 <= nums.length <= 2500
 * - -10^4 <= nums[i] <= 10^4
 * 
 * Follow up: Can you come up with an algorithm that runs in O(n log(n)) time complexity?
 */
public class LongestIncreasingSubsequence {
    
    /**
     * Solution using Dynamic Programming (O(n^2) time, O(n) space)
     * 
     * @param nums Array of integers
     * @return Length of longest increasing subsequence
     */
    public int lengthOfLIS(int[] nums) {
        // Write your logic here
        // Hint: dp[i] = length of LIS ending at index i
        // For each i, check all previous j where nums[j] < nums[i]
        // dp[i] = max(dp[j]) + 1 for all valid j
        return 0;
    }
    
    /**
     * Solution using Binary Search (O(n log n) time, O(n) space)
     * 
     * @param nums Array of integers
     * @return Length of longest increasing subsequence
     */
    public int lengthOfLISBinarySearch(int[] nums) {
        // Write your logic here
        // Hint: Maintain array of smallest tail values for each length
        // Use binary search to find position to insert/update
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        LongestIncreasingSubsequence solution = new LongestIncreasingSubsequence();
        
        // Test case 1
        int[] nums1 = {10,9,2,5,3,7,101,18};
        int result1 = solution.lengthOfLIS(nums1);
        System.out.println("Test 1 - Input: [10,9,2,5,3,7,101,18]");
        System.out.println("Expected: 4, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {0,1,0,3,2,3};
        int result2 = solution.lengthOfLIS(nums2);
        System.out.println("Test 2 - Input: [0,1,0,3,2,3]");
        System.out.println("Expected: 4, Got: " + result2);
        
        // Test case 3
        int[] nums3 = {7,7,7,7,7,7,7};
        int result3 = solution.lengthOfLIS(nums3);
        System.out.println("Test 3 - Input: [7,7,7,7,7,7,7]");
        System.out.println("Expected: 1, Got: " + result3);
    }
}

