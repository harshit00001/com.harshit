package Practise;

public class ReverseLL {
    public static void printAll(Node head)
    {
        while(head!= null)
        {
            System.out.println(head.a);
            head= head.node;
        }
    }
    public static Node reverse(Node head)
    {
        Node temp = head;
        Node prev = null;
        while(temp.node!=null) {
            Node front = temp.node;
            temp.node = prev;
            prev= temp;
            temp = front;
        }
        return prev;
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
        Node result= reverse(node);
        System.out.println("-----result----");
        printAll(result);
        System.out.println(result);
    }
}
