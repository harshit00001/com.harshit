package com.harshit.dsa.trees;

/**
 * Same Tree Problem
 * 
 * Problem: Check if two binary trees are identical.
 * 
 * Example:
 * Tree 1:     1          Tree 2:     1
 *            / \                    / \
 *           2   3                  2   3
 * Output: true
 * 
 * Approach: Recursive comparison
 * Time: O(n), Space: O(h)
 */
public class SameTree {
    
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        // Both null - same
        if (p == null && q == null) {
            return true;
        }
        
        // One null, one not - different
        if (p == null || q == null) {
            return false;
        }
        
        // Values must match, and subtrees must be same
        return p.val == q.val 
            && isSameTree(p.left, q.left) 
            && isSameTree(p.right, q.right);
    }
    
    public static void main(String[] args) {
        // Tree 1
        TreeNode tree1 = new TreeNode(1);
        tree1.left = new TreeNode(2);
        tree1.right = new TreeNode(3);
        
        // Tree 2
        TreeNode tree2 = new TreeNode(1);
        tree2.left = new TreeNode(2);
        tree2.right = new TreeNode(3);
        
        System.out.println("Are trees same? " + isSameTree(tree1, tree2));
        
        // Tree 3 (different)
        TreeNode tree3 = new TreeNode(1);
        tree3.left = new TreeNode(2);
        
        System.out.println("Are tree1 and tree3 same? " + isSameTree(tree1, tree3));
    }
}

