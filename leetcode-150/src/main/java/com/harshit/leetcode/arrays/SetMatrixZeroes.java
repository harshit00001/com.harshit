package com.harshit.leetcode.arrays;

/**
 * Problem: Set Matrix Zeroes
 * 
 * Given an m x n integer matrix matrix, if an element is 0, set its entire row and column to 0's.
 * 
 * You must do it in place.
 * 
 * Example 1:
 * Input: matrix = [[1,1,1],[1,0,1],[1,1,1]]
 * Output: [[1,0,1],[0,0,0],[1,0,1]]
 * 
 * Example 2:
 * Input: matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
 * Output: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
 * 
 * Constraints:
 * - m == matrix.length
 * - n == matrix[0].length
 * - 1 <= m, n <= 200
 * - -2^31 <= matrix[i][j] <= 2^31 - 1
 * 
 * Follow up:
 * - A straightforward solution using O(mn) space is probably a bad idea.
 * - A simple improvement uses O(m + n) space, but still not the best solution.
 * - Could you devise a constant space solution?
 */
public class SetMatrixZeroes {
    
    /**
     * Solution using O(m+n) space (O(m*n) time, O(m+n) space)
     * 
     * @param matrix Input matrix
     */
    public void setZeroes(int[][] matrix) {
        // Write your logic here
        // Hint: Use two arrays to track which rows and columns have zeros
        // Then set zeros based on these arrays
    }
    
    /**
     * Solution using constant space (O(m*n) time, O(1) space)
     * 
     * @param matrix Input matrix
     */
    public void setZeroesConstantSpace(int[][] matrix) {
        // Write your logic here
        // Hint: Use first row and first column as markers
        // Handle first row and column separately
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        SetMatrixZeroes solution = new SetMatrixZeroes();
        
        // Test case 1
        int[][] matrix1 = {{1,1,1},{1,0,1},{1,1,1}};
        solution.setZeroes(matrix1);
        System.out.println("Test 1 - Input: [[1,1,1],[1,0,1],[1,1,1]]");
        System.out.println("Expected: [[1,0,1],[0,0,0],[1,0,1]]");
    }
}

