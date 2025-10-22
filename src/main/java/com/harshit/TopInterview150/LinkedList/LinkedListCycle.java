package com.harshit.TopInterview150.LinkedList;


class ListNode {
      int val;
      ListNode next;
      ListNode(int x) {
          val = x;
          next = null;
      }
  }

public class LinkedListCycle {
    public static ListNode createLinkedList(int[] values, int pos) {
        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        ListNode cycleNode = null;

        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
            if (i == pos)
                cycleNode = current;
        }

        if (pos >= 0) {
            current.next = cycleNode; // create cycle
        }

        return head;
    }

    private static boolean hasCycle(ListNode head) {
            ListNode slow = head;
            ListNode fast = head;
            if(head == null)
                return false;
            while(fast!=null && fast.next!=null)
            {
                slow = slow.next;
                fast = fast.next.next;

                if(fast!=null && fast.equals(slow))
                    return true;
            }
            return false;
        }

    public static void main(String[] args) {
        int[] list = {3,2,0,-4};
//            ListNode ls = new ListNode(3);
//            ls.next= new ListNode(2);
//            ls.next.next= new ListNode(0);
//            ls.next.next.next= new ListNode(-4);
//            ls.next.next.next.next= new ListNode(9);
//            ls.next.next.next.next.next= new ListNode(12);
//            ls.next.next.next.next.next.next= new ListNode(7);
        ListNode ls = createLinkedList(list,1);
        if(hasCycle(ls))
        {
            System.out.println("Cycled");
        }
        else {
            System.out.println("not Cycled");
        }
    }

}
