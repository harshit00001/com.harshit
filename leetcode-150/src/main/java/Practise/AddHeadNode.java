package Practise;

class Node
{
    int a;
    Node node;
    Node(int a , Node node)
    {
        this.a = a;
        this.node= node;
    }
    Node(int a)
    {
        this.a = a;
        node = null;
    }
}
public class AddHeadNode {
    public static Node insert(int a , Node head)
    {
        Node temp = head;
        Node newNode= new Node(a,temp);
        return newNode;

    }
    public static void printAll(Node head)
    {
        while(head!= null)
        {
            System.out.println(head.a);
            head= head.node;
        }
    }

    public static void main(String[] args) {
        Node node = new Node(1);
        node.node  = new Node(2);
        node.node.node = new Node(3);
        node.node.node.node = new Node(4);

        printAll(node);
        Node newNode = insert(9,node);
        System.out.println("-------");
        printAll(newNode);


    }
}
