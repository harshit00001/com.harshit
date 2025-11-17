package com.harshit.leetcode.arrays;

/**
 * Problem: First Missing Positive
 * 
 * Given an unsorted integer array nums, return the smallest missing positive integer.
 * 
 * You must implement an algorithm that runs in O(n) time and uses O(1) extra space.
 * 
 * Example 1:
 * Input: nums = [1,2,0]
 * Output: 3
 * Explanation: The numbers in the range [1,2] are all in the array.
 * 
 * Example 2:
 * Input: nums = [3,4,-1,1]
 * Output: 2
 * 
 * Example 3:
 * Input: nums = [7,8,9,11,12]
 * Output: 1
 * 
 * Constraints:
 * - 1 <= nums.length <= 10^5
 * - -2^31 <= nums[i] <= 2^31 - 1
 */
public class FirstMissingPositive {
    
    /**
     * Solution using array as hash table (O(n) time, O(1) space)
     * 
     * @param nums Array of integers
     * @return First missing positive integer
     */
    public int firstMissingPositive(int[] nums) {
        // Write your logic here
        // Hint: Use array indices to mark presence of numbers
        // For number i, mark nums[i-1] as negative or use special value
        // First positive index + 1 is the answer
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        FirstMissingPositive solution = new FirstMissingPositive();
        
        // Test case 1
        int[] nums1 = {1,2,0};
        int result1 = solution.firstMissingPositive(nums1);
        System.out.println("Test 1 - Input: [1,2,0]");
        System.out.println("Expected: 3, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {3,4,-1,1};
        int result2 = solution.firstMissingPositive(nums2);
        System.out.println("Test 2 - Input: [3,4,-1,1]");
        System.out.println("Expected: 2, Got: " + result2);
        
        // Test case 3
        int[] nums3 = {7,8,9,11,12};
        int result3 = solution.firstMissingPositive(nums3);
        System.out.println("Test 3 - Input: [7,8,9,11,12]");
        System.out.println("Expected: 1, Got: " + result3);
    }
}

