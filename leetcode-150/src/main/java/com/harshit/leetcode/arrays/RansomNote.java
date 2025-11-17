package com.harshit.leetcode.arrays;

/**
 * Problem: Ransom Note
 * 
 * Given two strings ransomNote and magazine, return true if ransomNote can be constructed
 * by using the letters from magazine and false otherwise.
 * 
 * Each letter in magazine can only be used once in ransomNote.
 * 
 * Example 1:
 * Input: ransomNote = "a", magazine = "b"
 * Output: false
 * 
 * Example 2:
 * Input: ransomNote = "aa", magazine = "ab"
 * Output: false
 * 
 * Example 3:
 * Input: ransomNote = "aa", magazine = "aab"
 * Output: true
 * 
 * Constraints:
 * - 1 <= ransomNote.length, magazine.length <= 10^5
 * - ransomNote and magazine consist of lowercase English letters.
 */
public class RansomNote {
    
    /**
     * Solution using array counter (O(n+m) time, O(1) space)
     * 
     * @param ransomNote Note to construct
     * @param magazine Available letters
     * @return true if can construct, false otherwise
     */
    public boolean canConstruct(String ransomNote, String magazine) {
        // Write your logic here
        // Hint: Count characters in magazine
        // Decrement count for each character in ransomNote
        // If count goes negative, return false
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        RansomNote solution = new RansomNote();
        
        // Test case 1
        String ransom1 = "a";
        String mag1 = "b";
        boolean result1 = solution.canConstruct(ransom1, mag1);
        System.out.println("Test 1 - ransomNote=\"a\", magazine=\"b\"");
        System.out.println("Expected: false, Got: " + result1);
        
        // Test case 2
        String ransom2 = "aa";
        String mag2 = "ab";
        boolean result2 = solution.canConstruct(ransom2, mag2);
        System.out.println("Test 2 - ransomNote=\"aa\", magazine=\"ab\"");
        System.out.println("Expected: false, Got: " + result2);
        
        // Test case 3
        String ransom3 = "aa";
        String mag3 = "aab";
        boolean result3 = solution.canConstruct(ransom3, mag3);
        System.out.println("Test 3 - ransomNote=\"aa\", magazine=\"aab\"");
        System.out.println("Expected: true, Got: " + result3);
    }
}

