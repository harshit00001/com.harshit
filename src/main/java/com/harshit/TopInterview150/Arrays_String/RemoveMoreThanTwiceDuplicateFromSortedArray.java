package com.harshit.TopInterview150.Arrays_String;
//Given an integer array nums sorted in non-decreasing order,
// remove some duplicates in-place such that each unique element appears at most twice.
// The relative order of the elements should be kept the same.

public class RemoveMoreThanTwiceDuplicateFromSortedArray {
        private static int removeDuplicates(int[] nums) {
            int count = 0;
            for (int i = 0; i < nums.length; i++) {
                if (count < 2 || nums[i] != nums[count - 2]) {
                    nums[count++] = nums[i];
                } else
                    continue;
            }
            return count;
        }
    public static void main(String[] args) {
        int[] nums1 = {0,0,1,1,1,1,2,3,3};

        System.out.println(removeDuplicates(nums1));

    }
}
