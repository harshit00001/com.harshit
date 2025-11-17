package com.harshit.leetcode.arrays;

import java.util.*;

/**
 * Problem: Top K Frequent Elements
 * 
 * Given an integer array nums and an integer k, return the k most frequent elements.
 * You may return the answer in any order.
 * 
 * Example 1:
 * Input: nums = [1,1,1,2,2,3], k = 2
 * Output: [1,2]
 * 
 * Example 2:
 * Input: nums = [1], k = 1
 * Output: [1]
 * 
 * Constraints:
 * - 1 <= nums.length <= 10^5
 * - -10^4 <= nums[i] <= 10^4
 * - k is in the range [1, the number of unique elements in the array].
 * - It is guaranteed that the answer is unique.
 * 
 * Follow up: Your algorithm's time complexity must be better than O(n log n), where n is the array's size.
 */
public class TopKFrequentElements {
    
    /**
     * Solution using HashMap and PriorityQueue (O(n log k) time, O(n) space)
     * 
     * @param nums Array of integers
     * @param k Number of most frequent elements to return
     * @return Array of k most frequent elements
     */
    public int[] topKFrequent(int[] nums, int k) {
        // Write your logic here
        // Hint: Count frequencies, use min heap of size k
        // Add to heap, remove smallest if heap size > k
        return new int[0];
    }
    
    /**
     * Solution using Bucket Sort (O(n) time, O(n) space)
     * 
     * @param nums Array of integers
     * @param k Number of most frequent elements to return
     * @return Array of k most frequent elements
     */
    public int[] topKFrequentBucket(int[] nums, int k) {
        // Write your logic here
        // Hint: Use array of lists indexed by frequency
        // Traverse from highest frequency to get top k
        return new int[0];
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        TopKFrequentElements solution = new TopKFrequentElements();
        
        // Test case 1
        int[] nums1 = {1,1,1,2,2,3};
        int k1 = 2;
        int[] result1 = solution.topKFrequent(nums1, k1);
        System.out.println("Test 1 - Input: [1,1,1,2,2,3], k=2");
        System.out.print("Expected: [1,2], Got: [");
        for (int i = 0; i < result1.length; i++) {
            System.out.print(result1[i]);
            if (i < result1.length - 1) System.out.print(",");
        }
        System.out.println("]");
        
        // Test case 2
        int[] nums2 = {1};
        int k2 = 1;
        int[] result2 = solution.topKFrequent(nums2, k2);
        System.out.println("Test 2 - Input: [1], k=1");
        System.out.println("Expected: [1], Got: [" + result2[0] + "]");
    }
}

