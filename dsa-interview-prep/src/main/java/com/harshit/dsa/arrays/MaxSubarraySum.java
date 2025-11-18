package com.harshit.dsa.arrays;

/**
 * ===================================================================
 * MAXIMUM SUBARRAY SUM (KADANE'S ALGORITHM)
 * ===================================================================
 * 
 * PROBLEM STATEMENT:
 * Given an integer array nums, find the contiguous subarray
 * (containing at least one number) which has the largest sum
 * and return its sum.
 * 
 * A subarray is a contiguous part of an array.
 * 
 * ===================================================================
 * EXAMPLES:
 * ===================================================================
 * 
 * Example 1:
 * Input: nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
 * Output: 6
 * Explanation: [4, -1, 2, 1] has the largest sum = 6
 * 
 * Let's visualize:
 * Array:  [-2,  1, -3,  4, -1,  2,  1, -5,  4]
 * Index:   0   1   2   3   4   5   6   7   8
 * 
 * Subarray [4, -1, 2, 1] from index 3 to 6:
 * Sum = 4 + (-1) + 2 + 1 = 6
 * 
 * Example 2:
 * Input: nums = [1]
 * Output: 1
 * 
 * Example 3:
 * Input: nums = [5, 4, -1, 7, 8]
 * Output: 23
 * Explanation: The entire array [5, 4, -1, 7, 8] has sum = 23
 * 
 * Example 4:
 * Input: nums = [-1]
 * Output: -1
 * (Even if all negative, we must return the maximum)
 * 
 * ===================================================================
 * BRUTE FORCE APPROACH (Not Optimal):
 * ===================================================================
 * 
 * Try all possible subarrays:
 * - Start from index 0, try lengths 1, 2, 3, ..., n
 * - Start from index 1, try lengths 1, 2, 3, ..., n-1
 * - And so on...
 * 
 * Time: O(n³) or O(n²) with optimization
 * Space: O(1)
 * 
 * This is too slow! We need a better approach.
 * 
 * ===================================================================
 * KADANE'S ALGORITHM (Optimal):
 * ===================================================================
 * 
 * KEY INSIGHT:
 * At each position i, we ask: "What's the maximum sum subarray
 * ending at position i?"
 * 
 * We have two choices:
 * 1. Start a NEW subarray from current element: nums[i]
 * 2. EXTEND the previous subarray: currentSum + nums[i]
 * 
 * We choose whichever gives us a larger sum!
 * 
 * FORMULA:
 * currentSum = max(nums[i], currentSum + nums[i])
 * 
 * Then update the global maximum:
 * maxSum = max(maxSum, currentSum)
 * 
 * ===================================================================
 * INTUITION - WHY THIS WORKS:
 * ===================================================================
 * 
 * Think of it this way:
 * - If currentSum becomes negative, it's better to start fresh
 *   because adding a negative number to any future positive number
 *   will only make it smaller
 * 
 * - If currentSum is positive, we keep extending because it might
 *   help us get a larger sum later
 * 
 * Example walkthrough:
 * nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
 * 
 * i=0: nums[0] = -2
 *      currentSum = max(-2, 0 + -2) = -2
 *      maxSum = max(-2, -2) = -2
 * 
 * i=1: nums[1] = 1
 *      currentSum = max(1, -2 + 1) = max(1, -1) = 1
 *      (Start fresh! Previous sum was negative)
 *      maxSum = max(-2, 1) = 1
 * 
 * i=2: nums[2] = -3
 *      currentSum = max(-3, 1 + -3) = max(-3, -2) = -2
 *      maxSum = max(1, -2) = 1
 * 
 * i=3: nums[3] = 4
 *      currentSum = max(4, -2 + 4) = max(4, 2) = 4
 *      (Start fresh! Previous sum was negative)
 *      maxSum = max(1, 4) = 4
 * 
 * i=4: nums[4] = -1
 *      currentSum = max(-1, 4 + -1) = max(-1, 3) = 3
 *      (Extend! Previous sum was positive)
 *      maxSum = max(4, 3) = 4
 * 
 * i=5: nums[5] = 2
 *      currentSum = max(2, 3 + 2) = max(2, 5) = 5
 *      (Extend!)
 *      maxSum = max(4, 5) = 5
 * 
 * i=6: nums[6] = 1
 *      currentSum = max(1, 5 + 1) = max(1, 6) = 6
 *      (Extend!)
 *      maxSum = max(5, 6) = 6 ✓ (This is our answer!)
 * 
 * i=7: nums[7] = -5
 *      currentSum = max(-5, 6 + -5) = max(-5, 1) = 1
 *      maxSum = max(6, 1) = 6
 * 
 * i=8: nums[8] = 4
 *      currentSum = max(4, 1 + 4) = max(4, 5) = 5
 *      maxSum = max(6, 5) = 6
 * 
 * Final Answer: 6
 * 
 * ===================================================================
 * TIME & SPACE COMPLEXITY:
 * ===================================================================
 * 
 * Time Complexity: O(n)
 *   - Single pass through the array
 *   - Constant work at each step
 * 
 * Space Complexity: O(1)
 *   - Only using a few variables (maxSum, currentSum)
 *   - No extra data structures needed
 * 
 * ===================================================================
 * WHEN TO USE KADANE'S ALGORITHM:
 * ===================================================================
 * 
 * - Finding maximum/minimum sum subarray
 * - Finding maximum product subarray (with modifications)
 * - Stock trading problems (buy and sell)
 * - Any problem involving contiguous subarrays
 * 
 * ===================================================================
 */
public class MaxSubarraySum {
    
    /**
     * KADANE'S ALGORITHM - Optimal Solution
     * 
     * This is one of the most elegant and important algorithms
     * you must know for interviews!
     * 
     * @param nums Array of integers (can contain negative numbers)
     * @return Maximum sum of any contiguous subarray
     */
    public static int maxSubarraySum(int[] nums) {
        // Edge case: Empty array
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // Initialize:
        // maxSum = best sum we've seen so far
        // currentSum = best sum ending at current position
        int maxSum = nums[0];
        int currentSum = nums[0];
        
        // Process each element starting from index 1
        for (int i = 1; i < nums.length; i++) {
            // KEY DECISION: Should we start fresh or extend?
            // 
            // If currentSum is negative, starting fresh (nums[i]) is better
            // If currentSum is positive, extending (currentSum + nums[i]) is better
            //
            // This is equivalent to:
            // currentSum = max(nums[i], currentSum + nums[i])
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            
            // Update global maximum
            // This tracks the best sum we've seen across all positions
            maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
    }
    
    /**
     * Extended: Also return the subarray indices
     */
    public static int[] maxSubarrayWithIndices(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[]{0, 0, 0};
        }
        
        int maxSum = nums[0];
        int currentSum = nums[0];
        int start = 0, end = 0, tempStart = 0;
        
        for (int i = 1; i < nums.length; i++) {
            if (currentSum < 0) {
                currentSum = nums[i];
                tempStart = i;
            } else {
                currentSum += nums[i];
            }
            
            if (currentSum > maxSum) {
                maxSum = currentSum;
                start = tempStart;
                end = i;
            }
        }
        
        return new int[]{maxSum, start, end};
    }
    
    public static void main(String[] args) {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        
        System.out.println("Array: " + java.util.Arrays.toString(nums));
        
        int maxSum = maxSubarraySum(nums);
        System.out.println("Maximum Subarray Sum: " + maxSum);
        
        int[] result = maxSubarrayWithIndices(nums);
        System.out.println("Max Sum: " + result[0]);
        System.out.println("Subarray from index " + result[1] + " to " + result[2]);
        System.out.print("Subarray: [");
        for (int i = result[1]; i <= result[2]; i++) {
            System.out.print(nums[i] + (i < result[2] ? ", " : ""));
        }
        System.out.println("]");
    }
}

