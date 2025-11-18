package com.harshit.dsa.greedy;

/**
 * ===================================================================
 * JUMP GAME - Classic Greedy Algorithm Problem
 * ===================================================================
 * 
 * PROBLEM STATEMENT:
 * You are given an integer array nums. You are initially positioned
 * at the array's first index, and each element in the array represents
 * your maximum jump length at that position.
 * 
 * Return true if you can reach the last index, or false otherwise.
 * 
 * ===================================================================
 * EXAMPLES:
 * ===================================================================
 * 
 * Example 1:
 * Input: nums = [2, 3, 1, 1, 4]
 * Output: true
 * 
 * Explanation:
 * Jump 1 step from index 0 to 1, then 3 steps from index 1 to the last index.
 * 
 * Visualization:
 * Index:  0  1  2  3  4
 * Value:  2  3  1  1  4
 *         ^
 *         Start here
 * 
 * From index 0, we can jump up to 2 steps (to index 1 or 2)
 * Let's jump to index 1 (value 3)
 * 
 * From index 1, we can jump up to 3 steps (to index 2, 3, or 4)
 * Let's jump to index 4 (the last index) ✓
 * 
 * Example 2:
 * Input: nums = [3, 2, 1, 0, 4]
 * Output: false
 * 
 * Explanation:
 * You will always arrive at index 3, but its maximum jump length is 0,
 * which makes it impossible to reach the last index.
 * 
 * Visualization:
 * Index:  0  1  2  3  4
 * Value:  3  2  1  0  4
 *         ^
 *         Start here
 * 
 * From index 0, we can jump to index 1, 2, or 3
 * - If we jump to index 1: can reach index 2 or 3
 * - If we jump to index 2: can reach index 3
 * - If we jump to index 3: stuck! (value is 0)
 * 
 * No matter what, we get stuck at index 3 ✗
 * 
 * ===================================================================
 * INTUITION & APPROACH:
 * ===================================================================
 * 
 * GREEDY APPROACH:
 * Instead of trying all possible paths (which would be exponential),
 * we use a greedy strategy:
 * 
 * KEY INSIGHT:
 * At each position, we track the farthest position we can reach.
 * If we can reach a position, we can reach all positions before it.
 * 
 * ALGORITHM:
 * 1. Initialize farthest = 0 (farthest position we can reach)
 * 2. For each index i from 0 to n-1:
 *    a. If i > farthest: We can't reach position i, return false
 *    b. Update farthest = max(farthest, i + nums[i])
 * 3. If we process all indices, return true
 * 
 * ===================================================================
 * STEP-BY-STEP EXAMPLE:
 * ===================================================================
 * 
 * nums = [2, 3, 1, 1, 4]
 * 
 * i=0: nums[0] = 2
 *      Can we reach index 0? Yes (we start here)
 *      farthest = max(0, 0 + 2) = max(0, 2) = 2
 *      (We can reach up to index 2)
 * 
 * i=1: nums[1] = 3
 *      Can we reach index 1? Yes (1 <= 2)
 *      farthest = max(2, 1 + 3) = max(2, 4) = 4
 *      (We can reach up to index 4, which is the last index!)
 * 
 * i=2: nums[2] = 1
 *      Can we reach index 2? Yes (2 <= 4)
 *      farthest = max(4, 2 + 1) = max(4, 3) = 4
 *      (Still can reach up to index 4)
 * 
 * i=3: nums[3] = 1
 *      Can we reach index 3? Yes (3 <= 4)
 *      farthest = max(4, 3 + 1) = max(4, 4) = 4
 * 
 * i=4: nums[4] = 4
 *      Can we reach index 4? Yes (4 <= 4)
 *      We've reached the last index! ✓
 * 
 * Return: true
 * 
 * ===================================================================
 * WHY GREEDY WORKS:
 * ===================================================================
 * 
 * The greedy approach works because:
 * 
 * 1. If we can reach position i, we can reach all positions before i
 * 2. We only care about the farthest position we can reach, not
 *    the exact path
 * 3. If at any point we can't reach the current position, we can't
 *    reach the end either
 * 
 * ===================================================================
 * TIME & SPACE COMPLEXITY:
 * ===================================================================
 * 
 * Time Complexity: O(n)
 *   - Single pass through the array
 *   - Constant work at each step
 * 
 * Space Complexity: O(1)
 *   - Only using a few variables
 * 
 * ===================================================================
 */
public class JumpGame {
    
    /**
     * GREEDY SOLUTION
     * 
     * This is the optimal solution for interviews!
     * 
     * @param nums Array where nums[i] is maximum jump length from index i
     * @return true if we can reach the last index, false otherwise
     */
    public static boolean canJump(int[] nums) {
        if (nums == null || nums.length == 0) {
            return false;
        }
        
        // Track the farthest position we can reach
        int farthest = 0;
        
        // Check each position
        for (int i = 0; i < nums.length; i++) {
            // If current position is beyond what we can reach, return false
            if (i > farthest) {
                return false;
            }
            
            // Update farthest position we can reach
            // From position i, we can reach up to i + nums[i]
            farthest = Math.max(farthest, i + nums[i]);
            
            // Early exit: if we can already reach the last index
            if (farthest >= nums.length - 1) {
                return true;
            }
        }
        
        // We've processed all positions and can reach the end
        return true;
    }
    
    /**
     * ALTERNATIVE: More explicit version
     * 
     * This version is easier to understand for beginners.
     */
    public static boolean canJumpAlternative(int[] nums) {
        if (nums == null || nums.length == 0) {
            return false;
        }
        
        int n = nums.length;
        int farthest = 0; // Farthest index we can reach
        
        for (int i = 0; i < n; i++) {
            // If we can't reach current index, we can't go further
            if (i > farthest) {
                return false;
            }
            
            // From current index i, we can jump up to nums[i] steps
            // So we can reach index i + nums[i]
            int reachableFromI = i + nums[i];
            
            // Update farthest if we can reach further
            farthest = Math.max(farthest, reachableFromI);
        }
        
        // Check if we can reach the last index
        return farthest >= n - 1;
    }
    
    public static void main(String[] args) {
        // Test case 1: Can reach end
        int[] nums1 = {2, 3, 1, 1, 4};
        System.out.println("Test 1 - Input: " + java.util.Arrays.toString(nums1));
        System.out.println("Can jump to end: " + canJump(nums1));
        System.out.println("Expected: true\n");
        
        // Test case 2: Cannot reach end
        int[] nums2 = {3, 2, 1, 0, 4};
        System.out.println("Test 2 - Input: " + java.util.Arrays.toString(nums2));
        System.out.println("Can jump to end: " + canJump(nums2));
        System.out.println("Expected: false\n");
        
        // Test case 3: Single element
        int[] nums3 = {0};
        System.out.println("Test 3 - Input: " + java.util.Arrays.toString(nums3));
        System.out.println("Can jump to end: " + canJump(nums3));
        System.out.println("Expected: true\n");
        
        // Test case 4: Can jump from start
        int[] nums4 = {5, 0, 0, 0, 0};
        System.out.println("Test 4 - Input: " + java.util.Arrays.toString(nums4));
        System.out.println("Can jump to end: " + canJump(nums4));
        System.out.println("Expected: true");
    }
}

