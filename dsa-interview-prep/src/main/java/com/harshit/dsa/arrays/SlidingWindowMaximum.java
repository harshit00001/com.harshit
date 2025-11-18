package com.harshit.dsa.arrays;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Sliding Window Maximum
 * 
 * Problem: Given an array and window size k, find maximum in each window.
 * 
 * Example:
 * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * Output: [3,3,5,5,6,7]
 * 
 * Explanation:
 * Window 1: [1,3,-1] -> max = 3
 * Window 2: [3,-1,-3] -> max = 3
 * Window 3: [-1,-3,5] -> max = 5
 * ...
 * 
 * Approaches:
 * 1. Brute Force: O(n*k) time
 * 2. Deque (Optimal): O(n) time, O(k) space
 */
public class SlidingWindowMaximum {
    
    /**
     * Approach 1: Brute Force
     * Time: O(n*k), Space: O(1)
     */
    public static int[] maxSlidingWindowBruteForce(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n - k + 1];
        
        for (int i = 0; i <= n - k; i++) {
            int max = nums[i];
            for (int j = i; j < i + k; j++) {
                max = Math.max(max, nums[j]);
            }
            result[i] = max;
        }
        
        return result;
    }
    
    /**
     * Approach 2: Using Deque (Optimal)
     * Time: O(n), Space: O(k)
     * 
     * Key Idea: Use deque to maintain indices of elements in decreasing order
     * - Front of deque always has index of maximum element in current window
     * - Remove indices outside current window
     * - Remove indices of smaller elements (they can never be maximum)
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>(); // Store indices
        
        for (int i = 0; i < n; i++) {
            // Remove indices outside current window
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            
            // Remove indices of smaller elements (they can't be maximum)
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            
            // Add current index
            deque.offerLast(i);
            
            // If window is complete, add maximum to result
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        
        System.out.println("Array: " + java.util.Arrays.toString(nums));
        System.out.println("Window Size: " + k);
        
        int[] result1 = maxSlidingWindowBruteForce(nums, k);
        System.out.println("Brute Force Result: " + java.util.Arrays.toString(result1));
        
        int[] result2 = maxSlidingWindow(nums, k);
        System.out.println("Optimal Result: " + java.util.Arrays.toString(result2));
    }
}

