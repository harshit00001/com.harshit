package com.harshit.leetcode.greedy;

/**
 * Problem: Jump Game
 * 
 * You are given an integer array nums. You are initially positioned at the array's first index,
 * and each element in the array represents your maximum jump length at that position.
 * 
 * Return true if you can reach the last index, or false otherwise.
 * 
 * Example 1:
 * Input: nums = [2,3,1,1,4]
 * Output: true
 * Explanation: Jump 1 step from index 0 to 1, then 3 steps from index 1 to the last index.
 * 
 * Example 2:
 * Input: nums = [3,2,1,0,4]
 * Output: false
 * Explanation: You will always arrive at index 3, but its maximum jump length is 0, which makes
 * it impossible to reach the last index.
 * 
 * Constraints:
 * - 1 <= nums.length <= 10^4
 * - 0 <= nums[i] <= 10^5
 */
public class JumpGame {
    
    /**
     * Solution using greedy (O(n) time, O(1) space)
     * 
     * @param nums Array of jump lengths
     * @return true if can reach last index, false otherwise
     */
    public boolean canJump(int[] nums) {
        // Write your logic here
        // Hint: Track farthest reachable position
        // If current index > farthest, return false
        // Update farthest = max(farthest, i + nums[i])
        return false;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        JumpGame solution = new JumpGame();
        
        // Test case 1
        int[] nums1 = {2,3,1,1,4};
        boolean result1 = solution.canJump(nums1);
        System.out.println("Test 1 - Input: [2,3,1,1,4]");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        int[] nums2 = {3,2,1,0,4};
        boolean result2 = solution.canJump(nums2);
        System.out.println("Test 2 - Input: [3,2,1,0,4]");
        System.out.println("Expected: false, Got: " + result2);
    }
}

