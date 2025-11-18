package com.harshit.dsa.linkedlist;

/**
 * Merge Two Sorted Linked Lists
 * 
 * Problem: Merge two sorted linked lists into one sorted list.
 * 
 * Example:
 * Input: 
 *   list1: 1 -> 2 -> 4
 *   list2: 1 -> 3 -> 4
 * Output: 1 -> 1 -> 2 -> 3 -> 4 -> 4
 * 
 * Approach: Use two pointers, compare and merge
 * Time: O(n + m), Space: O(1)
 */
public class MergeTwoSortedLists {
    
    /**
     * Iterative approach
     */
    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // Dummy node to simplify code
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                current.next = list1;
                list1 = list1.next;
            } else {
                current.next = list2;
                list2 = list2.next;
            }
            current = current.next;
        }
        
        // Attach remaining nodes
        if (list1 != null) {
            current.next = list1;
        } else {
            current.next = list2;
        }
        
        return dummy.next;
    }
    
    /**
     * Recursive approach
     */
    public static ListNode mergeTwoListsRecursive(ListNode list1, ListNode list2) {
        // Base cases
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        
        if (list1.val <= list2.val) {
            list1.next = mergeTwoListsRecursive(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeTwoListsRecursive(list1, list2.next);
            return list2;
        }
    }
    
    public static void main(String[] args) {
        ListNode list1 = ListNode.createList(new int[]{1, 2, 4});
        ListNode list2 = ListNode.createList(new int[]{1, 3, 4});
        
        System.out.println("List 1:");
        ListNode.printList(list1);
        System.out.println("List 2:");
        ListNode.printList(list2);
        
        ListNode merged = mergeTwoLists(list1, list2);
        System.out.println("Merged List:");
        ListNode.printList(merged);
    }
}

