package com.harshit.TopInterview150.Arrays_String;

import java.util.Arrays;

public class RotateArray {
    private static int[] rotateArray(int[] nums, int k) {
        k = k % nums.length;
        reverse(0,nums.length-1,nums);
        reverse(0,k-1,nums);
        reverse(k,nums.length-1,nums);
        return nums;
    }
    private static void reverse(int start,int end,int[] nums)
    {
        int temp=0;
        while(start<end)
        {
            temp=nums[start];
            nums[start]=nums[end];
            nums[end]=temp;
            start++;
            end--;
        }
    }
    public static void main(String[] args) {
        int[] nums1 = {-1,-100,3,99};
        int k =2;

        int[] num2 = rotateArray(nums1,2);
        System.out.println(Arrays.toString(num2));
    }
}
