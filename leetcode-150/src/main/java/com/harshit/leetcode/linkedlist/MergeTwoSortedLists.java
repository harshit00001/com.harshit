package com.harshit.leetcode.linkedlist;

/**
 * Problem: Merge Two Sorted Lists
 * 
 * You are given the heads of two sorted linked lists list1 and list2.
 * 
 * Merge the two lists in a one sorted list. The list should be made by splicing together
 * the nodes of the first two lists.
 * 
 * Return the head of the merged linked list.
 * 
 * Example 1:
 * Input: list1 = [1,2,4], list2 = [1,3,4]
 * Output: [1,1,2,3,4,4]
 * 
 * Example 2:
 * Input: list1 = [], list2 = []
 * Output: []
 * 
 * Example 3:
 * Input: list1 = [], list2 = [0]
 * Output: [0]
 * 
 * Constraints:
 * - The number of nodes in both lists is in the range [0, 50].
 * - -100 <= Node.val <= 100
 * - Both list1 and list2 are sorted in non-decreasing order.
 */
public class MergeTwoSortedLists {
    
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
     * Iterative solution (O(n + m) time, O(1) space)
     * 
     * @param list1 Head of first sorted list
     * @param list2 Head of second sorted list
     * @return Head of merged sorted list
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // Write your logic here
        // Hint: Use dummy node to simplify merging
        // Compare nodes from both lists, attach smaller one
        // Continue until one list is exhausted
        return null;
    }
    
    /**
     * Recursive solution (O(n + m) time, O(n + m) space)
     * 
     * @param list1 Head of first sorted list
     * @param list2 Head of second sorted list
     * @return Head of merged sorted list
     */
    public ListNode mergeTwoListsRecursive(ListNode list1, ListNode list2) {
        // Write your logic here
        // Hint: Recursively merge rest of lists
        // Choose smaller node and recursively merge
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
        MergeTwoSortedLists solution = new MergeTwoSortedLists();
        
        // Test case 1
        int[] arr1 = {1,2,4};
        int[] arr2 = {1,3,4};
        ListNode list1 = createList(arr1);
        ListNode list2 = createList(arr2);
        System.out.println("Test 1 - Input: list1=[1,2,4], list2=[1,3,4]");
        System.out.print("Merged: ");
        ListNode result1 = solution.mergeTwoLists(list1, list2);
        printList(result1);
        System.out.println("Expected: [1,1,2,3,4,4]");
        System.out.println();
        
        // Test case 2
        int[] arr3 = {};
        int[] arr4 = {};
        ListNode list3 = createList(arr3);
        ListNode list4 = createList(arr4);
        System.out.println("Test 2 - Input: list1=[], list2=[]");
        System.out.print("Merged: ");
        ListNode result2 = solution.mergeTwoLists(list3, list4);
        printList(result2);
        System.out.println("Expected: []");
    }
}

