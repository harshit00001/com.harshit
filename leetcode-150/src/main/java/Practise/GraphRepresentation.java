package Practise;

import java.util.*;

public class GraphRepresentation {

    public static void main(String[] args) {
        int v=4;
        ArrayList<ArrayList<Integer>> arr = new ArrayList<>();
        for(int i=0;i<v;i++)
        {
            arr.add(new ArrayList<>());
        }
        arr.get(0).add(1);
        arr.get(0).add(2);
        arr.get(1).add(3);
        arr.get(1).add(0);
        arr.get(2).add(0);
        arr.get(2).add(3);
        arr.get(3).add(1);
        arr.get(3).add(2);

        for(int i =0;i<v;i++)
        {
            ArrayList<Integer> arr2=arr.get(i);
            int size= arr.get(i).size();
            System.out.println("size of "+ i +" node: "+ size);
            System.out.print(i+" -> ");
            for(Integer j : arr2)
            {
                System.out.print(j+ " ");
            }
            System.out.println();
        }
    }
}
