package com.harshit.dsa.searching;

/**
 * Binary Search - Most Important Search Algorithm!
 * 
 * Prerequisite: Array must be sorted
 * 
 * Time: O(log n), Space: O(1) iterative, O(log n) recursive
 * 
 * Applications:
 * - Finding element in sorted array
 * - Finding first/last occurrence
 * - Finding insertion position
 * - Search in rotated sorted array
 */
public class BinarySearch {
    
    /**
     * Binary Search - Iterative
     * Returns index if found, -1 otherwise
     */
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2; // Avoid overflow
            
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1; // Not found
    }
    
    /**
     * Binary Search - Recursive
     */
    public static int binarySearchRecursive(int[] arr, int target) {
        return binarySearchRecursive(arr, target, 0, arr.length - 1);
    }
    
    private static int binarySearchRecursive(int[] arr, int target, int left, int right) {
        if (left > right) {
            return -1;
        }
        
        int mid = left + (right - left) / 2;
        
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            return binarySearchRecursive(arr, target, mid + 1, right);
        } else {
            return binarySearchRecursive(arr, target, left, mid - 1);
        }
    }
    
    /**
     * Find First Occurrence of target
     * Example: [1,2,2,2,3], target=2 -> returns 1
     */
    public static int findFirstOccurrence(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (arr[mid] == target) {
                result = mid;
                right = mid - 1; // Continue searching left
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * Find Last Occurrence of target
     * Example: [1,2,2,2,3], target=2 -> returns 3
     */
    public static int findLastOccurrence(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (arr[mid] == target) {
                result = mid;
                left = mid + 1; // Continue searching right
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * Search in Rotated Sorted Array
     * Example: [4,5,6,7,0,1,2], target=0 -> returns 4
     */
    public static int searchRotatedArray(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (arr[mid] == target) {
                return mid;
            }
            
            // Left half is sorted
            if (arr[left] <= arr[mid]) {
                if (target >= arr[left] && target < arr[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } 
            // Right half is sorted
            else {
                if (target > arr[mid] && target <= arr[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;
    }
    
    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 7, 9, 11, 13, 15};
        int target = 7;
        
        System.out.println("Array: " + java.util.Arrays.toString(arr));
        System.out.println("Searching for " + target);
        System.out.println("Found at index: " + binarySearch(arr, target));
        
        // Test with duplicates
        int[] arr2 = {1, 2, 2, 2, 3, 4, 5};
        System.out.println("\nArray with duplicates: " + java.util.Arrays.toString(arr2));
        System.out.println("First occurrence of 2: " + findFirstOccurrence(arr2, 2));
        System.out.println("Last occurrence of 2: " + findLastOccurrence(arr2, 2));
        
        // Test rotated array
        int[] rotated = {4, 5, 6, 7, 0, 1, 2};
        System.out.println("\nRotated array: " + java.util.Arrays.toString(rotated));
        System.out.println("Search 0: " + searchRotatedArray(rotated, 0));
        System.out.println("Search 5: " + searchRotatedArray(rotated, 5));
    }
}

