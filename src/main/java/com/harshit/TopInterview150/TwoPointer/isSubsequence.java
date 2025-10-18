package com.harshit.TopInterview150.TwoPointer;
/*
Given two strings s and t, return true if s is a subsequence of t, or false otherwise.
A subsequence of a string is a new string
that is formed from the original string by deleting some (can be none) of the characters without
disturbing the relative positions of the remaining characters.
(i.e., "ace" is a subsequence of "abcde" while "aec" is not).
Input: s = "abc", t = "ahbgdc"
Output: true
 */
public class isSubsequence {
    private static boolean isSubsequence(String s, String t) {
        if(s.length()>t.length())
            return false;
        int left=0, right =0;
        while(left<s.length() && right<t.length())
        {
            if(s.charAt(left)==t.charAt(right++))
            {
                left++;
            }
        }
        if(left==s.length())
            return true;
        else
            return false;
    }
    public static void main(String[] args) {
        String s = "abc", t = "ahbgdc";

        boolean num = isSubsequence(s,t);
        System.out.println(num);
    }
}
