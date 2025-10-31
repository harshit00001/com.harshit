package com.harshit.TopInterview150.Tree;

class TreeNode
{
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int val)
    {
        this.val=val;
    }
    TreeNode(int val, TreeNode left, TreeNode right)
    {
        this.val=val;
        this.left=left;
        this.right=right;
    }
}
public class MaxDepthOfBinaryTree {
    private static int maxLen(TreeNode treeNode)
    {
        if(treeNode==null)
        return 0;
        int left = maxLen(treeNode.left)+1;
        int right = maxLen(treeNode.right)+1;
        return Math.max(left,right);

    }
    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(3);
        treeNode.left= new TreeNode(9);
        treeNode.right= new TreeNode(20);
        treeNode.right.left= new TreeNode(15);
        treeNode.right.right= new TreeNode(7);
        System.out.println(maxLen(treeNode));
    }
}
