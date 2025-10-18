package com.harshit.practise;
class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) {
        this.val = val;
    }
}
public class DeleteNodeInTree {
        public TreeNode deleteNode(TreeNode root, int key) {
            if (root == null) return null;

            if (key < root.val) {
                root.left = deleteNode(root.left, key);
            } else if (key > root.val) {
                root.right = deleteNode(root.right, key);
            } else {
                // Node found
                if (root.left == null) return root.right;
                if (root.right == null) return root.left;

                // Node has two children
                TreeNode successor = findMin(root.right);
                root.val = successor.val;
                root.right = deleteNode(root.right, successor.val);
            }
            return root;
        }

        private TreeNode findMin(TreeNode node) {
            while (node.left != null) node = node.left;
            return node;
        }

        // Helper: Insert node into BST
        public TreeNode insert(TreeNode root, int val) {
            if (root == null) return new TreeNode(val);
            if (val < root.val)
                root.left = insert(root.left, val);
            else
                root.right = insert(root.right, val);
            return root;
        }

        // Helper: In-order traversal
        public void inorder(TreeNode root) {
            if (root == null) return;
            inorder(root.left);
            System.out.print(root.val + " ");
            inorder(root.right);
        }

        public static void main(String[] args) {
            DeleteNodeInTree tree = new DeleteNodeInTree();
            TreeNode root = null;

            // Build BST
            int[] values = {30, 50, 70, 20, 40, 60, 80};
            for (int val : values) {
                root = tree.insert(root, val);
            }

            System.out.println("Original BST (in-order):");
            tree.inorder(root);
            System.out.println();

            // Delete node
            int keyToDelete = 50;
            root = tree.deleteNode(root, keyToDelete);

            System.out.println("BST after deleting " + keyToDelete + ":");
            tree.inorder(root);
            System.out.println();
        }
    }
