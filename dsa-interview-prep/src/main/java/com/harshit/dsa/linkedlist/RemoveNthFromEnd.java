package com.harshit.dsa.linkedlist;

/**
 * Remove Nth Node From End of List
 * 
 * Problem: Remove the nth node from the end of the list.
 * 
 * Example:
 * Input: head = [1,2,3,4,5], n = 2
 * Output: [1,2,3,5]
 * 
 * Approach: Two pointers technique
 * - Move first pointer n steps ahead
 * - Then move both pointers until first reaches end
 * - Second pointer will be at (n+1)th node from end
 * 
 * Time: O(n), Space: O(1)
 */
public class RemoveNthFromEnd {
    
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        // Dummy node to handle edge case (removing head)
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        ListNode first = dummy;
        ListNode second = dummy;
        
        // Move first pointer n+1 steps ahead
        for (int i = 0; i <= n; i++) {
            first = first.next;
        }
        
        // Move both pointers until first reaches end
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        
        // Remove nth node from end
        second.next = second.next.next;
        
        return dummy.next;
    }
    
    public static void main(String[] args) {
        ListNode head = ListNode.createList(new int[]{1, 2, 3, 4, 5});
        
        System.out.println("Original List:");
        ListNode.printList(head);
        
        head = removeNthFromEnd(head, 2);
        System.out.println("After removing 2nd node from end:");
        ListNode.printList(head);
    }
}

