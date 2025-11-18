package com.harshit.dsa.dp;

import java.util.Arrays;

/**
 * Longest Increasing Subsequence (LIS)
 * 
 * Problem: Find the length of longest strictly increasing subsequence.
 * 
 * Example:
 * Input: [10,9,2,5,3,7,101,18]
 * Output: 4 (subsequence: [2,3,7,101] or [2,3,7,18])
 * 
 * Approaches:
 * 1. DP: O(n²) time, O(n) space
 * 2. Binary Search: O(n log n) time, O(n) space
 */
public class LongestIncreasingSubsequence {
    
    /**
     * Approach 1: Dynamic Programming
     * dp[i] = length of LIS ending at index i
     * Time: O(n²), Space: O(n)
     */
    public static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // Each element is a subsequence of length 1
        
        int maxLength = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }
        
        return maxLength;
    }
    
    /**
     * Approach 2: Binary Search (Optimal)
     * Maintain array of smallest tail values for all increasing subsequences
     * Time: O(n log n), Space: O(n)
     */
    public static int lengthOfLISBinarySearch(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int[] tails = new int[nums.length];
        int len = 0;
        
        for (int num : nums) {
            int left = 0, right = len;
            
            // Binary search for position to insert/replace
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            tails[left] = num;
            if (left == len) {
                len++;
            }
        }
        
        return len;
    }
    
    public static void main(String[] args) {
        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        
        System.out.println("Array: " + Arrays.toString(nums));
        System.out.println("LIS Length (DP): " + lengthOfLIS(nums));
        System.out.println("LIS Length (Binary Search): " + lengthOfLISBinarySearch(nums));
    }
}

