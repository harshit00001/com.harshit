package com.harshit.leetcode.graphs;

/**
 * Problem: Number of Islands
 * 
 * Given an m x n 2D binary grid grid which represents a map of '1's (land) and '0's (water),
 * return the number of islands.
 * 
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally
 * or vertically. You may assume all four edges of the grid are all surrounded by water.
 * 
 * Example 1:
 * Input: grid = [
 *   ["1","1","1","1","0"],
 *   ["1","1","0","1","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","0","0","0"]
 * ]
 * Output: 1
 * 
 * Example 2:
 * Input: grid = [
 *   ["1","1","0","0","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","1","0","0"],
 *   ["0","0","0","1","1"]
 * ]
 * Output: 3
 * 
 * Constraints:
 * - m == grid.length
 * - n == grid[i].length
 * - 1 <= m, n <= 300
 * - grid[i][j] is '0' or '1'.
 */
public class NumberOfIslands {
    
    /**
     * Solution using DFS (O(m*n) time, O(m*n) space for recursion)
     * 
     * @param grid 2D grid of '1's and '0's
     * @return Number of islands
     */
    public int numIslands(char[][] grid) {
        // Write your logic here
        // Hint: For each '1', do DFS to mark all connected '1's as visited
        // Count number of DFS calls (each is an island)
        return 0;
    }
    
    /**
     * Solution using BFS (O(m*n) time, O(min(m,n)) space)
     * 
     * @param grid 2D grid of '1's and '0's
     * @return Number of islands
     */
    public int numIslandsBFS(char[][] grid) {
        // Write your logic here
        // Hint: Use queue for BFS traversal
        // Mark visited cells to avoid revisiting
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        NumberOfIslands solution = new NumberOfIslands();
        
        // Test case 1
        char[][] grid1 = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        int result1 = solution.numIslands(grid1);
        System.out.println("Test 1 - Grid with 1 island");
        System.out.println("Expected: 1, Got: " + result1);
        
        // Test case 2
        char[][] grid2 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        int result2 = solution.numIslands(grid2);
        System.out.println("Test 2 - Grid with 3 islands");
        System.out.println("Expected: 3, Got: " + result2);
    }
}

