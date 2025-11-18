package com.harshit.dsa.trees;

/**
 * Maximum Depth of Binary Tree
 * 
 * Problem: Find the maximum depth (height) of a binary tree.
 * 
 * Example:
 * Input:       3
 *             / \
 *            9  20
 *              /  \
 *             15   7
 * Output: 3
 * 
 * Approach: Recursive DFS
 * Time: O(n), Space: O(h) where h is height
 */
public class MaxDepthBinaryTree {
    
    /**
     * Recursive approach
     */
    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    public static void main(String[] args) {
        // Create tree:       3
        //                  /   \
        //                 9    20
        //                     /  \
        //                    15   7
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        
        System.out.println("Maximum Depth: " + maxDepth(root));
    }
}

