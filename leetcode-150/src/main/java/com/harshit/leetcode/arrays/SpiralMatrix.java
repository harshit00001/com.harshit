package com.harshit.leetcode.arrays;

import java.util.*;

/**
 * Problem: Spiral Matrix
 * 
 * Given an m x n matrix, return all elements of the matrix in spiral order.
 * 
 * Example 1:
 * Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: [1,2,3,6,9,8,7,4,5]
 * 
 * Example 2:
 * Input: matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
 * Output: [1,2,3,4,8,12,11,10,9,5,6,7]
 * 
 * Constraints:
 * - m == matrix.length
 * - n == matrix[i].length
 * - 1 <= m, n <= 10
 * - -100 <= matrix[i][j] <= 100
 */
public class SpiralMatrix {
    
    /**
     * Solution using boundaries (O(m*n) time, O(1) space excluding output)
     * 
     * @param matrix Input matrix
     * @return List of elements in spiral order
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        // Write your logic here
        // Hint: Use four boundaries: top, bottom, left, right
        // Traverse right, down, left, up
        // Update boundaries after each direction
        return new ArrayList<>();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        SpiralMatrix solution = new SpiralMatrix();
        
        // Test case 1
        int[][] matrix1 = {{1,2,3},{4,5,6},{7,8,9}};
        List<Integer> result1 = solution.spiralOrder(matrix1);
        System.out.println("Test 1 - Input: [[1,2,3],[4,5,6],[7,8,9]]");
        System.out.println("Result: " + result1);
        System.out.println("Expected: [1,2,3,6,9,8,7,4,5]");
        
        // Test case 2
        int[][] matrix2 = {{1,2,3,4},{5,6,7,8},{9,10,11,12}};
        List<Integer> result2 = solution.spiralOrder(matrix2);
        System.out.println("Test 2 - Input: [[1,2,3,4],[5,6,7,8],[9,10,11,12]]");
        System.out.println("Result: " + result2);
        System.out.println("Expected: [1,2,3,4,8,12,11,10,9,5,6,7]");
    }
}

