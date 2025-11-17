package com.harshit.leetcode.heap;

import java.util.*;

/**
 * Problem: Merge k Sorted Lists
 * 
 * You are given an array of k linked-lists lists, each linked-list is sorted in ascending order.
 * 
 * Merge all the linked-lists into one sorted linked-list and return it.
 * 
 * Example 1:
 * Input: lists = [[1,4,5],[1,3,4],[2,6]]
 * Output: [1,1,2,3,4,4,5,6]
 * Explanation: The linked-lists are:
 * [
 *   1->4->5,
 *   1->3->4,
 *   2->6
 * ]
 * merging them into one sorted list:
 * 1->1->2->3->4->4->5->6
 * 
 * Example 2:
 * Input: lists = []
 * Output: []
 * 
 * Example 3:
 * Input: lists = [[]]
 * Output: []
 * 
 * Constraints:
 * - k == lists.length
 * - 0 <= k <= 10^4
 * - 0 <= lists[i].length <= 500
 * - -10^4 <= lists[i][j] <= 10^4
 * - lists[i] is sorted in ascending order.
 * - The sum of lists[i].length will not exceed 10^4.
 */
public class MergeKSortedLists {
    
    /**
     * Definition for singly-linked list node
     */
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    /**
     * Solution using Priority Queue (O(n log k) time, O(k) space)
     * 
     * @param lists Array of sorted linked lists
     * @return Merged sorted linked list
     */
    public ListNode mergeKLists(ListNode[] lists) {
        // Write your logic here
        // Hint: Use min heap (PriorityQueue)
        // Add first node of each list to heap
        // Pop smallest, add its next to heap, repeat
        return null;
    }
    
    /**
     * Solution using Divide and Conquer (O(n log k) time, O(1) space)
     * 
     * @param lists Array of sorted linked lists
     * @return Merged sorted linked list
     */
    public ListNode mergeKListsDivideConquer(ListNode[] lists) {
        // Write your logic here
        // Hint: Merge lists in pairs repeatedly
        // Similar to merge sort
        return null;
    }
    
    /**
     * Helper method to create linked list from array
     */
    public static ListNode createList(int[] arr) {
        if (arr.length == 0) return null;
        ListNode head = new ListNode(arr[0]);
        ListNode current = head;
        for (int i = 1; i < arr.length; i++) {
            current.next = new ListNode(arr[i]);
            current = current.next;
        }
        return head;
    }
    
    /**
     * Helper method to print linked list
     */
    public static void printList(ListNode head) {
        ListNode current = head;
        System.out.print("[");
        while (current != null) {
            System.out.print(current.val);
            if (current.next != null) System.out.print(",");
            current = current.next;
        }
        System.out.println("]");
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        MergeKSortedLists solution = new MergeKSortedLists();
        
        // Test case 1
        ListNode list1 = createList(new int[]{1,4,5});
        ListNode list2 = createList(new int[]{1,3,4});
        ListNode list3 = createList(new int[]{2,6});
        ListNode[] lists1 = {list1, list2, list3};
        
        System.out.println("Test 1 - Input: [[1,4,5],[1,3,4],[2,6]]");
        ListNode result1 = solution.mergeKLists(lists1);
        System.out.print("Merged: ");
        printList(result1);
        System.out.println("Expected: [1,1,2,3,4,4,5,6]");
    }
}

