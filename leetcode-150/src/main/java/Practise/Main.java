package Practise;

import java.util.LinkedList;
import java.util.Queue;

// Definition for a binary tree node.
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {}

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

public class Main {

    // Optimized Iterative BFS Solution
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();

            // Swap the left and right pointers
            TreeNode temp = current.left;
            current.left = current.right;
            current.right = temp;

            // Enqueue children if they exist
            if (current.left != null) {
                queue.add(current.left);
            }
            if (current.right != null) {
                queue.add(current.right);
            }
        }

        return root;
    }

    // Helper method to print the tree in level-order (BFS) for testing
    public static void printTree(TreeNode root) {
        if (root == null) {
            System.out.println("Empty Tree");
            return;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            if (current != null) {
                System.out.print(current.val + " ");
                queue.add(current.left);
                queue.add(current.right);
            } else {
                System.out.print("null ");
            }
        }
        System.out.println();
    }

    public static void main(String[] genealogy) {
        Main solver = new Main();

        /*
           Constructing a sample LeetCode binary tree:
                 4
               /   \
              2     7
             / \   / \
            1   3 6   9
        */
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2, new TreeNode(1), new TreeNode(3));
        root.right = new TreeNode(7, new TreeNode(6), new TreeNode(9));

        System.out.print("Original Tree (Level-order): ");
        printTree(root);

        // Invert the tree
        TreeNode invertedRoot = solver.invertTree(root);

        System.out.print("Inverted Tree (Level-order): ");
        printTree(invertedRoot);
        /*
           Expected Output Structure:
                 4
               /   \
              7     2
             / \   / \
            9   6 3   1
        */
    }
}

