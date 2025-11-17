package com.harshit.leetcode.bitmanipulation;

/**
 * Problem: Single Number
 * 
 * Given a non-empty array of integers nums, every element appears twice except for one.
 * Find that single one.
 * 
 * You must implement a solution with a linear runtime complexity and use only constant extra space.
 * 
 * Example 1:
 * Input: nums = [2,2,1]
 * Output: 1
 * 
 * Example 2:
 * Input: nums = [4,1,2,1,2]
 * Output: 4
 * 
 * Example 3:
 * Input: nums = [1]
 * Output: 1
 * 
 * Constraints:
 * - 1 <= nums.length <= 3 * 10^4
 * - -3 * 10^4 <= nums[i] <= 3 * 10^4
 * - Each element in the array appears twice except for one element which appears only once.
 */
public class SingleNumber {
    
    /**
     * Solution using XOR (O(n) time, O(1) space)
     * 
     * @param nums Array of integers
     * @return Single number that appears once
     */
    public int singleNumber(int[] nums) {
        // Write your logic here
        // Hint: Use XOR property: a ^ a = 0, a ^ 0 = a
        // XOR all numbers, duplicates cancel out, result is single number
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        SingleNumber solution = new SingleNumber();
        
        // Test case 1
        int[] nums1 = {2,2,1};
        int result1 = solution.singleNumber(nums1);
        System.out.println("Test 1 - Input: [2,2,1]");
        System.out.println("Expected: 1, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {4,1,2,1,2};
        int result2 = solution.singleNumber(nums2);
        System.out.println("Test 2 - Input: [4,1,2,1,2]");
        System.out.println("Expected: 4, Got: " + result2);
        
        // Test case 3
        int[] nums3 = {1};
        int result3 = solution.singleNumber(nums3);
        System.out.println("Test 3 - Input: [1]");
        System.out.println("Expected: 1, Got: " + result3);
    }
}

