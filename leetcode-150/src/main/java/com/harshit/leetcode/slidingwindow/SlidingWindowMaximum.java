package com.harshit.leetcode.slidingwindow;

import java.util.*;

/**
 * Problem: Sliding Window Maximum
 * 
 * You are given an array of integers nums, there is a sliding window of size k which is moving
 * from the very left of the array to the very right. You can only see the k numbers in the window.
 * Each time the sliding window moves right by one position.
 * 
 * Return the max sliding window.
 * 
 * Example 1:
 * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * Output: [3,3,5,5,6,7]
 * Explanation:
 * Window position                Max
 * ---------------               -----
 * [1  3  -1] -3  5  3  6  7       3
 *  1 [3  -1  -3] 5  3  6  7       3
 *  1  3 [-1  -3  5] 3  6  7       5
 *  1  3  -1 [-3  5  3] 6  7       5
 *  1  3  -1  -3 [5  3  6] 7       6
 *  1  3  -1  -3  5 [3  6  7]      7
 * 
 * Example 2:
 * Input: nums = [1], k = 1
 * Output: [1]
 * 
 * Constraints:
 * - 1 <= nums.length <= 10^5
 * - -10^4 <= nums[i] <= 10^4
 * - 1 <= k <= nums.length
 */
public class SlidingWindowMaximum {
    
    /**
     * Solution using Deque (O(n) time, O(k) space)
     * 
     * @param nums Array of integers
     * @param k Window size
     * @return Array of maximum values in each sliding window
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        // Write your logic here
        // Hint: Use Deque to store indices in decreasing order of values
        // Remove indices outside window from front
        // Remove indices with smaller values from back
        // Front of deque always has index of max element in current window
        return new int[0];
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        SlidingWindowMaximum solution = new SlidingWindowMaximum();
        
        // Test case 1
        int[] nums1 = {1,3,-1,-3,5,3,6,7};
        int k1 = 3;
        int[] result1 = solution.maxSlidingWindow(nums1, k1);
        System.out.println("Test 1 - Input: [1,3,-1,-3,5,3,6,7], k=3");
        System.out.print("Expected: [3,3,5,5,6,7], Got: [");
        for (int i = 0; i < result1.length; i++) {
            System.out.print(result1[i]);
            if (i < result1.length - 1) System.out.print(",");
        }
        System.out.println("]");
        
        // Test case 2
        int[] nums2 = {1};
        int k2 = 1;
        int[] result2 = solution.maxSlidingWindow(nums2, k2);
        System.out.println("Test 2 - Input: [1], k=1");
        System.out.println("Expected: [1], Got: [" + result2[0] + "]");
    }
}

