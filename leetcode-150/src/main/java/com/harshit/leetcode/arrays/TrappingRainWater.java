package com.harshit.leetcode.arrays;

/**
 * Problem: Trapping Rain Water
 * 
 * Given n non-negative integers representing an elevation map where the width of each bar is 1,
 * compute how much water it can trap after raining.
 * 
 * Example 1:
 * Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * Output: 6
 * Explanation: The above elevation map (black section) is represented by array [0,1,0,2,1,0,1,3,2,1,2,1].
 * In this case, 6 units of rain water (blue section) are being trapped.
 * 
 * Example 2:
 * Input: height = [4,2,0,3,2,5]
 * Output: 9
 * 
 * Constraints:
 * - n == height.length
 * - 1 <= n <= 2 * 10^4
 * - 0 <= height[i] <= 10^5
 */
public class TrappingRainWater {
    
    /**
     * Solution using two pointers (O(n) time, O(1) space)
     * 
     * @param height Array of elevations
     * @return Total trapped water
     */
    public int trap(int[] height) {
        // Write your logic here
        // Hint: Use two pointers from both ends
        // Track max height from left and right
        // Water trapped = min(maxLeft, maxRight) - current height
        return 0;
    }
    
    /**
     * Solution using stack (O(n) time, O(n) space)
     * 
     * @param height Array of elevations
     * @return Total trapped water
     */
    public int trapStack(int[] height) {
        // Write your logic here
        // Hint: Use stack to store indices
        // When height[i] > height[stack.top], calculate trapped water
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        TrappingRainWater solution = new TrappingRainWater();
        
        // Test case 1
        int[] height1 = {0,1,0,2,1,0,1,3,2,1,2,1};
        int result1 = solution.trap(height1);
        System.out.println("Test 1 - Input: [0,1,0,2,1,0,1,3,2,1,2,1]");
        System.out.println("Expected: 6, Got: " + result1);
        
        // Test case 2
        int[] height2 = {4,2,0,3,2,5};
        int result2 = solution.trap(height2);
        System.out.println("Test 2 - Input: [4,2,0,3,2,5]");
        System.out.println("Expected: 9, Got: " + result2);
    }
}

