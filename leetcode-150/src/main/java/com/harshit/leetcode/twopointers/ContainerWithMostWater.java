package com.harshit.leetcode.twopointers;

/**
 * Problem: Container With Most Water
 * 
 * You are given an integer array height of length n. There are n vertical lines drawn such that
 * the two endpoints of the ith line are (i, 0) and (i, height[i]).
 * 
 * Find two lines that together with the x-axis form a container, such that the container contains
 * the most water.
 * 
 * Return the maximum amount of water a container can store.
 * 
 * Notice that you may not slant the container.
 * 
 * Example 1:
 * Input: height = [1,8,6,2,5,4,8,3,7]
 * Output: 49
 * Explanation: The above vertical lines are represented by array [1,8,6,2,5,4,8,3,7].
 * In this case, the max area of water (blue section) the container can contain is 49.
 * 
 * Example 2:
 * Input: height = [1,1]
 * Output: 1
 * 
 * Constraints:
 * - n == height.length
 * - 2 <= n <= 10^5
 * - 0 <= height[i] <= 10^4
 */
public class ContainerWithMostWater {
    
    /**
     * Solution using two pointers (O(n) time, O(1) space)
     * 
     * @param height Array of heights
     * @return Maximum area of water that can be contained
     */
    public int maxArea(int[] height) {
        // Write your logic here
        // Hint: Use two pointers at start and end
        // Calculate area = min(height[left], height[right]) * (right - left)
        // Move pointer with smaller height
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ContainerWithMostWater solution = new ContainerWithMostWater();
        
        // Test case 1
        int[] height1 = {1,8,6,2,5,4,8,3,7};
        int result1 = solution.maxArea(height1);
        System.out.println("Test 1 - Input: [1,8,6,2,5,4,8,3,7]");
        System.out.println("Expected: 49, Got: " + result1);
        
        // Test case 2
        int[] height2 = {1,1};
        int result2 = solution.maxArea(height2);
        System.out.println("Test 2 - Input: [1,1]");
        System.out.println("Expected: 1, Got: " + result2);
    }
}

