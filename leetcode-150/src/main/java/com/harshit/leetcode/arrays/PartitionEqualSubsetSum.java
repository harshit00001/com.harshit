package com.harshit.leetcode.arrays;

/**
 * Problem: Partition Equal Subset Sum
 * 
 * Given a non-empty array nums containing only positive integers, find if the array can be
 * partitioned into two subsets such that the sum of elements in both subsets is equal.
 * 
 * Example 1:
 * Input: nums = [1,5,11,5]
 * Output: true
 * Explanation: The array can be partitioned as [1, 5, 5] and [11].
 * 
 * Example 2:
 * Input: nums = [1,2,3,5]
 * Output: false
 * Explanation: The array cannot be partitioned into equal sum subsets.
 * 
 * Constraints:
 * - 1 <= nums.length <= 200
 * - 1 <= nums[i] <= 100
 */
public class PartitionEqualSubsetSum {
    
    /**
     * Solution using Dynamic Programming (O(n*sum) time, O(sum) space)
     * 
     * @param nums Array of positive integers
     * @return true if can be partitioned, false otherwise
     */
    public boolean canPartition(int[] nums) {
        // Write your logic here
        // Hint: If total sum is odd, return false
        // Find if subset with sum = total/2 exists (0/1 knapsack)
        // dp[i] = true if sum i can be achieved
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        PartitionEqualSubsetSum solution = new PartitionEqualSubsetSum();
        
        // Test case 1
        int[] nums1 = {1,5,11,5};
        boolean result1 = solution.canPartition(nums1);
        System.out.println("Test 1 - Input: [1,5,11,5]");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {1,2,3,5};
        boolean result2 = solution.canPartition(nums2);
        System.out.println("Test 2 - Input: [1,2,3,5]");
        System.out.println("Expected: false, Got: " + result2);
    }
}

