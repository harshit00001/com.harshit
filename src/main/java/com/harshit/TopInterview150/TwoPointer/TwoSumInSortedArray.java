package com.harshit.TopInterview150.TwoPointer;

import java.util.Arrays;

public class TwoSumInSortedArray {
    public static int[] twoSum(int[] numbers, int target) {
        int left=0, right = numbers.length-1;
        while(left<right)
        {
            int sum = numbers[left]+numbers[right];
            if(sum==target)
                return new int[]{left+1,right+1};
            else if(sum>target)
                right--;
            else
                left++;
        }
        return new int[]{-1,-1};
    }
    public static void main(String[] args) {
        int[] num= {2,7,11,15};
        int target =9;

        int[] num2 = twoSum(num,target);
        System.out.println(Arrays.toString(num2));
    }
}
