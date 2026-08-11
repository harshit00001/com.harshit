package Practise;
import java.util.*;

import java.util.*;

public class BSFGraph {

    public static List<Integer> bfs(
            int V,
            ArrayList<ArrayList<Integer>> adj) {

       boolean[] visited = new boolean[V];
       Queue<Integer> que = new LinkedList<>();
       List<Integer> result = new ArrayList<>();
       visited[0]= true;
       que.offer(0);
       while(!que.isEmpty())
       {
           int del = que.poll();
           result.add(del);
           for(Integer in : adj.get(del))
           {
               if(!visited[in]) {
                   visited[in] = true;
                   que.offer(in);
               }
           }
       }
       return result;
    }

    public static void main(String[] args) {

        int V = 5;

        ArrayList<ArrayList<Integer>> adj =
                new ArrayList<>();

        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        // Add edges
        adj.get(0).add(1);
        adj.get(1).add(0);

        adj.get(0).add(2);
        adj.get(2).add(0);

        adj.get(1).add(3);
        adj.get(3).add(1);

        adj.get(2).add(4);
        adj.get(4).add(2);

        List<Integer> bfsTraversal = bfs(V, adj);

        System.out.println("BFS Traversal:");

        for (int node : bfsTraversal) {
            System.out.print(node + " ");
        }
    }
}

