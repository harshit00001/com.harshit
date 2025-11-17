package com.harshit.leetcode.stack;

import java.util.*;

/**
 * Problem: Largest Rectangle in Histogram
 * 
 * Given an array of integers heights representing the histogram's bar height where the width
 * of each bar is 1, return the area of the largest rectangle in the histogram.
 * 
 * Example 1:
 * Input: heights = [2,1,5,6,2,3]
 * Output: 10
 * Explanation: The above is a histogram where width of each bar is 1.
 * The largest rectangle is shown in the red area, which has an area = 10 units.
 * 
 * Example 2:
 * Input: heights = [2,4]
 * Output: 4
 * 
 * Constraints:
 * - 1 <= heights.length <= 10^5
 * - 0 <= heights[i] <= 10^4
 */
public class LargestRectangleInHistogram {
    
    /**
     * Solution using Stack (O(n) time, O(n) space)
     * 
     * @param heights Array of bar heights
     * @return Area of largest rectangle
     */
    public int largestRectangleArea(int[] heights) {
        // Write your logic here
        // Hint: Use stack to store indices in increasing order of heights
        // When smaller height found, calculate area with popped height
        // Area = height[popped] * (current_index - stack_top - 1)
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        LargestRectangleInHistogram solution = new LargestRectangleInHistogram();
        
        // Test case 1
        int[] heights1 = {2,1,5,6,2,3};
        int result1 = solution.largestRectangleArea(heights1);
        System.out.println("Test 1 - Input: [2,1,5,6,2,3]");
        System.out.println("Expected: 10, Got: " + result1);
        
        // Test case 2
        int[] heights2 = {2,4};
        int result2 = solution.largestRectangleArea(heights2);
        System.out.println("Test 2 - Input: [2,4]");
        System.out.println("Expected: 4, Got: " + result2);
    }
}

