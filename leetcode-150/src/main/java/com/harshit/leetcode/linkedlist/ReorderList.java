package com.harshit.leetcode.linkedlist;

/**
 * Problem: Reorder List
 * 
 * You are given the head of a singly linked-list. The list can be represented as:
 * L0 → L1 → … → Ln - 1 → Ln
 * 
 * Reorder the list to be on the following form:
 * L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
 * 
 * You may not modify the values in the list's nodes. Only nodes themselves may be changed.
 * 
 * Example 1:
 * Input: head = [1,2,3,4]
 * Output: [1,4,2,3]
 * 
 * Example 2:
 * Input: head = [1,2,3,4,5]
 * Output: [1,5,2,4,3]
 * 
 * Constraints:
 * - The number of nodes in the list is in the range [1, 5 * 10^4].
 * - 1 <= Node.val <= 1000
 */
public class ReorderList {
    
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
     * Solution using three steps (O(n) time, O(1) space)
     * 
     * @param head Head of the linked list
     */
    public void reorderList(ListNode head) {
        // Write your logic here
        // Hint: 1. Find middle of list
        // 2. Reverse second half
        // 3. Merge first half and reversed second half alternately
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
        ReorderList solution = new ReorderList();
        
        // Test case 1
        int[] arr1 = {1,2,3,4};
        ListNode head1 = createList(arr1);
        System.out.println("Test 1 - Input: [1,2,3,4]");
        System.out.print("Original: ");
        printList(head1);
        solution.reorderList(head1);
        System.out.print("Reordered: ");
        printList(head1);
        System.out.println("Expected: [1,4,2,3]");
        System.out.println();
        
        // Test case 2
        int[] arr2 = {1,2,3,4,5};
        ListNode head2 = createList(arr2);
        System.out.println("Test 2 - Input: [1,2,3,4,5]");
        System.out.print("Original: ");
        printList(head2);
        solution.reorderList(head2);
        System.out.print("Reordered: ");
        printList(head2);
        System.out.println("Expected: [1,5,2,4,3]");
    }
}

