package com.harshit.TopInterview150.Arrays_String;

public class RemoveElement {
        private static int removeElement(int[] nums, int val) {
            int count =0;
            for(int i=0;i<nums.length;i++)
            {
                if(nums[i]!=val)
                {
                    nums[count++] = nums[i];
                }
            }
            return count;
            //  int[] filtered = Arrays.stream(nums)
            //                        .filter(n -> n != val)
            //                        .toArray();

            // // Copy filtered elements back into original array
            // // System.arraycopy(filtered, 0, nums, 0, filtered.length);
            // for(int i=0;i<filtered.length;i++){
            //     nums[i]=filtered[i];
            // }
        }
    public static void main(String[] args) {
        int[] nums1 = {0,1,2,2,3,0,4,2};
        int m = 2;


        System.out.println(removeElement(nums1, m));


    }
}
