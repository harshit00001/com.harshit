package com.harshit.leetcode.stack;

import java.util.*;

/**
 * Problem: Daily Temperatures
 * 
 * Given an array of integers temperatures represents the daily temperatures, return an array
 * answer such that answer[i] is the number of days you have to wait after the ith day to get
 * a warmer temperature. If there is no future day for which this is possible, keep answer[i] == 0 instead.
 * 
 * Example 1:
 * Input: temperatures = [73,74,75,71,69,72,76,73]
 * Output: [1,1,4,2,1,1,0,0]
 * 
 * Example 2:
 * Input: temperatures = [30,40,50,60]
 * Output: [1,1,1,0]
 * 
 * Example 3:
 * Input: temperatures = [30,60,90]
 * Output: [1,1,0]
 * 
 * Constraints:
 * - 1 <= temperatures.length <= 10^5
 * - 30 <= temperatures[i] <= 100
 */
public class DailyTemperatures {
    
    /**
     * Solution using Stack (O(n) time, O(n) space)
     * 
     * @param temperatures Array of daily temperatures
     * @return Array of days to wait for warmer temperature
     */
    public int[] dailyTemperatures(int[] temperatures) {
        // Write your logic here
        // Hint: Use stack to store indices
        // For each temperature, pop indices with lower temperatures
        // Calculate difference in indices
        return new int[0];
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        DailyTemperatures solution = new DailyTemperatures();
        
        // Test case 1
        int[] temps1 = {73,74,75,71,69,72,76,73};
        int[] result1 = solution.dailyTemperatures(temps1);
        System.out.println("Test 1 - Input: [73,74,75,71,69,72,76,73]");
        System.out.print("Expected: [1,1,4,2,1,1,0,0], Got: [");
        for (int i = 0; i < result1.length; i++) {
            System.out.print(result1[i]);
            if (i < result1.length - 1) System.out.print(",");
        }
        System.out.println("]");
        
        // Test case 2
        int[] temps2 = {30,40,50,60};
        int[] result2 = solution.dailyTemperatures(temps2);
        System.out.println("Test 2 - Input: [30,40,50,60]");
        System.out.print("Expected: [1,1,1,0], Got: [");
        for (int i = 0; i < result2.length; i++) {
            System.out.print(result2[i]);
            if (i < result2.length - 1) System.out.print(",");
        }
        System.out.println("]");
    }
}

