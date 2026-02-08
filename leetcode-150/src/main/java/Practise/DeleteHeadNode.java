package Practise;

public class DeleteHeadNode {
    private static Node head;
    public static Node delete(Node head, int pos)
    {
        if(head == null)
        {
            return head;
        }
        if(pos==1)
        {
            head = head.node;
            return head;
        }
        Node temp = head;
        for(int i=1;i<pos-1 && temp!=null;i++)
        {
            temp = temp.node;
        }
        if(temp==null || temp.node==null)
            return temp;
        temp.node = temp.node.node;
        return head;

    }
    public static void printAll(Node head)
    {
        while(head!= null)
        {
            System.out.println(head.a);
            head= head.node;
        }
    }
    public static void addNode(int a)
    {
        Node node = new Node(a);
        node.node = head;
        head = node;
    }

    public static void main(String[] args) {
        for(int i =0;i<5;i++)
        {
            addNode(i);
        }

        printAll(head);
        Node newNode = delete(head,3);
        System.out.println("-------");
        printAll(newNode);

    }
}
