package com.harshit.TopInterview150.Arrays_String;

public class ReverseWordOfAString {
    private static String reverseWords(String s) {
        // List<String> str = Arrays.asList(s.trim().split("\\s+"));
        // Collections.reverse(str);
        // return String.join(" ",str);
        StringBuilder str = new StringBuilder();
        String[] str2 =  s.trim().split("\\s+");
        for(int i=str2.length-1;i>=0;i--)
        {
            str.append(str2[i]);
            if(i!=0)
            {
                str.append(" ");
            }
        }
        return str.toString();
    }
    public static void main(String[] args) {
        String str="luffy is still joyboy";

        String num = reverseWords(str);
        System.out.println(num);
    }
}
