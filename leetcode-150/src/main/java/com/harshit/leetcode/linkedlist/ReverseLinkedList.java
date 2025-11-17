package com.harshit.leetcode.linkedlist;

/**
 * Problem: Reverse Linked List
 * 
 * Given the head of a singly linked list, reverse the list, and return the reversed list.
 * 
 * Example 1:
 * Input: head = [1,2,3,4,5]
 * Output: [5,4,3,2,1]
 * 
 * Example 2:
 * Input: head = [1,2]
 * Output: [2,1]
 * 
 * Example 3:
 * Input: head = []
 * Output: []
 * 
 * Constraints:
 * - The number of nodes in the list is the range [0, 5000].
 * - -5000 <= Node.val <= 5000
 * 
 * Follow up: A linked list can be reversed either iteratively or recursively. Could you implement both?
 */
public class ReverseLinkedList {
    
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
     * Iterative solution (O(n) time, O(1) space)
     * 
     * @param head Head of the linked list
     * @return Head of reversed linked list
     */
    public ListNode reverseList(ListNode head) {
        // Write your logic here
        // Hint: Use three pointers: prev, current, next
        // Traverse list and reverse pointers
        return null;
    }
    
    /**
     * Recursive solution (O(n) time, O(n) space for recursion stack)
     * 
     * @param head Head of the linked list
     * @return Head of reversed linked list
     */
    public ListNode reverseListRecursive(ListNode head) {
        // Write your logic here
        // Hint: Recursively reverse rest of list
        // Then set current node's next to point to previous
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
        ReverseLinkedList solution = new ReverseLinkedList();
        
        // Test case 1
        int[] arr1 = {1, 2, 3, 4, 5};
        ListNode head1 = createList(arr1);
        System.out.println("Test 1 - Input: [1,2,3,4,5]");
        System.out.print("Original: ");
        printList(head1);
        ListNode result1 = solution.reverseList(head1);
        System.out.print("Reversed: ");
        printList(result1);
        System.out.println("Expected: [5,4,3,2,1]");
        System.out.println();
        
        // Test case 2
        int[] arr2 = {1, 2};
        ListNode head2 = createList(arr2);
        System.out.println("Test 2 - Input: [1,2]");
        System.out.print("Original: ");
        printList(head2);
        ListNode result2 = solution.reverseList(head2);
        System.out.print("Reversed: ");
        printList(result2);
        System.out.println("Expected: [2,1]");
        System.out.println();
        
        // Test case 3
        int[] arr3 = {};
        ListNode head3 = createList(arr3);
        System.out.println("Test 3 - Input: []");
        System.out.print("Original: ");
        printList(head3);
        ListNode result3 = solution.reverseList(head3);
        System.out.print("Reversed: ");
        printList(result3);
        System.out.println("Expected: []");
    }
}

