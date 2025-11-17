package com.harshit.leetcode.twopointers;

/**
 * Problem: Move Zeroes
 * 
 * Given an integer array nums, move all 0's to the end of it while maintaining the relative
 * order of the non-zero elements.
 * 
 * Note that you must do this in-place without making a copy of the array.
 * 
 * Example 1:
 * Input: nums = [0,1,0,3,12]
 * Output: [1,3,12,0,0]
 * 
 * Example 2:
 * Input: nums = [0]
 * Output: [0]
 * 
 * Constraints:
 * - 1 <= nums.length <= 10^4
 * - -2^31 <= nums[i] <= 2^31 - 1
 * 
 * Follow up: Could you minimize the total number of operations done?
 */
public class MoveZeroes {
    
    /**
     * Solution using two pointers (O(n) time, O(1) space)
     * 
     * @param nums Array with zeros
     */
    public void moveZeroes(int[] nums) {
        // Write your logic here
        // Hint: Use two pointers
        // One pointer for position to place next non-zero
        // One pointer to scan array
        // Swap or copy non-zero elements to front
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        MoveZeroes solution = new MoveZeroes();
        
        // Test case 1
        int[] nums1 = {0,1,0,3,12};
        solution.moveZeroes(nums1);
        System.out.println("Test 1 - Input: [0,1,0,3,12]");
        System.out.print("Result: [");
        for (int i = 0; i < nums1.length; i++) {
            System.out.print(nums1[i]);
            if (i < nums1.length - 1) System.out.print(",");
        }
        System.out.println("]");
        System.out.println("Expected: [1,3,12,0,0]");
    }
}

