package com.harshit.leetcode.slidingwindow;

/**
 * Problem: Minimum Size Subarray Sum
 * 
 * Given an array of positive integers nums and a positive integer target, return the minimal
 * length of a subarray whose sum is greater than or equal to target. If there is no such subarray,
 * return 0 instead.
 * 
 * Example 1:
 * Input: target = 7, nums = [2,3,1,2,4,3]
 * Output: 2
 * Explanation: The subarray [4,3] has the minimal length under the problem constraint.
 * 
 * Example 2:
 * Input: target = 4, nums = [1,4,4]
 * Output: 1
 * 
 * Example 3:
 * Input: target = 11, nums = [1,1,1,1,1,1,1,1]
 * Output: 0
 * 
 * Constraints:
 * - 1 <= target <= 10^9
 * - 1 <= nums.length <= 10^5
 * - 1 <= nums[i] <= 10^4
 * 
 * Follow up: If you have figured out the O(n) solution, try coding another solution of which
 * the time complexity is O(n log(n)).
 */
public class MinimumSizeSubarraySum {
    
    /**
     * Solution using sliding window (O(n) time, O(1) space)
     * 
     * @param target Target sum
     * @param nums Array of positive integers
     * @return Minimum length of subarray with sum >= target
     */
    public int minSubArrayLen(int target, int[] nums) {
        // Write your logic here
        // Hint: Use sliding window
        // Expand window until sum >= target
        // Then shrink from left to find minimum length
        return 0;
    }
    
    /**
     * Solution using binary search (O(n log n) time, O(n) space)
     * 
     * @param target Target sum
     * @param nums Array of positive integers
     * @return Minimum length of subarray with sum >= target
     */
    public int minSubArrayLenBinarySearch(int target, int[] nums) {
        // Write your logic here
        // Hint: Create prefix sum array
        // For each position, binary search for minimum length
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        MinimumSizeSubarraySum solution = new MinimumSizeSubarraySum();
        
        // Test case 1
        int target1 = 7;
        int[] nums1 = {2,3,1,2,4,3};
        int result1 = solution.minSubArrayLen(target1, nums1);
        System.out.println("Test 1 - target=7, nums=[2,3,1,2,4,3]");
        System.out.println("Expected: 2, Got: " + result1);
        
        // Test case 2
        int target2 = 4;
        int[] nums2 = {1,4,4};
        int result2 = solution.minSubArrayLen(target2, nums2);
        System.out.println("Test 2 - target=4, nums=[1,4,4]");
        System.out.println("Expected: 1, Got: " + result2);
    }
}

