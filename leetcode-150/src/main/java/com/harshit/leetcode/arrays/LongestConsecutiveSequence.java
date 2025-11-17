package com.harshit.leetcode.arrays;

import java.util.*;

/**
 * Problem: Longest Consecutive Sequence
 * 
 * Given an unsorted array of integers nums, return the length of the longest consecutive
 * elements sequence.
 * 
 * You must write an algorithm that runs in O(n) time.
 * 
 * Example 1:
 * Input: nums = [100,4,200,1,3,2]
 * Output: 4
 * Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its length is 4.
 * 
 * Example 2:
 * Input: nums = [0,3,7,2,5,8,4,6,0,1]
 * Output: 9
 * 
 * Constraints:
 * - 0 <= nums.length <= 10^5
 * - -10^9 <= nums[i] <= 10^9
 */
public class LongestConsecutiveSequence {
    
    /**
     * Solution using HashSet (O(n) time, O(n) space)
     * 
     * @param nums Array of integers
     * @return Length of longest consecutive sequence
     */
    public int longestConsecutive(int[] nums) {
        // Write your logic here
        // Hint: Add all numbers to HashSet
        // For each number, check if it's start of sequence (num-1 not in set)
        // If start, count consecutive numbers
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        LongestConsecutiveSequence solution = new LongestConsecutiveSequence();
        
        // Test case 1
        int[] nums1 = {100,4,200,1,3,2};
        int result1 = solution.longestConsecutive(nums1);
        System.out.println("Test 1 - Input: [100,4,200,1,3,2]");
        System.out.println("Expected: 4, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {0,3,7,2,5,8,4,6,0,1};
        int result2 = solution.longestConsecutive(nums2);
        System.out.println("Test 2 - Input: [0,3,7,2,5,8,4,6,0,1]");
        System.out.println("Expected: 9, Got: " + result2);
    }
}

