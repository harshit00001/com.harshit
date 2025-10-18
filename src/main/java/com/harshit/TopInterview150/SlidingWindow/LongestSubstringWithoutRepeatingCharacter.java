package com.harshit.TopInterview150.SlidingWindow;

import java.util.ArrayList;
import java.util.List;

public class LongestSubstringWithoutRepeatingCharacter {
    public static int lengthOfLongestSubstring(String s) {
        int left=0,right=0,maxLength=Integer.MIN_VALUE;
        List<Character> str = new ArrayList<>();
        while(right<s.length())
        {
            if(!str.contains(s.charAt(right)))
            {
                str.add(s.charAt(right++));
                maxLength= Math.max(maxLength,str.size());
            }
            else
            {
                str.remove(Character.valueOf(s.charAt(left++)));
            }
        }
        return maxLength==Integer.MIN_VALUE?0:maxLength;
    }
    public static void main(String[] args) {
        String str = "abcabcbb";
        int num2 = lengthOfLongestSubstring(str);
        System.out.println(num2);
    }
}
