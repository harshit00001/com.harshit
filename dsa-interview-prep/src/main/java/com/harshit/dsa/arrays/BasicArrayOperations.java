package com.harshit.dsa.arrays;

/**
 * Basic Array Operations
 * 
 * Arrays are the most fundamental data structure.
 * Time Complexity: O(1) for access, O(n) for search/insert/delete
 * Space Complexity: O(n)
 */
public class BasicArrayOperations {
    
    /**
     * Find maximum element in array
     * Time: O(n), Space: O(1)
     */
    public static int findMax(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }
    
    /**
     * Find minimum element in array
     * Time: O(n), Space: O(1)
     */
    public static int findMin(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }
    
    /**
     * Reverse an array
     * Time: O(n), Space: O(1)
     */
    public static void reverse(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        
        while (left < right) {
            // Swap elements
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            
            left++;
            right--;
        }
    }
    
    /**
     * Find sum of all elements
     * Time: O(n), Space: O(1)
     */
    public static int sum(int[] arr) {
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        return sum;
    }
    
    /**
     * Find average of array elements
     * Time: O(n), Space: O(1)
     */
    public static double average(int[] arr) {
        if (arr.length == 0) return 0;
        return (double) sum(arr) / arr.length;
    }
    
    /**
     * Search element in array (Linear Search)
     * Time: O(n), Space: O(1)
     */
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1; // Not found
    }
    
    public static void main(String[] args) {
        int[] arr = {5, 2, 8, 1, 9, 3};
        
        System.out.println("Original Array: " + java.util.Arrays.toString(arr));
        System.out.println("Max: " + findMax(arr));
        System.out.println("Min: " + findMin(arr));
        System.out.println("Sum: " + sum(arr));
        System.out.println("Average: " + average(arr));
        
        System.out.println("\nSearching for 8:");
        int index = linearSearch(arr, 8);
        System.out.println("Found at index: " + index);
        
        reverse(arr);
        System.out.println("Reversed Array: " + java.util.Arrays.toString(arr));
    }
}

