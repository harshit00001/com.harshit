package com.harshit.leetcode.binarysearch;

/**
 * Problem: Search a 2D Matrix
 * 
 * You are given an m x n integer matrix matrix with the following two properties:
 * - Each row is sorted in non-decreasing order.
 * - The first integer of each row is greater than the last integer of the previous row.
 * 
 * Given an integer target, return true if target is in matrix or false otherwise.
 * 
 * You must write a solution in O(log(m * n)) time complexity.
 * 
 * Example 1:
 * Input: matrix = [[1,4,7,11],[2,5,8,12],[3,6,9,16],[10,13,14,17]], target = 5
 * Output: true
 * 
 * Example 2:
 * Input: matrix = [[1,4,7,11],[2,5,8,12],[3,6,9,16],[10,13,14,17]], target = 3
 * Output: false
 * 
 * Constraints:
 * - m == matrix.length
 * - n == matrix[i].length
 * - 1 <= m, n <= 100
 * - -10^4 <= matrix[i][j], target <= 10^4
 */
public class Search2DMatrix {
    
    /**
     * Solution using binary search treating matrix as 1D array (O(log(m*n)) time, O(1) space)
     * 
     * @param matrix 2D sorted matrix
     * @param target Target value
     * @return true if target found, false otherwise
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        // Write your logic here
        // Hint: Treat 2D matrix as 1D array
        // Convert index: row = mid / n, col = mid % n
        // Use binary search
        return false;
    }
    
    /**
     * Solution using two binary searches (O(log m + log n) time, O(1) space)
     * 
     * @param matrix 2D sorted matrix
     * @param target Target value
     * @return true if target found, false otherwise
     */
    public boolean searchMatrixTwoPass(int[][] matrix, int target) {
        // Write your logic here
        // Hint: First binary search to find correct row
        // Then binary search in that row
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        Search2DMatrix solution = new Search2DMatrix();
        
        // Test case 1
        int[][] matrix1 = {{1,4,7,11},{2,5,8,12},{3,6,9,16},{10,13,14,17}};
        int target1 = 5;
        boolean result1 = solution.searchMatrix(matrix1, target1);
        System.out.println("Test 1 - target=5");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        int target2 = 3;
        boolean result2 = solution.searchMatrix(matrix1, target2);
        System.out.println("Test 2 - target=3");
        System.out.println("Expected: false, Got: " + result2);
    }
}

