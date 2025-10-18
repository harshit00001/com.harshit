package com.harshit.TopInterview150.TwoPointer;

public class IsPalindrome {
    private static boolean isPalindrome(String s) {
        s=s.toLowerCase().trim();
        int left=0,right=s.length()-1;
        if(s.isEmpty())
        {
            return true;
        }
        while(left<right)
        {
            while(left<right && !Character.isLetterOrDigit(s.charAt(left))) left++;
            while(left<right && !Character.isLetterOrDigit(s.charAt(right))) right--;
            if(s.charAt(left++)!=s.charAt(right--))
                return false;
        }
        return true;
    }
    public static void main(String[] args) {
        String str="A man, a plan, a canal: Panama";

        boolean num = isPalindrome(str);
        System.out.println(num);
    }
}
