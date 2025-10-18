package com.harshit.TopInterview150.Arrays_String;
/*
Given a string s consisting of words and spaces,
return the length of the last word in the string.
A word is a maximal substring consisting of non-space characters only.
 */
public class LengthOfLastWord {
    private static int lengthOfLastWord(String s) {
        String str= s.trim();
        int count=0;
        for(int i=str.length()-1;i>=0;i--)
        {
            if(str.charAt(i) == ' ')
            {
                break;
            }
            else
                count++;
        }
        return count;
    }
    public static void main(String[] args) {
        String str="luffy is still joyboy";

        int num = lengthOfLastWord(str);
        System.out.println(num);
    }
}
