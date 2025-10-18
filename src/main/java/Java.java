//import java.util.Stack;
//class Node
//{
//    Node next;
//    int data;
//    Node(int data)
//    {
//        this.data=data;
//    }
//}
//public class Java {
//    public static Node reverseLink(Node head)
//    {
//        Node prev=null;
//        Node curr =head;
//        if(head==null)
//            return head;
//        while(curr.next!=null)
//        {
//            Node temp = curr.next;
//            curr.next = prev;
//            prev=curr;
//            curr=temp;
//        }
//        prev.=curr;
//        return prev;
//    }
//    public static void main(String[] args)
//    {
//        System.out.println("Hello World");
//        Node head = new Node(2);
//        head.next = new Node(3);
//        head.next.next = new Node(4);
//        head.next.next.next = new Node(5);
//        head.next.next.next.next = new Node(6);
//        Node head3= head;
//        while(head3!=null)
//        {
//            System.out.println(head3.data);
//            head3=head3.next;
//        }
//        Node curr2 = reverseLink(head);
//        Node head2= curr2;
//        while(head2!=null)
//        {
//            System.out.println(head2.data);
//            head2=head2.next;
//        }
//
//    }
//}
//class ListNode {
//    int val;
//    ListNode next;
//
//    ListNode(int val) {
//        this.val = val;
//    }
//}
//
//public class ReverseLinkedList {
//
//    // Method to reverse the linked list
//    public static ListNode reverseList(ListNode head) {
//        ListNode prev = null;
//        ListNode curr = head;
//
//        while (curr != null) {
//            ListNode nextTemp = curr.next;
//            curr.next = prev;
//            prev = curr;
//            curr = nextTemp;
//        }
//
//        return prev;
//    }
//
//    // Helper method to print the list
//    public static void printList(ListNode head) {
//        ListNode curr = head;
//        while (curr != null) {
//            System.out.print(curr.val + " -> ");
//            curr = curr.next;
//        }
//        System.out.println("null");
//    }
//
//    public static void main(String[] args) {
//        // Create linked list: 1 -> 2 -> 3 -> 4 -> null
//        ListNode head = new ListNode(1);
//        head.next = new ListNode(2);
//        head.next.next = new ListNode(3);
//        head.next.next.next = new ListNode(4);
//
//        System.out.println("Original List:");
//        printList(head);
//
//        // Reverse the list
//        ListNode reversedHead = reverseList(head);
//
//        System.out.println("Reversed List:");
//        printList(reversedHead);
//    }
//}
class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
    }
}

public class Java {

    // Method to reverse the linked list
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }

        return prev;
    }

    // Helper method to print the list
    public static void printList(ListNode head) {
        ListNode curr = head;
        while (curr != null) {
            System.out.print(curr.val + " -> ");
            curr = curr.next;
        }
        System.out.println("null");
    }

    public static void main(String[] args) {
        // Create linked list: 1 -> 2 -> 3 -> 4 -> null
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);

        System.out.println("Original List:");
        printList(head);

        // Reverse the list
        ListNode reversedHead = reverseList(head);

        System.out.println("Reversed List:");
        printList(reversedHead);
    }
}
