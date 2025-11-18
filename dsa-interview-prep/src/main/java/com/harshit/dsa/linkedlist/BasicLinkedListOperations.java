package com.harshit.dsa.linkedlist;

/**
 * Basic Linked List Operations
 * 
 * Linked List: Linear data structure where elements are linked via pointers
 * - Singly Linked List: Each node points to next node
 * - Doubly Linked List: Each node points to both next and previous
 * 
 * Time Complexity:
 * - Access: O(n)
 * - Search: O(n)
 * - Insert/Delete at beginning: O(1)
 * - Insert/Delete at end: O(n) (O(1) if we maintain tail pointer)
 */
public class BasicLinkedListOperations {
    
    /**
     * Insert at the beginning of linked list
     * Time: O(1)
     */
    public static ListNode insertAtBeginning(ListNode head, int val) {
        ListNode newNode = new ListNode(val);
        newNode.next = head;
        return newNode; // New head
    }
    
    /**
     * Insert at the end of linked list
     * Time: O(n)
     */
    public static ListNode insertAtEnd(ListNode head, int val) {
        ListNode newNode = new ListNode(val);
        
        if (head == null) {
            return newNode;
        }
        
        ListNode current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
        
        return head;
    }
    
    /**
     * Delete a node with given value
     * Time: O(n)
     */
    public static ListNode deleteNode(ListNode head, int val) {
        if (head == null) {
            return null;
        }
        
        // If head needs to be deleted
        if (head.val == val) {
            return head.next;
        }
        
        ListNode current = head;
        while (current.next != null) {
            if (current.next.val == val) {
                current.next = current.next.next;
                return head;
            }
            current = current.next;
        }
        
        return head; // Value not found
    }
    
    /**
     * Find length of linked list
     * Time: O(n)
     */
    public static int length(ListNode head) {
        int count = 0;
        ListNode current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }
    
    /**
     * Search for a value in linked list
     * Time: O(n)
     */
    public static boolean search(ListNode head, int val) {
        ListNode current = head;
        while (current != null) {
            if (current.val == val) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    
    /**
     * Reverse a linked list (Iterative)
     * Time: O(n), Space: O(1)
     */
    public static ListNode reverse(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        
        while (current != null) {
            ListNode next = current.next; // Store next node
            current.next = prev; // Reverse the link
            prev = current; // Move prev forward
            current = next; // Move current forward
        }
        
        return prev; // New head
    }
    
    /**
     * Reverse a linked list (Recursive)
     * Time: O(n), Space: O(n) due to recursion stack
     */
    public static ListNode reverseRecursive(ListNode head) {
        // Base case
        if (head == null || head.next == null) {
            return head;
        }
        
        // Reverse rest of the list
        ListNode newHead = reverseRecursive(head.next);
        
        // Reverse current node's link
        head.next.next = head;
        head.next = null;
        
        return newHead;
    }
    
    public static void main(String[] args) {
        // Create list: 1 -> 2 -> 3 -> 4 -> 5
        ListNode head = ListNode.createList(new int[]{1, 2, 3, 4, 5});
        
        System.out.println("Original List:");
        ListNode.printList(head);
        
        System.out.println("Length: " + length(head));
        System.out.println("Search 3: " + search(head, 3));
        System.out.println("Search 10: " + search(head, 10));
        
        head = insertAtBeginning(head, 0);
        System.out.println("After inserting 0 at beginning:");
        ListNode.printList(head);
        
        head = insertAtEnd(head, 6);
        System.out.println("After inserting 6 at end:");
        ListNode.printList(head);
        
        head = deleteNode(head, 3);
        System.out.println("After deleting 3:");
        ListNode.printList(head);
        
        head = reverse(head);
        System.out.println("After reversing:");
        ListNode.printList(head);
    }
}

