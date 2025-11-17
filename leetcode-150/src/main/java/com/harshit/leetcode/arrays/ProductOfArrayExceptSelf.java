package com.harshit.leetcode.arrays;

/**
 * Problem: Product of Array Except Self
 * 
 * Given an integer array nums, return an array answer such that answer[i] is equal to the
 * product of all the elements of nums except nums[i].
 * 
 * The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.
 * 
 * You must write an algorithm that runs in O(n) time and without using the division operator.
 * 
 * Example 1:
 * Input: nums = [1,2,3,4]
 * Output: [24,12,8,6]
 * 
 * Example 2:
 * Input: nums = [-1,1,0,-3,3]
 * Output: [0,0,9,0,0]
 * 
 * Constraints:
 * - 2 <= nums.length <= 10^5
 * - -30 <= nums[i] <= 30
 * - The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.
 * 
 * Follow up: Can you solve the problem in O(1) extra space complexity?
 * (The output array does not count as extra space for space complexity analysis.)
 */
public class ProductOfArrayExceptSelf {
    
    /**
     * Solution using left and right product arrays (O(n) time, O(n) space)
     * 
     * @param nums Array of integers
     * @return Array where each element is product of all other elements
     */
    public int[] productExceptSelf(int[] nums) {
        // Write your logic here
        // Hint: Create left[] and right[] arrays
        // left[i] = product of all elements to the left of i
        // right[i] = product of all elements to the right of i
        // result[i] = left[i] * right[i]
        return new int[0];
    }
    
    /**
     * Solution using constant space (O(n) time, O(1) space excluding output array)
     * 
     * @param nums Array of integers
     * @return Array where each element is product of all other elements
     */
    public int[] productExceptSelfConstantSpace(int[] nums) {
        // Write your logic here
        // Hint: Use output array to store left products first
        // Then traverse from right, multiply with right product
        return new int[0];
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ProductOfArrayExceptSelf solution = new ProductOfArrayExceptSelf();
        
        // Test case 1
        int[] nums1 = {1, 2, 3, 4};
        int[] result1 = solution.productExceptSelf(nums1);
        System.out.println("Test 1 - Input: [1,2,3,4]");
        System.out.print("Expected: [24,12,8,6], Got: [");
        for (int i = 0; i < result1.length; i++) {
            System.out.print(result1[i]);
            if (i < result1.length - 1) System.out.print(",");
        }
        System.out.println("]");
        
        // Test case 2
        int[] nums2 = {-1, 1, 0, -3, 3};
        int[] result2 = solution.productExceptSelf(nums2);
        System.out.println("Test 2 - Input: [-1,1,0,-3,3]");
        System.out.print("Expected: [0,0,9,0,0], Got: [");
        for (int i = 0; i < result2.length; i++) {
            System.out.print(result2[i]);
            if (i < result2.length - 1) System.out.print(",");
        }
        System.out.println("]");
    }
}

