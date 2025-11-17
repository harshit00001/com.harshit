package com.harshit.leetcode.trees;

/**
 * Problem: Validate Binary Search Tree
 * 
 * Given the root of a binary tree, determine if it is a valid binary search tree (BST).
 * 
 * A valid BST is defined as follows:
 * - The left subtree of a node contains only nodes with keys less than the node's key.
 * - The right subtree of a node contains only nodes with keys greater than the node's key.
 * - Both the left and right subtrees must also be binary search trees.
 * 
 * Example 1:
 * Input: root = [2,1,3]
 * Output: true
 * 
 * Example 2:
 * Input: root = [5,1,4,null,null,3,6]
 * Output: false
 * Explanation: The root node's value is 5 but its right child's value is 4.
 * 
 * Constraints:
 * - The number of nodes in the tree is in the range [1, 10^4].
 * - -2^31 <= Node.val <= 2^31 - 1
 */
public class ValidateBinarySearchTree {
    
    /**
     * Definition for a binary tree node
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    /**
     * Solution using bounds (O(n) time, O(h) space)
     * 
     * @param root Root of binary tree
     * @return true if valid BST, false otherwise
     */
    public boolean isValidBST(TreeNode root) {
        // Write your logic here
        // Hint: Use min and max bounds for each node
        // Left child must be < node.val and > min
        // Right child must be > node.val and < max
        return false;
    }
    
    /**
     * Solution using inorder traversal (O(n) time, O(h) space)
     * 
     * @param root Root of binary tree
     * @return true if valid BST, false otherwise
     */
    public boolean isValidBSTInorder(TreeNode root) {
        // Write your logic here
        // Hint: Inorder traversal of BST gives sorted sequence
        // Check if each value is greater than previous
        return false;
    }
    
    /**
     * Helper method to create tree from array (for testing)
     */
    public static TreeNode createTree(Integer[] arr) {
        if (arr.length == 0 || arr[0] == null) return null;
        
        TreeNode root = new TreeNode(arr[0]);
        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        queue.offer(root);
        
        for (int i = 1; i < arr.length; i += 2) {
            TreeNode current = queue.poll();
            if (current == null) continue;
            
            if (i < arr.length && arr[i] != null) {
                current.left = new TreeNode(arr[i]);
                queue.offer(current.left);
            }
            if (i + 1 < arr.length && arr[i + 1] != null) {
                current.right = new TreeNode(arr[i + 1]);
                queue.offer(current.right);
            }
        }
        return root;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        ValidateBinarySearchTree solution = new ValidateBinarySearchTree();
        
        // Test case 1
        Integer[] arr1 = {2,1,3};
        TreeNode root1 = createTree(arr1);
        boolean result1 = solution.isValidBST(root1);
        System.out.println("Test 1 - Input: [2,1,3]");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        Integer[] arr2 = {5,1,4,null,null,3,6};
        TreeNode root2 = createTree(arr2);
        boolean result2 = solution.isValidBST(root2);
        System.out.println("Test 2 - Input: [5,1,4,null,null,3,6]");
        System.out.println("Expected: false, Got: " + result2);
    }
}

