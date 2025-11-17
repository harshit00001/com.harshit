package com.harshit.leetcode.stack;

import java.util.*;

/**
 * Problem: Evaluate Reverse Polish Notation
 * 
 * You are given an array of strings tokens that represents an arithmetic expression in
 * a Reverse Polish Notation.
 * 
 * Evaluate the expression. Return an integer that represents the value of the expression.
 * 
 * Note that:
 * - The valid operators are '+', '-', '*', and '/'.
 * - Each operand may be an integer or another expression.
 * - The division between two integers always truncates toward zero.
 * - There will not be any division by zero.
 * - The input represents a valid arithmetic expression in a reverse polish notation.
 * - The answer and all the intermediate calculations can be represented in a 32-bit integer.
 * 
 * Example 1:
 * Input: tokens = ["2","1","+","3","*"]
 * Output: 9
 * Explanation: ((2 + 1) * 3) = 9
 * 
 * Example 2:
 * Input: tokens = ["4","13","5","/","+"]
 * Output: 6
 * Explanation: (4 + (13 / 5)) = 6
 * 
 * Example 3:
 * Input: tokens = ["10","6","9","3","+","-11","*","/","*","17","+","5","+"]
 * Output: 22
 * Explanation: ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
 * = ((10 * (6 / (12 * -11))) + 17) + 5
 * = ((10 * (6 / -132)) + 17) + 5
 * = ((10 * 0) + 17) + 5
 * = (0 + 17) + 5
 * = 17 + 5
 * = 22
 * 
 * Constraints:
 * - 1 <= tokens.length <= 10^4
 * - tokens[i] is either an operator: "+", "-", "*", or "/", or an integer in the range [-200, 200].
 */
public class EvaluateReversePolishNotation {
    
    /**
     * Solution using Stack (O(n) time, O(n) space)
     * 
     * @param tokens Array of tokens in RPN
     * @return Result of expression evaluation
     */
    public int evalRPN(String[] tokens) {
        // Write your logic here
        // Hint: Use stack to store operands
        // When operator found, pop two operands, perform operation, push result
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        EvaluateReversePolishNotation solution = new EvaluateReversePolishNotation();
        
        // Test case 1
        String[] tokens1 = {"2","1","+","3","*"};
        int result1 = solution.evalRPN(tokens1);
        System.out.println("Test 1 - Input: [\"2\",\"1\",\"+\",\"3\",\"*\"]");
        System.out.println("Expected: 9, Got: " + result1);
        
        // Test case 2
        String[] tokens2 = {"4","13","5","/","+"};
        int result2 = solution.evalRPN(tokens2);
        System.out.println("Test 2 - Input: [\"4\",\"13\",\"5\",\"/\",\"+\"]");
        System.out.println("Expected: 6, Got: " + result2);
    }
}

