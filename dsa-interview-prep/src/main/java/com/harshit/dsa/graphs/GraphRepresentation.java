package com.harshit.dsa.graphs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Graph Representation and Basic Traversals
 * 
 * Graph can be represented as:
 * 1. Adjacency List (most common) - List of lists
 * 2. Adjacency Matrix - 2D array
 * 
 * Traversals:
 * - BFS (Breadth-First Search): Level by level, uses Queue
 * - DFS (Depth-First Search): Go deep, uses Stack/Recursion
 */
public class GraphRepresentation {
    
    private int vertices;
    private List<List<Integer>> adjList;
    
    public GraphRepresentation(int vertices) {
        this.vertices = vertices;
        this.adjList = new ArrayList<>();
        
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }
    
    /**
     * Add edge (undirected graph)
     */
    public void addEdge(int src, int dest) {
        adjList.get(src).add(dest);
        adjList.get(dest).add(src); // For undirected graph
    }
    
    /**
     * BFS Traversal
     * Time: O(V + E), Space: O(V)
     */
    public void bfs(int start) {
        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();
        
        visited[start] = true;
        queue.offer(start);
        
        System.out.print("BFS from " + start + ": ");
        
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            System.out.print(vertex + " ");
            
            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        System.out.println();
    }
    
    /**
     * DFS Traversal (Recursive)
     * Time: O(V + E), Space: O(V)
     */
    public void dfs(int start) {
        boolean[] visited = new boolean[vertices];
        System.out.print("DFS from " + start + ": ");
        dfsUtil(start, visited);
        System.out.println();
    }
    
    private void dfsUtil(int vertex, boolean[] visited) {
        visited[vertex] = true;
        System.out.print(vertex + " ");
        
        for (int neighbor : adjList.get(vertex)) {
            if (!visited[neighbor]) {
                dfsUtil(neighbor, visited);
            }
        }
    }
    
    /**
     * DFS Traversal (Iterative using Stack)
     */
    public void dfsIterative(int start) {
        boolean[] visited = new boolean[vertices];
        java.util.Stack<Integer> stack = new java.util.Stack<>();
        
        stack.push(start);
        System.out.print("DFS (Iterative) from " + start + ": ");
        
        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            
            if (!visited[vertex]) {
                visited[vertex] = true;
                System.out.print(vertex + " ");
                
                // Push neighbors in reverse order to maintain same order as recursive
                for (int i = adjList.get(vertex).size() - 1; i >= 0; i--) {
                    int neighbor = adjList.get(vertex).get(i);
                    if (!visited[neighbor]) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        // Create graph:
        //     0
        //   /   \
        //  1     2
        //  |     |
        //  3 --- 4
        
        GraphRepresentation graph = new GraphRepresentation(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
        
        graph.bfs(0);
        graph.dfs(0);
        graph.dfsIterative(0);
    }
}

