package com.harshit.TopInterview150.Arrays_String;
//Given an integer array nums sorted in non-decreasing order,
// remove some duplicates in-place such that each unique element appears at most twice.
// The relative order of the elements should be kept the same.

public class RemoveDuplicateFromSortedArray {
        private static int removeDuplicates(int[] nums) {
            if(nums.length==0)
                return 0;
            // int[] k = Arrays.stream(nums).dintinct().toArray();
            // return k.length;
            int i=0;
            for(int j=1;j<nums.length;j++)
            {
                if(nums[i]!=nums[j])
                    i++;
                nums[i]=nums[j];
            }
            return i+1;
        }
// public class RemoveDuplicatesFromSortedArray {
//     public static int removeDuplicates(int[] nums) {}
//     public static void main(String[] args) {
//         int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
//         int k = removeDuplicates(nums);
//         System.out.println("Number of unique elements: " + k);
//         System.out.print("Modified array: ");
//         for (int i = 0; i < k; i++) {
//             System.out.print(nums[i] + " ");
//         }}
// }
    public static void main(String[] args) {
        int[] nums1 = {0,0,1,1,1,2,2,3,3,4};


        System.out.println(removeDuplicates(nums1));


    }
}
