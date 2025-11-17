package com.harshit.leetcode.arrays;

import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Contains Duplicate
 * 
 * Given an integer array nums, return true if any value appears at least twice in the array,
 * and return false if every element is distinct.
 * 
 * Example 1:
 * Input: nums = [1,2,3,1]
 * Output: true
 * 
 * Example 2:
 * Input: nums = [1,2,3,4]
 * Output: false
 * 
 * Example 3:
 * Input: nums = [1,1,1,3,3,4,3,2,4,2]
 * Output: true
 * 
 * Constraints:
 * - 1 <= nums.length <= 10^5
 * - -10^9 <= nums[i] <= 10^9
 */
public class ContainsDuplicate {
    
    /**
     * Solution using HashSet (O(n) time, O(n) space)
     * 
     * @param nums Array of integers
     * @return true if array contains duplicates, false otherwise
     */
    public boolean containsDuplicate(int[] nums) {
        // Write your logic here
        // Hint: Use HashSet to track seen numbers
        // If number already in set, return true
        return false;
    }
    
    /**
     * Solution using sorting (O(n log n) time, O(1) space)
     * 
     * @param nums Array of integers
     * @return true if array contains duplicates, false otherwise
     */
    public boolean containsDuplicateSorting(int[] nums) {
        // Write your logic here
        // Hint: Sort array first, then check adjacent elements
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ContainsDuplicate solution = new ContainsDuplicate();
        
        // Test case 1
        int[] nums1 = {1, 2, 3, 1};
        boolean result1 = solution.containsDuplicate(nums1);
        System.out.println("Test 1 - Input: [1,2,3,1]");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {1, 2, 3, 4};
        boolean result2 = solution.containsDuplicate(nums2);
        System.out.println("Test 2 - Input: [1,2,3,4]");
        System.out.println("Expected: false, Got: " + result2);
        
        // Test case 3
        int[] nums3 = {1, 1, 1, 3, 3, 4, 3, 2, 4, 2};
        boolean result3 = solution.containsDuplicate(nums3);
        System.out.println("Test 3 - Input: [1,1,1,3,3,4,3,2,4,2]");
        System.out.println("Expected: true, Got: " + result3);
    }
}

