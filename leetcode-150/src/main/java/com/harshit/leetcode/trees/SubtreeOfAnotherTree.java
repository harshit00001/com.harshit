package com.harshit.leetcode.trees;

/**
 * Problem: Subtree of Another Tree
 * 
 * Given the roots of two binary trees root and subRoot, return true if there is a subtree of root
 * with the same structure and node values of subRoot and false otherwise.
 * 
 * A subtree of a binary tree tree is a tree that consists of a node in tree and all of this node's
 * descendants. The tree tree could also be considered as a subtree of itself.
 * 
 * Example 1:
 * Input: root = [3,4,5,1,2], subRoot = [4,1,2]
 * Output: true
 * 
 * Example 2:
 * Input: root = [3,4,5,1,2,null,null,null,null,0], subRoot = [4,1,2]
 * Output: false
 * 
 * Constraints:
 * - The number of nodes in the root tree is in the range [1, 2000].
 * - The number of nodes in the subRoot tree is in the range [1, 1000].
 * - -10^4 <= root.val <= 10^4
 * - -10^4 <= subRoot.val <= 10^4
 */
public class SubtreeOfAnotherTree {
    
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
     * Solution using DFS (O(n*m) time, O(h) space)
     * 
     * @param root Main tree root
     * @param subRoot Subtree root
     * @return true if subRoot is subtree of root
     */
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        // Write your logic here
        // Hint: For each node in root, check if subtree starting from that node matches subRoot
        // Use helper function to check if two trees are identical
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
        SubtreeOfAnotherTree solution = new SubtreeOfAnotherTree();
        
        // Test case 1
        Integer[] arr1 = {3,4,5,1,2};
        Integer[] arr2 = {4,1,2};
        TreeNode root1 = createTree(arr1);
        TreeNode subRoot1 = createTree(arr2);
        boolean result1 = solution.isSubtree(root1, subRoot1);
        System.out.println("Test 1 - Input: root=[3,4,5,1,2], subRoot=[4,1,2]");
        System.out.println("Expected: true, Got: " + result1);
        
        // Test case 2
        Integer[] arr3 = {3,4,5,1,2,null,null,null,null,0};
        Integer[] arr4 = {4,1,2};
        TreeNode root2 = createTree(arr3);
        TreeNode subRoot2 = createTree(arr4);
        boolean result2 = solution.isSubtree(root2, subRoot2);
        System.out.println("Test 2 - Input: root=[3,4,5,1,2,null,null,null,null,0], subRoot=[4,1,2]");
        System.out.println("Expected: false, Got: " + result2);
    }
}

