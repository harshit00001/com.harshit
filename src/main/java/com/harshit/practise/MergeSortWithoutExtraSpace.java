package com.harshit.practise;

public class MergeSortWithoutExtraSpace {
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;         // Last valid element in nums1
        int j = n - 1;         // Last element in nums2
        int k = m + n - 1;     // End of nums1 array

        // Merge from the back
        while (i >= 0 && j >= 0) {
            nums1[k--] = (nums1[i] > nums2[j]) ? nums1[i--] : nums2[j--];
        }

        // If nums2 still has elements, copy them
        while (j >= 0) {
            nums1[k--] = nums2[j--];
        }
    }

    public static void main(String[] args) {

        int[] nums1 = {1, 3, 5, 0, 0, 0}; // m = 3
        int[] nums2 = {2, 4, 6};          // n = 3

        merge(nums1, 3, nums2, 3);

        System.out.print("Merged array: ");
        for (int num : nums1) {
            System.out.print(num + " ");
        }
    }
}
