package com.harshit.TopInterview150.LinkedList;

public class MergeTwoSortedList {

    private static ListNode merge(ListNode ls1, ListNode ls2)
    {
        ListNode dummy = new ListNode(0);
        ListNode head3 = dummy;
        while(ls1!=null && ls2!=null )
        {
            if(ls1.val<=ls2.val)
            {
                head3.next = ls1;
                ls1= ls1.next;
            }
            else {
                head3.next = ls2;
                ls2=ls2.next;
            }

            head3 = head3.next;
        }
        head3.next= ls1!=null?ls1:ls2;
        return dummy.next;
    }
    private static void printNode(ListNode node)
    {
        while(node!=null)
        {
            System.out.print(node.val+" ");
            node = node.next;
        }
    }
    private static ListNode getNode(int[] list)
    {
        ListNode node = new ListNode(0);
        ListNode current = node;
        for(int i=0;i<list.length;i++)
        {
            current.next=new ListNode(list[i]);
            current= current.next;
        }
        current.next = null;
        return node.next;
    }
    public static void main(String[] args) {
        int[] list1 = {1,3,6,8,13};
        int[] list2 = {2,4,7,9,11,32};

        ListNode head = getNode(list1);
        ListNode head2 = getNode(list2);
        System.out.println("----- List1 node ------");

        printNode(head);
        System.out.println("----- List2 node ------");

        printNode(head2);
        System.out.println("----- Result node ------");
        ListNode result = merge(head,head2);
        printNode(result);


    }
}
