package com.harshit.leetcode.arrays;

import java.util.*;

/**
 * Problem: Encode and Decode Strings
 * 
 * Design an algorithm to encode a list of strings to a string. The encoded string is then
 * sent over the network and is decoded back to the original list of strings.
 * 
 * Implement the encode and decode methods.
 * 
 * Example 1:
 * Input: ["neet","code","love","you"]
 * Output: ["neet","code","love","you"]
 * 
 * Example 2:
 * Input: ["we","say",":","yes"]
 * Output: ["we","say",":","yes"]
 * 
 * Constraints:
 * - 1 <= strs.length <= 200
 * - 0 <= strs[i].length <= 200
 * - strs[i] contains any possible characters out of 256 valid ascii characters.
 */
public class EncodeAndDecodeStrings {
    
    /**
     * Encodes a list of strings to a single string.
     * 
     * @param strs List of strings to encode
     * @return Encoded string
     */
    public String encode(List<String> strs) {
        // Write your logic here
        // Hint: Use delimiter or length prefix approach
        // Format: length#string (e.g., "4#neet4#code")
        return "";
    }
    
    /**
     * Decodes a single string to a list of strings.
     * 
     * @param s Encoded string
     * @return List of decoded strings
     */
    public List<String> decode(String s) {
        // Write your logic here
        // Hint: Parse length prefix and extract strings
        return new ArrayList<>();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        EncodeAndDecodeStrings codec = new EncodeAndDecodeStrings();
        
        // Test case 1
        List<String> strs1 = Arrays.asList("neet","code","love","you");
        String encoded1 = codec.encode(strs1);
        List<String> decoded1 = codec.decode(encoded1);
        System.out.println("Test 1 - Input: [\"neet\",\"code\",\"love\",\"you\"]");
        System.out.println("Encoded: " + encoded1);
        System.out.println("Decoded: " + decoded1);
        
        // Test case 2
        List<String> strs2 = Arrays.asList("we","say",":","yes");
        String encoded2 = codec.encode(strs2);
        List<String> decoded2 = codec.decode(encoded2);
        System.out.println("Test 2 - Input: [\"we\",\"say\",\":\",\"yes\"]");
        System.out.println("Encoded: " + encoded2);
        System.out.println("Decoded: " + decoded2);
    }
}

