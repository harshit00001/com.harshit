package com.harshit.leetcode.binarysearch;

/**
 * Problem: Search in Rotated Sorted Array
 * 
 * There is an integer array nums sorted in ascending order (with distinct values).
 * 
 * Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k
 * (1 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1],
 * nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,5,6,7] might be rotated
 * at pivot index 3 and become [4,5,6,7,0,1,2].
 * 
 * Given the array nums after the possible rotation and an integer target, return the index of
 * target if it is in nums, or -1 if it is not in nums.
 * 
 * You must write an algorithm with O(log n) runtime complexity.
 * 
 * Example 1:
 * Input: nums = [4,5,6,7,0,1,2], target = 0
 * Output: 4
 * 
 * Example 2:
 * Input: nums = [4,5,6,7,0,1,2], target = 3
 * Output: -1
 * 
 * Example 3:
 * Input: nums = [1], target = 0
 * Output: -1
 * 
 * Constraints:
 * - 1 <= nums.length <= 5000
 * - -10^4 <= nums[i] <= 10^4
 * - All values of nums are unique.
 * - nums is an ascending array that is possibly rotated.
 * - -10^4 <= target <= 10^4
 */
public class SearchInRotatedSortedArray {
    
    /**
     * Solution using binary search (O(log n) time, O(1) space)
     * 
     * @param nums Rotated sorted array
     * @param target Target value to search
     * @return Index of target, or -1 if not found
     */
    public int search(int[] nums, int target) {
        // Write your logic here
        // Hint: Find which half is sorted
        // Check if target is in sorted half, otherwise search other half
        return -1;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        SearchInRotatedSortedArray solution = new SearchInRotatedSortedArray();
        
        // Test case 1
        int[] nums1 = {4,5,6,7,0,1,2};
        int target1 = 0;
        int result1 = solution.search(nums1, target1);
        System.out.println("Test 1 - Input: [4,5,6,7,0,1,2], target=0");
        System.out.println("Expected: 4, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {4,5,6,7,0,1,2};
        int target2 = 3;
        int result2 = solution.search(nums2, target2);
        System.out.println("Test 2 - Input: [4,5,6,7,0,1,2], target=3");
        System.out.println("Expected: -1, Got: " + result2);
    }
}

