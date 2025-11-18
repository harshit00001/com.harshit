package com.harshit.dsa.trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Tree Traversals - Most Important for Interviews!
 * 
 * Binary Tree Traversals:
 * 1. Inorder (Left, Root, Right) - For BST, gives sorted order
 * 2. Preorder (Root, Left, Right) - Used for copying trees
 * 3. Postorder (Left, Right, Root) - Used for deleting trees
 * 4. Level Order (BFS) - Level by level
 * 
 * Time: O(n) for all, Space: O(h) where h is height
 */
public class TreeTraversals {
    
    /**
     * Inorder Traversal - Recursive
     * Left -> Root -> Right
     */
    public static void inorderRecursive(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        
        inorderRecursive(root.left, result);
        result.add(root.val);
        inorderRecursive(root.right, result);
    }
    
    /**
     * Inorder Traversal - Iterative (using Stack)
     */
    public static List<Integer> inorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        
        while (current != null || !stack.isEmpty()) {
            // Go to leftmost node
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            
            // Process node
            current = stack.pop();
            result.add(current.val);
            
            // Move to right subtree
            current = current.right;
        }
        
        return result;
    }
    
    /**
     * Preorder Traversal - Recursive
     * Root -> Left -> Right
     */
    public static void preorderRecursive(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        
        result.add(root.val);
        preorderRecursive(root.left, result);
        preorderRecursive(root.right, result);
    }
    
    /**
     * Preorder Traversal - Iterative
     */
    public static List<Integer> preorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);
            
            // Push right first, then left (so left is processed first)
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        
        return result;
    }
    
    /**
     * Postorder Traversal - Recursive
     * Left -> Right -> Root
     */
    public static void postorderRecursive(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        
        postorderRecursive(root.left, result);
        postorderRecursive(root.right, result);
        result.add(root.val);
    }
    
    /**
     * Level Order Traversal (BFS)
     * Process nodes level by level
     */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.val);
                
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            
            result.add(currentLevel);
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        // Create tree:       1
        //                  /   \
        //                 2     3
        //                / \   / \
        //               4   5 6   7
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);
        
        System.out.println("Tree Structure:");
        System.out.println("       1");
        System.out.println("      / \\");
        System.out.println("     2   3");
        System.out.println("    / \\ / \\");
        System.out.println("   4  5 6  7\n");
        
        // Inorder
        List<Integer> inorder = new ArrayList<>();
        inorderRecursive(root, inorder);
        System.out.println("Inorder (Recursive): " + inorder);
        System.out.println("Inorder (Iterative): " + inorderIterative(root));
        
        // Preorder
        List<Integer> preorder = new ArrayList<>();
        preorderRecursive(root, preorder);
        System.out.println("Preorder (Recursive): " + preorder);
        System.out.println("Preorder (Iterative): " + preorderIterative(root));
        
        // Postorder
        List<Integer> postorder = new ArrayList<>();
        postorderRecursive(root, postorder);
        System.out.println("Postorder (Recursive): " + postorder);
        
        // Level Order
        System.out.println("Level Order: " + levelOrder(root));
    }
}

