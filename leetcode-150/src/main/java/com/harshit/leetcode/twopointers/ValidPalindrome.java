package com.harshit.leetcode.twopointers;

/**
 * Problem: Valid Palindrome
 * 
 * A phrase is a palindrome if, after converting all uppercase letters into lowercase letters
 * and removing all non-alphanumeric characters, it reads the same forward and backward.
 * 
 * Given a string s, return true if it is a palindrome, or false otherwise.
 * 
 * Example 1:
 * Input: s = "A man, a plan, a canal: Panama"
 * Output: true
 * Explanation: "amanaplanacanalpanama" is a palindrome.
 * 
 * Example 2:
 * Input: s = "race a car"
 * Output: false
 * Explanation: "raceacar" is not a palindrome.
 * 
 * Example 3:
 * Input: s = " "
 * Output: true
 * Explanation: s is an empty string "" after removing non-alphanumeric characters.
 * Since an empty string reads the same forward and backward, it is a palindrome.
 * 
 * Constraints:
 * - 1 <= s.length <= 2 * 10^5
 * - s consists only of printable ASCII characters.
 */
public class ValidPalindrome {
    
    /**
     * Solution using two pointers (O(n) time, O(1) space)
     * 
     * @param s Input string
     * @return true if palindrome, false otherwise
     */
    public boolean isPalindrome(String s) {
        // Write your logic here
        // Hint: Use two pointers from start and end
        // Skip non-alphanumeric characters
        // Compare characters (case-insensitive)
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ValidPalindrome solution = new ValidPalindrome();
        
        // Test case 1
        String s1 = "A man, a plan, a canal: Panama";
        boolean result1 = solution.isPalindrome(s1);
        System.out.println("Test 1 - Input: \"A man, a plan, a canal: Panama\"");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        String s2 = "race a car";
        boolean result2 = solution.isPalindrome(s2);
        System.out.println("Test 2 - Input: \"race a car\"");
        System.out.println("Expected: false, Got: " + result2);
        
        // Test case 3
        String s3 = " ";
        boolean result3 = solution.isPalindrome(s3);
        System.out.println("Test 3 - Input: \" \"");
        System.out.println("Expected: true, Got: " + result3);
    }
}

