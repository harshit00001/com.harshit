package com.harshit.dsa.strings;

import java.util.HashMap;
import java.util.Map;

/**
 * ===================================================================
 * LONGEST SUBSTRING WITHOUT REPEATING CHARACTERS
 * ===================================================================
 * 
 * PROBLEM STATEMENT:
 * Given a string s, find the length of the longest substring
 * without repeating characters.
 * 
 * ===================================================================
 * EXAMPLES:
 * ===================================================================
 * 
 * Example 1:
 * Input: s = "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 * 
 * Step-by-step:
 * a b c a b c b b
 * ^     ^
 * start end
 * 
 * Window: "abc" (length 3) ✓
 * 
 * Example 2:
 * Input: s = "bbbbb"
 * Output: 1
 * Explanation: The answer is "b", with the length of 1.
 * 
 * Example 3:
 * Input: s = "pwwkew"
 * Output: 3
 * Explanation: The answer is "wke", with the length of 3.
 * Notice that the answer must be a substring, "pwke" is a subsequence.
 * 
 * ===================================================================
 * APPROACH: SLIDING WINDOW TECHNIQUE
 * ===================================================================
 * 
 * KEY IDEA:
 * Use a sliding window [start, end] to represent the current substring
 * without repeating characters.
 * 
 * ALGORITHM:
 * 1. Use two pointers: start and end
 * 2. Use a map to store: character -> last index where we saw it
 * 3. Expand window by moving end pointer:
 *    - If current character is new or outside window: expand window
 *    - If current character repeats within window: shrink window
 * 4. Track maximum window size
 * 
 * HOW IT WORKS:
 * 
 * For "abcabcbb":
 * 
 * end=0, char='a': map={a:0}, window="a", length=1
 * end=1, char='b': map={a:0, b:1}, window="ab", length=2
 * end=2, char='c': map={a:0, b:1, c:2}, window="abc", length=3
 * end=3, char='a': 'a' seen at index 0, but 0 < start(0)? No, it's equal
 *                  Actually, we need to check: is index 0 >= start? Yes
 *                  So move start to 0+1=1, window="bca", length=3
 * end=4, char='b': 'b' seen at index 1, 1 >= start(1)? Yes
 *                  Move start to 1+1=2, window="cab", length=3
 * end=5, char='c': 'c' seen at index 2, 2 >= start(2)? Yes
 *                  Move start to 2+1=3, window="abc", length=3
 * end=6, char='b': 'b' seen at index 4, 4 >= start(3)? Yes
 *                  Move start to 4+1=5, window="cb", length=2
 * end=7, char='b': 'b' seen at index 6, 6 >= start(5)? Yes
 *                  Move start to 6+1=7, window="b", length=1
 * 
 * Maximum length: 3
 * 
 * ===================================================================
 * TIME & SPACE COMPLEXITY:
 * ===================================================================
 * 
 * Time Complexity: O(n)
 *   - Each character is visited at most twice (once by end, once by start)
 *   - HashMap operations are O(1) on average
 * 
 * Space Complexity: O(min(n, m))
 *   - m = size of character set (e.g., 26 for lowercase letters, 128 for ASCII)
 *   - HashMap stores at most min(n, m) characters
 * 
 * ===================================================================
 * OPTIMIZATION: Using Array Instead of HashMap
 * ===================================================================
 * 
 * If we know the character set (e.g., ASCII), we can use an array
 * instead of HashMap for better performance:
 * 
 * int[] lastIndex = new int[128]; // For ASCII
 * 
 * This is faster because:
 * - Array access is O(1) guaranteed (no hash computation)
 * - Less memory overhead
 * 
 * ===================================================================
 */
public class LongestSubstringWithoutRepeating {
    
    /**
     * SLIDING WINDOW WITH HASHMAP
     * 
     * This approach works for any character set (Unicode, etc.)
     * 
     * @param s Input string
     * @return Length of longest substring without repeating characters
     */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        // Map: character -> last index where we saw it
        Map<Character, Integer> map = new HashMap<>();
        int maxLength = 0;
        int start = 0; // Start of current window
        
        // Expand window by moving end pointer
        for (int end = 0; end < s.length(); end++) {
            char currentChar = s.charAt(end);
            
            // If we've seen this character before AND it's within our current window
            // (i.e., its last occurrence is >= start), we need to shrink the window
            if (map.containsKey(currentChar) && map.get(currentChar) >= start) {
                // Move start to just after the last occurrence of current character
                start = map.get(currentChar) + 1;
            }
            
            // Update/Add current character's last seen index
            map.put(currentChar, end);
            
            // Update maximum length
            // Current window size = end - start + 1
            maxLength = Math.max(maxLength, end - start + 1);
        }
        
        return maxLength;
    }
    
    /**
     * Alternative: Using array for ASCII characters (faster)
     */
    public static int lengthOfLongestSubstringArray(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int[] lastIndex = new int[128]; // ASCII characters
        int maxLength = 0;
        int start = 0;
        
        for (int end = 0; end < s.length(); end++) {
            char currentChar = s.charAt(end);
            start = Math.max(start, lastIndex[currentChar]);
            maxLength = Math.max(maxLength, end - start + 1);
            lastIndex[currentChar] = end + 1;
        }
        
        return maxLength;
    }
    
    public static void main(String[] args) {
        String[] testCases = {
            "abcabcbb",
            "bbbbb",
            "pwwkew",
            "dvdf"
        };
        
        for (String s : testCases) {
            System.out.println("String: \"" + s + "\"");
            System.out.println("Longest substring length: " + lengthOfLongestSubstring(s));
            System.out.println();
        }
    }
}

