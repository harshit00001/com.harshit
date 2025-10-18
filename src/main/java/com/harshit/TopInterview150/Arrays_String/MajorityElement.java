package com.harshit.TopInterview150.Arrays_String;

//        Given an array nums of size n, return the majority element.
//        The majority element is the element that appears more than ⌊n / 2⌋ times.
//        You may assume that the majority element always exists in the array.

public class MajorityElement {
        private static int majorityElement(int[] nums) {
            int count=0, cand=0;
            for(int i : nums)
            {
                if(count==0)
                {
                    cand=i;
                }
                if(cand==i)
                {
                    count++;
                }
                else
                {
                    count--;
                }
            }
            return cand;
        }
    public static void main(String[] args) {
        int[] nums1 = {2,2,1,1,1,2,2};


        System.out.println(majorityElement(nums1));


    }
}
