package com.harshit.leetcode.binarysearch;

/**
 * Problem: Binary Search
 * 
 * Given an array of integers nums which is sorted in ascending order, and an integer target,
 * write a function to search target in nums. If target exists, then return its index. Otherwise, return -1.
 * 
 * You must write an algorithm with O(log n) runtime complexity.
 * 
 * Example 1:
 * Input: nums = [-1,0,3,5,9,12], target = 9
 * Output: 4
 * Explanation: 9 exists in nums and its index is 4
 * 
 * Example 2:
 * Input: nums = [-1,0,3,5,9,12], target = 2
 * Output: -1
 * Explanation: 2 does not exist in nums so return -1
 * 
 * Constraints:
 * - 1 <= nums.length <= 10^4
 * - -10^4 < nums[i], target < 10^4
 * - All the integers in nums are unique.
 * - nums is sorted in ascending order.
 */
public class BinarySearch {
    
    /**
     * Iterative solution (O(log n) time, O(1) space)
     * 
     * @param nums Sorted array of integers
     * @param target Target value to search
     * @return Index of target, or -1 if not found
     */
    public int search(int[] nums, int target) {
        // Write your logic here
        // Hint: Use two pointers (left, right)
        // Calculate mid = (left + right) / 2
        // If nums[mid] == target, return mid
        // If nums[mid] < target, search right half
        // If nums[mid] > target, search left half
        return -1;
    }
    
    /**
     * Recursive solution (O(log n) time, O(log n) space)
     * 
     * @param nums Sorted array of integers
     * @param target Target value to search
     * @return Index of target, or -1 if not found
     */
    public int searchRecursive(int[] nums, int target) {
        // Write your logic here
        // Hint: Recursively search left or right half
        return searchHelper(nums, target, 0, nums.length - 1);
    }
    
    private int searchHelper(int[] nums, int target, int left, int right) {
        // Write your logic here
        return -1;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        BinarySearch solution = new BinarySearch();
        
        // Test case 1
        int[] nums1 = {-1,0,3,5,9,12};
        int target1 = 9;
        int result1 = solution.search(nums1, target1);
        System.out.println("Test 1 - Input: [-1,0,3,5,9,12], target=9");
        System.out.println("Expected: 4, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {-1,0,3,5,9,12};
        int target2 = 2;
        int result2 = solution.search(nums2, target2);
        System.out.println("Test 2 - Input: [-1,0,3,5,9,12], target=2");
        System.out.println("Expected: -1, Got: " + result2);
    }
}

