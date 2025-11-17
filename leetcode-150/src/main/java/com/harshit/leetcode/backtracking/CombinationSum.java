package com.harshit.leetcode.backtracking;

import java.util.*;

/**
 * Problem: Combination Sum
 * 
 * Given an array of distinct integers candidates and a target integer target, return a list
 * of all unique combinations of candidates where the chosen numbers sum to target. You may
 * return the combinations in any order.
 * 
 * The same number may be chosen from candidates an unlimited number of times. Two combinations
 * are unique if the frequency of at least one of the chosen numbers is different.
 * 
 * The test cases are generated such that the number of unique combinations that sum up to target
 * is less than 150 combinations for the given input.
 * 
 * Example 1:
 * Input: candidates = [2,3,6,7], target = 7
 * Output: [[2,2,3],[7]]
 * Explanation:
 * 2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
 * 7 is a candidate, and 7 = 7.
 * These are the only two combinations.
 * 
 * Example 2:
 * Input: candidates = [2,3,5], target = 8
 * Output: [[2,2,2,2],[2,3,3],[3,5]]
 * 
 * Example 3:
 * Input: candidates = [2], target = 1
 * Output: []
 * 
 * Constraints:
 * - 1 <= candidates.length <= 30
 * - 2 <= candidates[i] <= 40
 * - All elements of candidates are distinct.
 * - 1 <= target <= 40
 */
public class CombinationSum {
    
    /**
     * Solution using backtracking (O(2^target) time, O(target) space)
     * 
     * @param candidates Array of distinct integers
     * @param target Target sum
     * @return List of all unique combinations
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        // Write your logic here
        // Hint: Use backtracking
        // Try each candidate, add to current combination
        // If sum == target, add to result
        // If sum < target, recurse (can reuse same candidate)
        // Backtrack by removing last added candidate
        return new ArrayList<>();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        CombinationSum solution = new CombinationSum();
        
        // Test case 1
        int[] candidates1 = {2,3,6,7};
        int target1 = 7;
        List<List<Integer>> result1 = solution.combinationSum(candidates1, target1);
        System.out.println("Test 1 - Input: candidates=[2,3,6,7], target=7");
        System.out.println("Result: " + result1);
        System.out.println("Expected: [[2,2,3],[7]]");
        
        // Test case 2
        int[] candidates2 = {2,3,5};
        int target2 = 8;
        List<List<Integer>> result2 = solution.combinationSum(candidates2, target2);
        System.out.println("Test 2 - Input: candidates=[2,3,5], target=8");
        System.out.println("Result: " + result2);
        System.out.println("Expected: [[2,2,2,2],[2,3,3],[3,5]]");
    }
}

