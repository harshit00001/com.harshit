package com.harshit.leetcode.binarysearch;

/**
 * Problem: Koko Eating Bananas
 * 
 * Koko loves to eat bananas. There are n piles of bananas, the ith pile has piles[i] bananas.
 * The guards have gone and will come back in h hours.
 * 
 * Koko can decide her bananas-per-hour eating speed of k. Each hour, she chooses some pile of
 * bananas and eats k bananas from that pile. If the pile has less than k bananas, she eats all
 * of them instead and will not eat any more bananas during this hour.
 * 
 * Koko wants to finish eating all the bananas before the guards return.
 * 
 * Return the minimum integer k such that she can eat all the bananas within h hours.
 * 
 * Example 1:
 * Input: piles = [3,6,7,11], h = 8
 * Output: 4
 * 
 * Example 2:
 * Input: piles = [30,11,23,4,20], h = 5
 * Output: 30
 * 
 * Example 3:
 * Input: piles = [30,11,23,4,20], h = 6
 * Output: 23
 * 
 * Constraints:
 * - 1 <= piles.length <= 10^4
 * - piles.length <= h <= 10^9
 * - 1 <= piles[i] <= 10^9
 */
public class KokoEatingBananas {
    
    /**
     * Solution using binary search (O(n * log(max)) time, O(1) space)
     * 
     * @param piles Array of banana piles
     * @param h Hours available
     * @return Minimum eating speed
     */
    public int minEatingSpeed(int[] piles, int h) {
        // Write your logic here
        // Hint: Binary search on eating speed k
        // Left = 1, right = max(piles)
        // For each k, calculate hours needed
        // If hours <= h, try smaller k, else try larger k
        return 0;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        KokoEatingBananas solution = new KokoEatingBananas();
        
        // Test case 1
        int[] piles1 = {3,6,7,11};
        int h1 = 8;
        int result1 = solution.minEatingSpeed(piles1, h1);
        System.out.println("Test 1 - piles=[3,6,7,11], h=8");
        System.out.println("Expected: 4, Got: " + result1);
        
        // Test case 2
        int[] piles2 = {30,11,23,4,20};
        int h2 = 5;
        int result2 = solution.minEatingSpeed(piles2, h2);
        System.out.println("Test 2 - piles=[30,11,23,4,20], h=5");
        System.out.println("Expected: 30, Got: " + result2);
    }
}

