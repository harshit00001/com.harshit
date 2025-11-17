package com.harshit.leetcode.backtracking;

/**
 * Problem: Word Search
 * 
 * Given an m x n grid of characters board and a string word, return true if word exists in the grid.
 * 
 * The word can be constructed from letters of sequentially adjacent cells, where adjacent cells
 * are horizontally or vertically neighboring. The same letter cell may not be used more than once.
 * 
 * Example 1:
 * Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
 * Output: true
 * 
 * Example 2:
 * Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"
 * Output: true
 * 
 * Example 3:
 * Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"
 * Output: false
 * 
 * Constraints:
 * - m == board.length
 * - n = board[i].length
 * - 1 <= m, n <= 6
 * - 1 <= word.length <= 15
 * - board and word consists of only lowercase and uppercase English letters.
 */
public class WordSearch {
    
    /**
     * Solution using backtracking/DFS (O(m*n*4^L) time, O(L) space where L is word length)
     * 
     * @param board 2D grid of characters
     * @param word Word to search
     * @return true if word exists, false otherwise
     */
    public boolean exist(char[][] board, String word) {
        // Write your logic here
        // Hint: For each cell, start DFS if first character matches
        // Use backtracking: mark visited, explore 4 directions, unmark visited
        // Return true if word found, false otherwise
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        WordSearch solution = new WordSearch();
        
        // Test case 1
        char[][] board1 = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };
        String word1 = "ABCCED";
        boolean result1 = solution.exist(board1, word1);
        System.out.println("Test 1 - Input: word=\"ABCCED\"");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        String word2 = "SEE";
        boolean result2 = solution.exist(board1, word2);
        System.out.println("Test 2 - Input: word=\"SEE\"");
        System.out.println("Expected: true, Got: " + result2);
        
        // Test case 3
        String word3 = "ABCB";
        boolean result3 = solution.exist(board1, word3);
        System.out.println("Test 3 - Input: word=\"ABCB\"");
        System.out.println("Expected: false, Got: " + result3);
    }
}

