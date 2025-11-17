package com.harshit.leetcode.twopointers;

/**
 * Problem: Remove Duplicates from Sorted Array
 * 
 * Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place
 * such that each unique element appears only once. The relative order of the elements should
 * be kept the same. Then return the number of unique elements in nums.
 * 
 * Consider the number of unique elements of nums to be k, to get accepted, you need to do
 * the following things:
 * - Change the array nums such that the first k elements of nums contain the unique elements
 *   in the order they were present in nums initially. The remaining elements of nums are not
 *   important as well as the size of nums.
 * - Return k.
 * 
 * Example 1:
 * Input: nums = [1,1,2]
 * Output: 2, nums = [1,2,_]
 * Explanation: Your function should return k = 2, with the first two elements of nums being 1 and 2 respectively.
 * 
 * Example 2:
 * Input: nums = [0,0,1,1,1,2,2,3,3,4]
 * Output: 5, nums = [0,1,2,3,4,_,_,_,_,_]
 * 
 * Constraints:
 * - 1 <= nums.length <= 3 * 10^4
 * - -100 <= nums[i] <= 100
 * - nums is sorted in non-decreasing order.
 */
public class RemoveDuplicates {
    
    /**
     * Solution using two pointers (O(n) time, O(1) space)
     * 
     * @param nums Sorted array with duplicates
     * @return Number of unique elements
     */
    public int removeDuplicates(int[] nums) {
        // Write your logic here
        // Hint: Use two pointers: one for current position, one for next unique element
        // Copy unique elements to front of array
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        RemoveDuplicates solution = new RemoveDuplicates();
        
        // Test case 1
        int[] nums1 = {1,1,2};
        int result1 = solution.removeDuplicates(nums1);
        System.out.println("Test 1 - Input: [1,1,2]");
        System.out.println("Unique count: " + result1 + " (Expected: 2)");
        System.out.print("Array: [");
        for (int i = 0; i < result1; i++) {
            System.out.print(nums1[i]);
            if (i < result1 - 1) System.out.print(",");
        }
        System.out.println("]");
        
        // Test case 2
        int[] nums2 = {0,0,1,1,1,2,2,3,3,4};
        int result2 = solution.removeDuplicates(nums2);
        System.out.println("Test 2 - Input: [0,0,1,1,1,2,2,3,3,4]");
        System.out.println("Unique count: " + result2 + " (Expected: 5)");
    }
}

