package com.harshit.leetcode.twopointers;

/**
 * Problem: Reverse Words in a String
 * 
 * Given an input string s, reverse the order of the words.
 * 
 * A word is defined as a sequence of non-space characters. The words in s will be separated
 * by at least one space.
 * 
 * Return a string of the words in reverse order concatenated by a single space.
 * 
 * Note that s may contain leading or trailing spaces or multiple spaces between two words.
 * The returned string should only have a single space separating the words. Do not include
 * any extra spaces.
 * 
 * Example 1:
 * Input: s = "the sky is blue"
 * Output: "blue is sky the"
 * 
 * Example 2:
 * Input: s = "  hello world  "
 * Output: "world hello"
 * Explanation: Your reversed string should not contain leading or trailing spaces.
 * 
 * Example 3:
 * Input: s = "a good   example"
 * Output: "example good a"
 * Explanation: You need to reduce multiple spaces between two words to a single space in the reversed string.
 * 
 * Constraints:
 * - 1 <= s.length <= 10^4
 * - s contains English letters (upper-case and lower-case), digits, and spaces ' '.
 * - There is at least one word in s.
 * 
 * Follow-up: If the string data type is mutable in your language, can you solve it in-place with O(1) extra space?
 */
public class ReverseWordsInString {
    
    /**
     * Solution using built-in methods (O(n) time, O(n) space)
     * 
     * @param s Input string
     * @return String with words reversed
     */
    public String reverseWords(String s) {
        // Write your logic here
        // Hint: Split by spaces, reverse array, join with single space
        // Handle multiple spaces and trim
        return "";
    }
    
    /**
     * Solution using two pointers (O(n) time, O(n) space)
     * 
     * @param s Input string
     * @return String with words reversed
     */
    public String reverseWordsTwoPointers(String s) {
        // Write your logic here
        // Hint: Reverse entire string first
        // Then reverse each word
        // Trim extra spaces
        return "";
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ReverseWordsInString solution = new ReverseWordsInString();
        
        // Test case 1
        String s1 = "the sky is blue";
        String result1 = solution.reverseWords(s1);
        System.out.println("Test 1 - Input: \"the sky is blue\"");
        System.out.println("Result: \"" + result1 + "\"");
        System.out.println("Expected: \"blue is sky the\"");
        
        // Test case 2
        String s2 = "  hello world  ";
        String result2 = solution.reverseWords(s2);
        System.out.println("Test 2 - Input: \"  hello world  \"");
        System.out.println("Result: \"" + result2 + "\"");
        System.out.println("Expected: \"world hello\"");
    }
}

