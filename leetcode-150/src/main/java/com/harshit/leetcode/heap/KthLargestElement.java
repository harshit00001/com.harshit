package com.harshit.leetcode.heap;

import java.util.*;

/**
 * Problem: Kth Largest Element in an Array
 * 
 * Given an integer array nums and an integer k, return the kth largest element in the array.
 * 
 * Note that it is the kth largest element in the sorted order, not the kth distinct element.
 * 
 * You must solve it in O(n) time complexity.
 * 
 * Example 1:
 * Input: nums = [3,2,1,5,6,4], k = 2
 * Output: 5
 * 
 * Example 2:
 * Input: nums = [3,2,3,1,2,4,5,5,6], k = 4
 * Output: 4
 * 
 * Constraints:
 * - 1 <= k <= nums.length <= 10^5
 * - -10^4 <= nums[i] <= 10^4
 */
public class KthLargestElement {
    
    /**
     * Solution using QuickSelect (O(n) average, O(n^2) worst, O(1) space)
     * 
     * @param nums Array of integers
     * @param k Kth largest position
     * @return Kth largest element
     */
    public int findKthLargest(int[] nums, int k) {
        // Write your logic here
        // Hint: Use QuickSelect algorithm
        // Partition array, if pivot is at position (n-k), return it
        // Otherwise recurse on appropriate partition
        return 0;
    }
    
    /**
     * Solution using Min Heap (O(n log k) time, O(k) space)
     * 
     * @param nums Array of integers
     * @param k Kth largest position
     * @return Kth largest element
     */
    public int findKthLargestHeap(int[] nums, int k) {
        // Write your logic here
        // Hint: Use min heap of size k
        // Add elements, remove smallest if heap size > k
        // Top of heap is kth largest
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        KthLargestElement solution = new KthLargestElement();
        
        // Test case 1
        int[] nums1 = {3,2,1,5,6,4};
        int k1 = 2;
        int result1 = solution.findKthLargest(nums1, k1);
        System.out.println("Test 1 - Input: [3,2,1,5,6,4], k=2");
        System.out.println("Expected: 5, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {3,2,3,1,2,4,5,5,6};
        int k2 = 4;
        int result2 = solution.findKthLargest(nums2, k2);
        System.out.println("Test 2 - Input: [3,2,3,1,2,4,5,5,6], k=4");
        System.out.println("Expected: 4, Got: " + result2);
    }
}

