package com.harshit.leetcode.trees;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem: Maximum Depth of Binary Tree
 * 
 * Given the root of a binary tree, return its maximum depth.
 * 
 * A binary tree's maximum depth is the number of nodes along the longest path from the root
 * node down to the farthest leaf node.
 * 
 * Example 1:
 * Input: root = [3,9,20,null,null,15,7]
 * Output: 3
 * 
 * Example 2:
 * Input: root = [1,null,2]
 * Output: 2
 * 
 * Constraints:
 * - The number of nodes in the tree is in the range [0, 10^4].
 * - -100 <= Node.val <= 100
 */
public class MaximumDepthOfBinaryTree {
    
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
     * Recursive solution (O(n) time, O(h) space where h is height)
     * 
     * @param root Root of binary tree
     * @return Maximum depth of the tree
     */
    public int maxDepth(TreeNode root) {
        // Write your logic here
        // Hint: Base case: if root is null, return 0
        // Recursively find max depth of left and right subtrees
        // Return 1 + max(leftDepth, rightDepth)
        return 0;
    }
    
    /**
     * Iterative solution using BFS/Level Order (O(n) time, O(n) space)
     * 
     * @param root Root of binary tree
     * @return Maximum depth of the tree
     */
    public int maxDepthBFS(TreeNode root) {
        // Write your logic here
        // Hint: Use queue for level-order traversal
        // Count number of levels
        return 0;
    }
    
    /**
     * Helper method to create tree from array (for testing)
     */
    public static TreeNode createTree(Integer[] arr) {
        if (arr.length == 0 || arr[0] == null) return null;
        
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> queue = new LinkedList<>();
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
        MaximumDepthOfBinaryTree solution = new MaximumDepthOfBinaryTree();
        
        // Test case 1
        Integer[] arr1 = {3, 9, 20, null, null, 15, 7};
        TreeNode root1 = createTree(arr1);
        int result1 = solution.maxDepth(root1);
        System.out.println("Test 1 - Input: [3,9,20,null,null,15,7]");
        System.out.println("Expected: 3, Got: " + result1);
        
        // Test case 2
        Integer[] arr2 = {1, null, 2};
        TreeNode root2 = createTree(arr2);
        int result2 = solution.maxDepth(root2);
        System.out.println("Test 2 - Input: [1,null,2]");
        System.out.println("Expected: 2, Got: " + result2);
        
        // Test case 3
        Integer[] arr3 = {};
        TreeNode root3 = createTree(arr3);
        int result3 = solution.maxDepth(root3);
        System.out.println("Test 3 - Input: []");
        System.out.println("Expected: 0, Got: " + result3);
    }
}

