package com.harshit.leetcode.twopointers;

/**
 * Problem: Reverse String
 * 
 * Write a function that reverses a string. The input string is given as an array of characters s.
 * 
 * You must do this by modifying the input array in-place with O(1) extra memory.
 * 
 * Example 1:
 * Input: s = ["h","e","l","l","o"]
 * Output: ["o","l","l","e","h"]
 * 
 * Example 2:
 * Input: s = ["H","a","n","n","a","h"]
 * Output: ["h","a","n","n","a","H"]
 * 
 * Constraints:
 * - 1 <= s.length <= 10^5
 * - s[i] is a printable ascii character.
 */
public class ReverseString {
    
    /**
     * Solution using two pointers (O(n) time, O(1) space)
     * 
     * @param s Array of characters
     */
    public void reverseString(char[] s) {
        // Write your logic here
        // Hint: Use two pointers from start and end
        // Swap characters until pointers meet
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ReverseString solution = new ReverseString();
        
        // Test case 1
        char[] s1 = {'h','e','l','l','o'};
        solution.reverseString(s1);
        System.out.println("Test 1 - Input: [\"h\",\"e\",\"l\",\"l\",\"o\"]");
        System.out.print("Result: [");
        for (int i = 0; i < s1.length; i++) {
            System.out.print("\"" + s1[i] + "\"");
            if (i < s1.length - 1) System.out.print(",");
        }
        System.out.println("]");
        System.out.println("Expected: [\"o\",\"l\",\"l\",\"e\",\"h\"]");
    }
}

