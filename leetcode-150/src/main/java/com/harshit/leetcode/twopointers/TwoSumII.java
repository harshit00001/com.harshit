package com.harshit.leetcode.twopointers;

/**
 * Problem: Two Sum II - Input Array Is Sorted
 * 
 * Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order,
 * find two numbers such that they add up to a specific target number. Let these two numbers
 * be numbers[index1] and numbers[index2] where 1 <= index1 < index2 <= numbers.length.
 * 
 * Return the indices of the two numbers, index1 and index2, added by one as an integer array
 * [index1, index2] of length 2.
 * 
 * The tests are generated such that there is exactly one solution. You may not use the same
 * element twice.
 * 
 * Your solution must use only constant extra space.
 * 
 * Example 1:
 * Input: numbers = [2,7,11,15], target = 9
 * Output: [1,2]
 * Explanation: The sum of 2 and 7 is 9. Therefore, index1 = 1, index2 = 2. We return [1, 2].
 * 
 * Example 2:
 * Input: numbers = [2,3,4], target = 6
 * Output: [1,3]
 * Explanation: The sum of 2 and 4 is 6. Therefore index1 = 1, index2 = 3. We return [1, 3].
 * 
 * Example 3:
 * Input: numbers = [-1,0], target = -1
 * Output: [1,2]
 * Explanation: The sum of -1 and 0 is -1. Therefore index1 = 1, index2 = 2. We return [1, 2].
 * 
 * Constraints:
 * - 2 <= numbers.length <= 3 * 10^4
 * - -1000 <= numbers[i] <= 1000
 * - numbers is sorted in non-decreasing order.
 * - -1000 <= target <= 1000
 * - The tests are generated such that there is exactly one solution.
 */
public class TwoSumII {
    
    /**
     * Solution using two pointers (O(n) time, O(1) space)
     * 
     * @param numbers Sorted array of integers
     * @param target Target sum
     * @return Array containing 1-indexed positions of two numbers
     */
    public int[] twoSum(int[] numbers, int target) {
        // Write your logic here
        // Hint: Use two pointers at start and end
        // If sum > target, move right pointer left
        // If sum < target, move left pointer right
        return new int[0];
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        TwoSumII solution = new TwoSumII();
        
        // Test case 1
        int[] numbers1 = {2, 7, 11, 15};
        int target1 = 9;
        int[] result1 = solution.twoSum(numbers1, target1);
        System.out.println("Test 1 - Input: [2,7,11,15], Target: 9");
        System.out.println("Expected: [1,2], Got: [" + result1[0] + "," + result1[1] + "]");
        
        // Test case 2
        int[] numbers2 = {2, 3, 4};
        int target2 = 6;
        int[] result2 = solution.twoSum(numbers2, target2);
        System.out.println("Test 2 - Input: [2,3,4], Target: 6");
        System.out.println("Expected: [1,3], Got: [" + result2[0] + "," + result2[1] + "]");
    }
}

