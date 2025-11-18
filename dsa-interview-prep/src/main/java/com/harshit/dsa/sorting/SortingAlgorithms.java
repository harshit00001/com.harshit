package com.harshit.dsa.sorting;

import java.util.Arrays;

/**
 * Sorting Algorithms - Must Know for Interviews!
 * 
 * Comparison of Sorting Algorithms:
 * 
 * Algorithm      | Best  | Average | Worst | Space | Stable
 * --------------|-------|---------|-------|-------|--------
 * Bubble Sort   | O(n)  | O(n²)   | O(n²) | O(1)  | Yes
 * Selection Sort| O(n²) | O(n²)   | O(n²) | O(1)  | No
 * Insertion Sort| O(n)  | O(n²)   | O(n²) | O(1)  | Yes
 * Merge Sort    | O(n log n) | O(n log n) | O(n log n) | O(n) | Yes
 * Quick Sort    | O(n log n) | O(n log n) | O(n²) | O(log n) | No
 * Heap Sort     | O(n log n) | O(n log n) | O(n log n) | O(1) | No
 */
public class SortingAlgorithms {
    
    /**
     * Bubble Sort
     * Repeatedly swap adjacent elements if they are in wrong order
     * Time: O(n²), Space: O(1)
     */
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // Swap
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            // Optimization: If no swap, array is sorted
            if (!swapped) break;
        }
    }
    
    /**
     * Selection Sort
     * Find minimum element and place it at beginning
     * Time: O(n²), Space: O(1)
     */
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            // Swap
            int temp = arr[i];
            arr[i] = arr[minIdx];
            arr[minIdx] = temp;
        }
    }
    
    /**
     * Insertion Sort
     * Build sorted array one element at a time
     * Time: O(n²), Space: O(1)
     * Best for small arrays or nearly sorted arrays
     */
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            
            // Move elements greater than key one position ahead
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
    
    /**
     * Merge Sort - Divide and Conquer
     * Time: O(n log n), Space: O(n)
     */
    public static void mergeSort(int[] arr) {
        mergeSort(arr, 0, arr.length - 1);
    }
    
    private static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            // Sort first and second halves
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            
            // Merge the sorted halves
            merge(arr, left, mid, right);
        }
    }
    
    private static void merge(int[] arr, int left, int mid, int right) {
        // Create temporary arrays
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];
        
        // Copy data to temp arrays
        System.arraycopy(arr, left, leftArr, 0, n1);
        System.arraycopy(arr, mid + 1, rightArr, 0, n2);
        
        // Merge temp arrays
        int i = 0, j = 0, k = left;
        
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
        }
        
        // Copy remaining elements
        while (i < n1) arr[k++] = leftArr[i++];
        while (j < n2) arr[k++] = rightArr[j++];
    }
    
    /**
     * Quick Sort - Divide and Conquer
     * Time: O(n log n) average, O(n²) worst, Space: O(log n)
     */
    public static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }
    
    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Partition and get pivot index
            int pi = partition(arr, low, high);
            
            // Recursively sort elements before and after partition
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // Choose last element as pivot
        int i = low - 1; // Index of smaller element
        
        for (int j = low; j < high; j++) {
            // If current element is smaller than pivot
            if (arr[j] < pivot) {
                i++;
                // Swap
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        
        // Swap pivot with element at i+1
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        
        return i + 1;
    }
    
    public static void main(String[] args) {
        int[] arr1 = {64, 34, 25, 12, 22, 11, 90};
        int[] arr2 = arr1.clone();
        int[] arr3 = arr1.clone();
        int[] arr4 = arr1.clone();
        int[] arr5 = arr1.clone();
        
        System.out.println("Original Array: " + Arrays.toString(arr1));
        
        bubbleSort(arr1);
        System.out.println("Bubble Sort: " + Arrays.toString(arr1));
        
        selectionSort(arr2);
        System.out.println("Selection Sort: " + Arrays.toString(arr2));
        
        insertionSort(arr3);
        System.out.println("Insertion Sort: " + Arrays.toString(arr3));
        
        mergeSort(arr4);
        System.out.println("Merge Sort: " + Arrays.toString(arr4));
        
        quickSort(arr5);
        System.out.println("Quick Sort: " + Arrays.toString(arr5));
    }
}

