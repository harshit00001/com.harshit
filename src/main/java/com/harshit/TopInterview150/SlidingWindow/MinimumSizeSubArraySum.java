package com.harshit.TopInterview150.SlidingWindow;
/*
Given an array of positive integers nums and a positive integer target,
return the minimal length of a subarray whose sum is greater than or equal to target.
If there is no such subarray, return 0 instead.

Example 1:
Input: target = 7, nums = [2,3,1,2,4,3]
Output: 2
Explanation: The subarray [4,3] has the minimal length under the problem constraint.
 */
public class MinimumSizeSubArraySum {
    public static int minSubArrayLen(int target, int[] nums) {
        int left=0, right=0, minLength=Integer.MAX_VALUE,curr_sum=0;
        for(right=0; right<=nums.length-1;right++)
        {
            curr_sum+=nums[right];
            while(curr_sum>=target)
            {
                int currwindow=right-left+1;
                minLength=Math.min(minLength,currwindow);
                curr_sum-=nums[left++];
            }
        }
        return minLength==Integer.MAX_VALUE?0:minLength;
    }
    public static void main(String[] args) {
        int[] num= {2,3,1,2,4,3};
        int target =7;

        int num2 = minSubArrayLen(target,num);
        System.out.println(num2);
    }


}
