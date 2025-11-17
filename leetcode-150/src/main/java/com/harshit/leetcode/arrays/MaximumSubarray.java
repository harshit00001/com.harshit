package com.harshit.leetcode.arrays;

/**
 * Problem: Maximum Subarray (Kadane's Algorithm)
 * 
 * Given an integer array nums, find the contiguous subarray (containing at least one number)
 * which has the largest sum and return its sum.
 * 
 * A subarray is a contiguous part of an array.
 * 
 * Example 1:
 * Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
 * Output: 6
 * Explanation: [4,-1,2,1] has the largest sum = 6.
 * 
 * Example 2:
 * Input: nums = [1]
 * Output: 1
 * 
 * Example 3:
 * Input: nums = [5,4,-1,7,8]
 * Output: 23
 * 
 * Constraints:
 * - 1 <= nums.length <= 10^5
 * - -10^4 <= nums[i] <= 10^4
 * 
 * Follow up: If you have figured out the O(n) solution, try coding another solution using
 * the divide and conquer approach, which is more subtle.
 */
public class MaximumSubarray {
    
    /**
     * Solution using Kadane's Algorithm (O(n) time, O(1) space)
     * 
     * @param nums Array of integers
     * @return Maximum sum of contiguous subarray
     */
    public int maxSubArray(int[] nums) {
        // Write your logic here
        // Hint: Track current sum and max sum
        // If current sum < 0, reset to 0 (start new subarray)
        // Update max sum at each step
        return 0;
    }
    
    /**
     * Solution using Divide and Conquer (O(n log n) time, O(log n) space)
     * 
     * @param nums Array of integers
     * @return Maximum sum of contiguous subarray
     */
    public int maxSubArrayDivideConquer(int[] nums) {
        // Write your logic here
        // Hint: Divide array in half
        // Max subarray is either in left half, right half, or crosses middle
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        MaximumSubarray solution = new MaximumSubarray();
        
        // Test case 1
        int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int result1 = solution.maxSubArray(nums1);
        System.out.println("Test 1 - Input: [-2,1,-3,4,-1,2,1,-5,4]");
        System.out.println("Expected: 6, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {1};
        int result2 = solution.maxSubArray(nums2);
        System.out.println("Test 2 - Input: [1]");
        System.out.println("Expected: 1, Got: " + result2);
        
        // Test case 3
        int[] nums3 = {5, 4, -1, 7, 8};
        int result3 = solution.maxSubArray(nums3);
        System.out.println("Test 3 - Input: [5,4,-1,7,8]");
        System.out.println("Expected: 23, Got: " + result3);
    }
}

