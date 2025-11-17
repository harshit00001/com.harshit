package com.harshit.leetcode.trees;

/**
 * Problem: Same Tree
 * 
 * Given the roots of two binary trees p and q, write a function to check if they are the same or not.
 * 
 * Two binary trees are considered the same if they are structurally identical, and the nodes
 * have the same value.
 * 
 * Example 1:
 * Input: p = [1,2,3], q = [1,2,3]
 * Output: true
 * 
 * Example 2:
 * Input: p = [1,2], q = [1,null,2]
 * Output: false
 * 
 * Example 3:
 * Input: p = [1,2,1], q = [1,1,2]
 * Output: false
 * 
 * Constraints:
 * - The number of nodes in both trees is in the range [0, 100].
 * - -10^4 <= Node.val <= 10^4
 */
public class SameTree {
    
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
     * Recursive solution (O(n) time, O(h) space)
     * 
     * @param p Root of first tree
     * @param q Root of second tree
     * @return true if trees are same, false otherwise
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        // Write your logic here
        // Hint: Check if both nodes are null (same)
        // Check if one is null (different)
        // Check if values are equal
        // Recursively check left and right subtrees
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
        SameTree solution = new SameTree();
        
        // Test case 1
        Integer[] arr1 = {1,2,3};
        Integer[] arr2 = {1,2,3};
        TreeNode p1 = createTree(arr1);
        TreeNode q1 = createTree(arr2);
        boolean result1 = solution.isSameTree(p1, q1);
        System.out.println("Test 1 - Input: p=[1,2,3], q=[1,2,3]");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        Integer[] arr3 = {1,2};
        Integer[] arr4 = {1,null,2};
        TreeNode p2 = createTree(arr3);
        TreeNode q2 = createTree(arr4);
        boolean result2 = solution.isSameTree(p2, q2);
        System.out.println("Test 2 - Input: p=[1,2], q=[1,null,2]");
        System.out.println("Expected: false, Got: " + result2);
    }
}

