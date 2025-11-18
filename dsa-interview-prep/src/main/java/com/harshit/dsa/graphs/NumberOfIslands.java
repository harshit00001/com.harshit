package com.harshit.dsa.graphs;

/**
 * Number of Islands - Classic Graph Problem
 * 
 * Problem: Given a 2D grid of '1's (land) and '0's (water),
 * count the number of islands. An island is surrounded by water
 * and formed by connecting adjacent lands horizontally or vertically.
 * 
 * Example:
 * Input: grid = [
 *   ["1","1","0","0","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","1","0","0"],
 *   ["0","0","0","1","1"]
 * ]
 * Output: 3
 * 
 * Approach: DFS or BFS to mark all connected '1's as visited
 * Time: O(m*n), Space: O(m*n) worst case
 */
public class NumberOfIslands {
    
    /**
     * DFS Approach
     */
    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j, rows, cols);
                }
            }
        }
        
        return count;
    }
    
    private static void dfs(char[][] grid, int i, int j, int rows, int cols) {
        // Check boundaries and if current cell is water or visited
        if (i < 0 || i >= rows || j < 0 || j >= cols || grid[i][j] == '0') {
            return;
        }
        
        // Mark as visited (sink the island)
        grid[i][j] = '0';
        
        // Explore all 4 directions
        dfs(grid, i + 1, j, rows, cols); // Down
        dfs(grid, i - 1, j, rows, cols); // Up
        dfs(grid, i, j + 1, rows, cols); // Right
        dfs(grid, i, j - 1, rows, cols); // Left
    }
    
    public static void main(String[] args) {
        char[][] grid = {
            {'1', '1', '0', '0', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '1', '0', '0'},
            {'0', '0', '0', '1', '1'}
        };
        
        System.out.println("Number of Islands: " + numIslands(grid));
    }
}

