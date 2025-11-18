package com.harshit.dsa.linkedlist;

/**
 * Detect Cycle in Linked List (Floyd's Cycle Detection Algorithm)
 * 
 * Problem: Determine if a linked list has a cycle.
 * 
 * Example:
 * Input: 1 -> 2 -> 3 -> 4 -> 5 -> 3 (cycle back to 3)
 * Output: true
 * 
 * Approach: Floyd's Cycle Detection (Tortoise and Hare)
 * - Use two pointers: slow (moves 1 step) and fast (moves 2 steps)
 * - If there's a cycle, they will eventually meet
 * - If fast reaches null, there's no cycle
 * 
 * Time: O(n), Space: O(1)
 */
public class DetectCycle {
    
    /**
     * Detect if linked list has a cycle
     */
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        
        ListNode slow = head; // Tortoise
        ListNode fast = head; // Hare
        
        while (fast != null && fast.next != null) {
            slow = slow.next; // Move 1 step
            fast = fast.next.next; // Move 2 steps
            
            if (slow == fast) {
                return true; // Cycle detected
            }
        }
        
        return false; // No cycle
    }
    
    /**
     * Find the node where cycle begins (if cycle exists)
     * 
     * After detecting cycle, reset one pointer to head
     * and move both one step at a time. They meet at cycle start.
     */
    public static ListNode detectCycleStart(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }
        
        ListNode slow = head;
        ListNode fast = head;
        boolean hasCycle = false;
        
        // Detect cycle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }
        
        if (!hasCycle) {
            return null;
        }
        
        // Find cycle start
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        
        return slow; // Cycle start node
    }
    
    public static void main(String[] args) {
        // Create a list with cycle: 1 -> 2 -> 3 -> 4 -> 5 -> 3
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node3; // Cycle back to node3
        
        System.out.println("Has Cycle: " + hasCycle(node1));
        
        ListNode cycleStart = detectCycleStart(node1);
        if (cycleStart != null) {
            System.out.println("Cycle starts at node with value: " + cycleStart.val);
        }
        
        // Test without cycle
        ListNode head2 = ListNode.createList(new int[]{1, 2, 3, 4, 5});
        System.out.println("List without cycle has cycle: " + hasCycle(head2));
    }
}

