package com.harshit.leetcode.backtracking;

import java.util.*;

/**
 * Problem: Subsets
 * 
 * Given an integer array nums of unique elements, return all possible subsets (the power set).
 * 
 * The solution set must not contain duplicate subsets. Return the solution in any order.
 * 
 * Example 1:
 * Input: nums = [1,2,3]
 * Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 * 
 * Example 2:
 * Input: nums = [0]
 * Output: [[],[0]]
 * 
 * Constraints:
 * - 1 <= nums.length <= 10
 * - -10 <= nums[i] <= 10
 * - All the numbers of nums are unique.
 */
public class Subsets {
    
    /**
     * Solution using backtracking (O(2^n) time, O(n) space)
     * 
     * @param nums Array of unique integers
     * @return List of all subsets
     */
    public List<List<Integer>> subsets(int[] nums) {
        // Write your logic here
        // Hint: For each element, either include it or exclude it
        // Use backtracking to generate all combinations
        // Add current subset to result at each step
        return new ArrayList<>();
    }
    
    /**
     * Solution using bit manipulation (O(2^n * n) time, O(1) space excluding output)
     * 
     * @param nums Array of unique integers
     * @return List of all subsets
     */
    public List<List<Integer>> subsetsBitManipulation(int[] nums) {
        // Write your logic here
        // Hint: Use bitmask to represent which elements to include
        // For each number 0 to 2^n - 1, create subset based on set bits
        return new ArrayList<>();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        Subsets solution = new Subsets();
        
        // Test case 1
        int[] nums1 = {1,2,3};
        List<List<Integer>> result1 = solution.subsets(nums1);
        System.out.println("Test 1 - Input: [1,2,3]");
        System.out.println("Result size: " + result1.size());
        System.out.println("Expected: 8 subsets");
        System.out.println("Result: " + result1);
        
        // Test case 2
        int[] nums2 = {0};
        List<List<Integer>> result2 = solution.subsets(nums2);
        System.out.println("Test 2 - Input: [0]");
        System.out.println("Result: " + result2);
        System.out.println("Expected: [[],[0]]");
    }
}

