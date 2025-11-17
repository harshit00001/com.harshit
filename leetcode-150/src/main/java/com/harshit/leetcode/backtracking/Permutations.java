package com.harshit.leetcode.backtracking;

import java.util.*;

/**
 * Problem: Permutations
 * 
 * Given an array nums of distinct integers, return all the possible permutations.
 * You can return the answer in any order.
 * 
 * Example 1:
 * Input: nums = [1,2,3]
 * Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * 
 * Example 2:
 * Input: nums = [0,1]
 * Output: [[0,1],[1,0]]
 * 
 * Example 3:
 * Input: nums = [1]
 * Output: [[1]]
 * 
 * Constraints:
 * - 1 <= nums.length <= 6
 * - -10 <= nums[i] <= 10
 * - All the integers of nums are unique.
 */
public class Permutations {
    
    /**
     * Solution using backtracking (O(n! * n) time, O(n) space)
     * 
     * @param nums Array of distinct integers
     * @return List of all permutations
     */
    public List<List<Integer>> permute(int[] nums) {
        // Write your logic here
        // Hint: Use backtracking with swapping or visited array
        // Try each unused number at current position
        // Recurse for next position
        // Backtrack by undoing swap or unmarking visited
        return new ArrayList<>();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        Permutations solution = new Permutations();
        
        // Test case 1
        int[] nums1 = {1,2,3};
        List<List<Integer>> result1 = solution.permute(nums1);
        System.out.println("Test 1 - Input: [1,2,3]");
        System.out.println("Result size: " + result1.size());
        System.out.println("Expected: 6 permutations");
        System.out.println("Result: " + result1);
        
        // Test case 2
        int[] nums2 = {0,1};
        List<List<Integer>> result2 = solution.permute(nums2);
        System.out.println("Test 2 - Input: [0,1]");
        System.out.println("Result: " + result2);
        System.out.println("Expected: [[0,1],[1,0]]");
    }
}

