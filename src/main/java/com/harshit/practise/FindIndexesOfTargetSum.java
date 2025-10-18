package com.harshit.practise;

import java.util.HashMap;
import java.util.Map;

public class FindIndexesOfTargetSum {
    public static int[] intArray(int[] arr, int target)
    {
        Map<Integer,Integer> map = new HashMap<>();
        for(int i=0;i<arr.length;i++)
        {
            int count = target-arr[i];
            if(map.containsKey(count))
                return new int[]{map.get(count),i};
            map.put(arr[i],i);
        }
        return new int[]{};
    }

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8};
        int[] result =intArray(arr,7);
        for(int i:result){
            System.out.println(i+",");
        }

    }
}
