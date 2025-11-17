package com.harshit.leetcode.arrays;

import java.util.*;

/**
 * Problem: Valid Sudoku
 * 
 * Determine if a 9 x 9 Sudoku board is valid. Only the filled cells need to be validated
 * according to the following rules:
 * 1. Each row must contain the digits 1-9 without repetition.
 * 2. Each column must contain the digits 1-9 without repetition.
 * 3. Each of the nine 3 x 3 sub-boxes of the grid must contain the digits 1-9 without repetition.
 * 
 * Note: A Sudoku board (partially filled) could be valid but is not necessarily solvable.
 * Only the filled cells need to be validated according to the mentioned rules.
 * 
 * Example 1:
 * Input: board = 
 * [["5","3",".",".","7",".",".",".","."]
 * ,["6",".",".","1","9","5",".",".","."]
 * ,[".","9","8",".",".",".",".","6","."]
 * ,["8",".",".",".","6",".",".",".","3"]
 * ,["4",".",".","8",".","3",".",".","1"]
 * ,["7",".",".",".","2",".",".",".","6"]
 * ,[".","6",".",".",".",".","2","8","."]
 * ,[".",".",".","4","1","9",".",".","5"]
 * ,[".",".",".",".","8",".",".","7","9"]]
 * Output: true
 * 
 * Constraints:
 * - board.length == 9
 * - board[i].length == 9
 * - board[i][j] is a digit 1-9 or '.'.
 */
public class ValidSudoku {
    
    /**
     * Solution using HashSet (O(1) time since fixed 9x9, O(1) space)
     * 
     * @param board 9x9 Sudoku board
     * @return true if valid, false otherwise
     */
    public boolean isValidSudoku(char[][] board) {
        // Write your logic here
        // Hint: Use HashSet to track seen numbers
        // Check rows, columns, and 3x3 boxes
        // Format: "row-5-3", "col-3-5", "box-1-1-5"
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ValidSudoku solution = new ValidSudoku();
        
        char[][] board1 = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        boolean result1 = solution.isValidSudoku(board1);
        System.out.println("Test 1 - Valid Sudoku");
        System.out.println("Expected: true, Got: " + result1);
    }
}
