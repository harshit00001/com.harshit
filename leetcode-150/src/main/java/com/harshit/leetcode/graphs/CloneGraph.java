package com.harshit.leetcode.graphs;

import java.util.*;

/**
 * Problem: Clone Graph
 * 
 * Given a reference of a node in a connected undirected graph.
 * 
 * Return a deep copy (clone) of the graph.
 * 
 * Each node in the graph contains a value (int) and a list (List[Node]) of its neighbors.
 * 
 * Test case format:
 * - For simplicity, each node's value is the same as the node's index (1-indexed).
 * - For example, the first node with val == 1, the second node with val == 2, and so on.
 * - The graph is represented in the test case using an adjacency list.
 * 
 * An adjacency list is a collection of unordered lists used to represent a finite graph.
 * Each list describes the set of neighbors of a node in the graph.
 * 
 * The given node will always be the first node with val = 1. You must return the copy of
 * the given node as a reference to the cloned graph.
 * 
 * Example 1:
 * Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
 * Output: [[2,4],[1,3],[2,4],[1,3]]
 * Explanation: There are 4 nodes in the graph.
 * 1st node (val = 1)'s neighbors are 2nd node (val = 2) and 4th node (val = 4).
 * 2nd node (val = 2)'s neighbors are 1st node (val = 1) and 3rd node (val = 3).
 * 3rd node (val = 3)'s neighbors are 2nd node (val = 2) and 4th node (val = 4).
 * 4th node (val = 4)'s neighbors are 1st node (val = 1) and 3rd node (val = 3).
 * 
 * Constraints:
 * - The number of nodes in the graph is in the range [0, 100].
 * - 1 <= Node.val <= 100
 * - Node.val is unique for each node.
 * - There are no repeated edges and no self-loops in the graph.
 * - The Graph is connected and all nodes can be visited starting from the given node.
 */
public class CloneGraph {
    
    /**
     * Definition for a Node
     */
    public static class Node {
        public int val;
        public List<Node> neighbors;
        public Node() {
            val = 0;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val, ArrayList<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }
    
    /**
     * Solution using DFS (O(n) time, O(n) space)
     * 
     * @param node Reference node of the graph
     * @return Cloned graph node
     */
    public Node cloneGraph(Node node) {
        // Write your logic here
        // Hint: Use HashMap to map original nodes to cloned nodes
        // DFS: Create clone, recursively clone neighbors
        return null;
    }
    
    /**
     * Solution using BFS (O(n) time, O(n) space)
     * 
     * @param node Reference node of the graph
     * @return Cloned graph node
     */
    public Node cloneGraphBFS(Node node) {
        // Write your logic here
        // Hint: Use queue for BFS, HashMap for mapping
        // Process nodes level by level
        return null;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        CloneGraph solution = new CloneGraph();
        
        // Test case 1: Simple graph
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        
        node1.neighbors.add(node2);
        node1.neighbors.add(node4);
        node2.neighbors.add(node1);
        node2.neighbors.add(node3);
        node3.neighbors.add(node2);
        node3.neighbors.add(node4);
        node4.neighbors.add(node1);
        node4.neighbors.add(node3);
        
        Node cloned = solution.cloneGraph(node1);
        System.out.println("Test 1 - Clone graph with 4 nodes");
        System.out.println("Cloned successfully: " + (cloned != null));
    }
}

