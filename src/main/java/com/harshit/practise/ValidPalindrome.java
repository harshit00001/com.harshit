package com.harshit.practise;

public class ValidPalindrome {
    public static boolean isPalindrome(String s) {
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
        String s="A man, a plan, a canal: Panama";
        if(isPalindrome(s))
        System.out.println("this is a palindrome");
        else
            System.out.println("this is not a palindrome");
    }
}
