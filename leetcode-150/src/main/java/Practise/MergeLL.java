package Practise;

public class MergeLL {
    public static void main(String[] args) {
        ListNode node = new ListNode(2);
        node.next= new ListNode(3);
        node.next.next= new ListNode(8);
        node.next.next.next= new ListNode(12);
        node.next.next.next.next= new ListNode(14);

        ListNode node1 = new ListNode(4);
        node1.next= new ListNode(6);
        node1.next.next= new ListNode(7);
        node1.next.next.next= new ListNode(11);
        node1.next.next.next.next= new ListNode(13);
        ListNode result = mergeTwoLists(node,node1);
        while(result!=null)
        {
            System.out.print(result.val+ "  ");
            result = result.next;
        }
    }
    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode node = new ListNode(0);
        ListNode current = node;
        while(list1 != null && list2 != null)
        {
             if(list1.val<=list2.val)
             {
                 current.next = list1;
                 list1=list1.next;
             }
             else
             {
                 current.next = list2;
                 list2=list2.next;
             }
             current=current.next;
        }
        current.next=(list1!=null)?list1:list2;
        return node.next;

    }
}
class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

