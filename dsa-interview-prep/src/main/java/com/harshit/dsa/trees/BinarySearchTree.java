package com.harshit.dsa.trees;

/**
 * Binary Search Tree (BST) Operations
 * 
 * BST Property: For each node,
 * - All nodes in left subtree < node.val
 * - All nodes in right subtree > node.val
 * 
 * Operations:
 * - Search: O(h) where h is height (O(log n) for balanced)
 * - Insert: O(h)
 * - Delete: O(h)
 */
public class BinarySearchTree {
    
    /**
     * Search in BST
     * Time: O(h), Space: O(1) iterative, O(h) recursive
     */
    public static TreeNode searchBST(TreeNode root, int val) {
        while (root != null && root.val != val) {
            root = val < root.val ? root.left : root.right;
        }
        return root;
    }
    
    /**
     * Insert into BST
     * Time: O(h), Space: O(1) iterative, O(h) recursive
     */
    public static TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }
        
        if (val < root.val) {
            root.left = insertIntoBST(root.left, val);
        } else if (val > root.val) {
            root.right = insertIntoBST(root.right, val);
        }
        
        return root;
    }
    
    /**
     * Delete from BST
     * Time: O(h), Space: O(h)
     */
    public static TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) {
            return null;
        }
        
        if (key < root.val) {
            root.left = deleteNode(root.left, key);
        } else if (key > root.val) {
            root.right = deleteNode(root.right, key);
        } else {
            // Node to delete found
            
            // Case 1: No child
            if (root.left == null && root.right == null) {
                return null;
            }
            
            // Case 2: One child
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            
            // Case 3: Two children
            // Find inorder successor (smallest in right subtree)
            TreeNode successor = findMin(root.right);
            root.val = successor.val;
            root.right = deleteNode(root.right, successor.val);
        }
        
        return root;
    }
    
    /**
     * Find minimum value node
     */
    private static TreeNode findMin(TreeNode root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }
    
    /**
     * Validate BST
     * Check if tree is a valid BST
     */
    public static boolean isValidBST(TreeNode root) {
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    
    private static boolean isValidBST(TreeNode root, long min, long max) {
        if (root == null) {
            return true;
        }
        
        if (root.val <= min || root.val >= max) {
            return false;
        }
        
        return isValidBST(root.left, min, root.val) 
            && isValidBST(root.right, root.val, max);
    }
    
    public static void main(String[] args) {
        // Create BST:       4
        //                 /   \
        //                2     6
        //               / \   / \
        //              1   3 5   7
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);
        
        System.out.println("Is Valid BST: " + isValidBST(root));
        
        TreeNode found = searchBST(root, 5);
        System.out.println("Search 5: " + (found != null ? "Found" : "Not Found"));
        
        root = insertIntoBST(root, 8);
        System.out.println("After inserting 8, Is Valid BST: " + isValidBST(root));
    }
}

