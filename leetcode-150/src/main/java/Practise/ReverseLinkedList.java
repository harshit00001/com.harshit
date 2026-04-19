package Practise;

class Node2
{
    Node2 node;
    int data;
    Node2(int data)
    {
        this.data=data;
    }
    Node2(int data,Node2 node)
    {
        this.data = data;
        this.node = node;
    }
}

public class ReverseLinkedList {
    public static void main(String[] args) {
        Node2 node = new Node2(2);
        node.node = new Node2(4);
        node.node.node = new Node2(1);
        node.node.node.node = new Node2(6);
        node.node.node.node.node = new Node2(7);
        node.node.node.node.node.node = new Node2(9);
        print(node);
        Node2 node4 = reverse(node);
        System.out.println();
        print(node4);

    }
    public static void print(Node2 node)
    {
        while(node!=null)
        {
            System.out.println(node.data);
            node= node.node;

        }
    }
    static Node2 reverse(Node2 node)
    {
        Node2 next = null;
        Node2 temp= node;

        while(temp!=null)
        {
            Node2 node3= temp.node;
            temp.node = next;
            next = temp;
            temp =node3;

        }
        return next;
    }
}
