package com.harshit.dsa.arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * ===================================================================
 * TWO SUM - The Most Frequently Asked Interview Question!
 * ===================================================================
 * 
 * PROBLEM STATEMENT:
 * Given an array of integers 'nums' and an integer 'target',
 * return indices of the two numbers such that they add up to target.
 * 
 * You may assume that each input would have exactly one solution,
 * and you may not use the same element twice.
 * 
 * You can return the answer in any order.
 * 
 * ===================================================================
 * EXAMPLES:
 * ===================================================================
 * 
 * Example 1:
 * Input: nums = [2, 7, 11, 15], target = 9
 * Output: [0, 1]
 * Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
 * 
 * Example 2:
 * Input: nums = [3, 2, 4], target = 6
 * Output: [1, 2]
 * Explanation: nums[1] + nums[2] == 6
 * 
 * Example 3:
 * Input: nums = [3, 3], target = 6
 * Output: [0, 1]
 * Explanation: Both elements are 3, and 3 + 3 = 6
 * 
 * ===================================================================
 * APPROACH 1: BRUTE FORCE
 * ===================================================================
 * 
 * IDEA:
 * For each element at index i, check all elements after it (j > i)
 * to see if nums[i] + nums[j] == target
 * 
 * ALGORITHM:
 * 1. Loop through array with index i (0 to n-1)
 * 2. For each i, loop through remaining elements with index j (i+1 to n-1)
 * 3. If nums[i] + nums[j] == target, return [i, j]
 * 
 * TIME COMPLEXITY: O(n²)
 *   - Outer loop: n iterations
 *   - Inner loop: (n-1) + (n-2) + ... + 1 = n(n-1)/2 iterations
 *   - Total: O(n²)
 * 
 * SPACE COMPLEXITY: O(1)
 *   - Only using a few variables
 * 
 * WHEN TO USE:
 * - When array is very small
 * - When you need to understand the problem first
 * - Not recommended for interviews (too slow)
 * 
 * ===================================================================
 * APPROACH 2: HASH MAP (OPTIMAL)
 * ===================================================================
 * 
 * IDEA:
 * Instead of checking all pairs, use a hash map to store:
 *   Key: number value
 *   Value: index of that number
 * 
 * For each number nums[i], we need to find if (target - nums[i]) exists.
 * If it exists in our map, we found the pair!
 * 
 * KEY INSIGHT:
 * If we need nums[i] + nums[j] = target,
 * then nums[j] = target - nums[i]
 * 
 * So for each nums[i], we look for (target - nums[i]) in the map.
 * 
 * ALGORITHM:
 * 1. Create a HashMap to store (number -> index)
 * 2. Iterate through array:
 *    a. Calculate complement = target - nums[i]
 *    b. Check if complement exists in map
 *       - If YES: return [map.get(complement), i]
 *       - If NO: add (nums[i], i) to map and continue
 * 
 * STEP-BY-STEP EXAMPLE:
 * 
 * nums = [2, 7, 11, 15], target = 9
 * map = {}
 * 
 * i=0: nums[0] = 2
 *      complement = 9 - 2 = 7
 *      Is 7 in map? NO
 *      Add (2, 0) to map
 *      map = {2: 0}
 * 
 * i=1: nums[1] = 7
 *      complement = 9 - 7 = 2
 *      Is 2 in map? YES! (at index 0)
 *      Return [0, 1] ✓
 * 
 * TIME COMPLEXITY: O(n)
 *   - Single pass through array
 *   - HashMap operations (get, put) are O(1) on average
 * 
 * SPACE COMPLEXITY: O(n)
 *   - HashMap can store up to n elements
 * 
 * WHY THIS WORKS:
 * - We only need to see each number once
 * - By storing numbers we've seen, we can instantly check
 *   if their complement exists
 * - This eliminates the need for nested loops
 * 
 * ===================================================================
 * EDGE CASES TO CONSIDER:
 * ===================================================================
 * 1. Array with 2 elements: [3, 3], target = 6
 * 2. Negative numbers: [-1, -2, -3, -4, -5], target = -8
 * 3. Zero: [0, 4, 3, 0], target = 0
 * 4. Large numbers
 * 5. No solution (problem states there's always one, but good to handle)
 * 
 * ===================================================================
 */
public class TwoSum {
    
    /**
     * APPROACH 1: BRUTE FORCE SOLUTION
     * 
     * This is the most straightforward approach but inefficient.
     * Use this to understand the problem, then optimize.
     * 
     * @param nums Array of integers
     * @param target Target sum
     * @return Array of two indices [i, j] where nums[i] + nums[j] = target
     */
    public static int[] twoSumBruteForce(int[] nums, int target) {
        // Try all possible pairs
        for (int i = 0; i < nums.length; i++) {
            // Start j from i+1 to avoid using same element twice
            for (int j = i + 1; j < nums.length; j++) {
                // Check if this pair sums to target
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        // According to problem, solution always exists, but return -1 if not found
        return new int[]{-1, -1};
    }
    
    /**
     * APPROACH 2: HASH MAP SOLUTION (OPTIMAL)
     * 
     * This is the solution you should use in interviews!
     * 
     * KEY CONCEPT:
     * For each number, we calculate what number we need to find:
     *   needed = target - current_number
     * 
     * If we've seen 'needed' before, we found our pair!
     * Otherwise, we store current number for future lookups.
     * 
     * @param nums Array of integers
     * @param target Target sum
     * @return Array of two indices [i, j] where nums[i] + nums[j] = target
     */
    public static int[] twoSum(int[] nums, int target) {
        // Map: number -> index where we saw it
        Map<Integer, Integer> map = new HashMap<>();
        
        // Single pass through array
        for (int i = 0; i < nums.length; i++) {
            // Calculate what number we need to find
            int complement = target - nums[i];
            
            // Check if we've seen the complement before
            if (map.containsKey(complement)) {
                // Found it! Return indices
                // map.get(complement) is the index where we saw the complement
                // i is the current index
                return new int[]{map.get(complement), i};
            }
            
            // We haven't seen the complement yet
            // Store current number and its index for future lookups
            map.put(nums[i], i);
        }
        
        // According to problem, solution always exists
        // But return -1 if somehow not found
        return new int[]{-1, -1};
    }
    
    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        
        System.out.println("Input: " + java.util.Arrays.toString(nums));
        System.out.println("Target: " + target);
        
        int[] result1 = twoSumBruteForce(nums, target);
        System.out.println("Brute Force Result: [" + result1[0] + ", " + result1[1] + "]");
        
        int[] result2 = twoSum(nums, target);
        System.out.println("Optimal Result: [" + result2[0] + ", " + result2[1] + "]");
        System.out.println("Values: " + nums[result2[0]] + " + " + nums[result2[1]] + " = " + target);
    }
}

