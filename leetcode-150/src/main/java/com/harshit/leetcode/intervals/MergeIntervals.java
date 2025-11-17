package com.harshit.leetcode.intervals;

import java.util.*;

/**
 * Problem: Merge Intervals
 * 
 * Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals,
 * and return an array of the non-overlapping intervals that cover all the intervals in the input.
 * 
 * Example 1:
 * Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
 * Output: [[1,6],[8,10],[15,18]]
 * Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].
 * 
 * Example 2:
 * Input: intervals = [[1,4],[4,5]]
 * Output: [[1,5]]
 * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
 * 
 * Constraints:
 * - 1 <= intervals.length <= 10^4
 * - intervals[i].length == 2
 * - 0 <= starti <= endi <= 10^4
 */
public class MergeIntervals {
    
    /**
     * Solution using sorting (O(n log n) time, O(n) space)
     * 
     * @param intervals Array of intervals
     * @return Merged intervals
     */
    public int[][] merge(int[][] intervals) {
        // Write your logic here
        // Hint: Sort intervals by start time
        // Merge overlapping intervals: if current.start <= previous.end, merge
        // Otherwise, add previous to result and start new interval
        return new int[0][];
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        MergeIntervals solution = new MergeIntervals();
        
        // Test case 1
        int[][] intervals1 = {{1,3},{2,6},{8,10},{15,18}};
        int[][] result1 = solution.merge(intervals1);
        System.out.println("Test 1 - Input: [[1,3],[2,6],[8,10],[15,18]]");
        System.out.print("Result: ");
        for (int[] interval : result1) {
            System.out.print("[" + interval[0] + "," + interval[1] + "] ");
        }
        System.out.println();
        System.out.println("Expected: [[1,6],[8,10],[15,18]]");
        
        // Test case 2
        int[][] intervals2 = {{1,4},{4,5}};
        int[][] result2 = solution.merge(intervals2);
        System.out.println("Test 2 - Input: [[1,4],[4,5]]");
        System.out.print("Result: ");
        for (int[] interval : result2) {
            System.out.print("[" + interval[0] + "," + interval[1] + "] ");
        }
        System.out.println();
        System.out.println("Expected: [[1,5]]");
    }
}

