package Practise;

public class MiddleLL {
    public static void printAll(Node head)
    {
        while(head!= null)
        {
            System.out.println(head.a);
            head= head.node;
        }
    }
    public static int middle(Node head)
    {
        Node slow=head, fast =head;
        while(fast.node!=null && fast.node.node!=null && slow.node!=null)
        {
            slow = slow.node;
            fast = fast.node.node;
        }
        return slow.a;
    }
    public static void main(String[] args) {
        Node node = new Node(1);
        node.node  = new Node(2);
        node.node.node = new Node(3);
        node.node.node.node = new Node(4);
        node.node.node.node.node = new Node(5);
        node.node.node.node.node.node = new Node(6);
        //node.node.node.node.node.node.node = new Node(7);
        printAll(node);
        int result= middle(node);
        System.out.println("-----result----");
        System.out.println(result);
    }
}
