package com.harshit.leetcode.arrays;

/**
 * Problem: Rotate Image
 * 
 * You are given an n x n 2D matrix representing an image, rotate the image by 90 degrees (clockwise).
 * 
 * You have to rotate the image in-place, which means you have to modify the input 2D matrix directly.
 * DO NOT allocate another 2D matrix and do the rotation.
 * 
 * Example 1:
 * Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: [[7,4,1],[8,5,2],[9,6,3]]
 * 
 * Example 2:
 * Input: matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
 * Output: [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
 * 
 * Constraints:
 * - n == matrix.length == matrix[i].length
 * - 1 <= n <= 20
 * - -1000 <= matrix[i][j] <= 1000
 */
public class RotateImage {
    
    /**
     * Solution using transpose and reverse (O(n^2) time, O(1) space)
     * 
     * @param matrix Input n x n matrix
     */
    public void rotate(int[][] matrix) {
        // Write your logic here
        // Hint: 1. Transpose the matrix (swap matrix[i][j] with matrix[j][i])
        // 2. Reverse each row
        // This gives 90-degree clockwise rotation
    }
    
    /**
     * Solution using layer-by-layer rotation (O(n^2) time, O(1) space)
     * 
     * @param matrix Input n x n matrix
     */
    public void rotateLayerByLayer(int[][] matrix) {
        // Write your logic here
        // Hint: Rotate four corners at a time
        // For each layer, rotate elements in groups of 4
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        RotateImage solution = new RotateImage();
        
        // Test case 1
        int[][] matrix1 = {{1,2,3},{4,5,6},{7,8,9}};
        solution.rotate(matrix1);
        System.out.println("Test 1 - Input: [[1,2,3],[4,5,6],[7,8,9]]");
        System.out.println("Expected: [[7,4,1],[8,5,2],[9,6,3]]");
    }
}

