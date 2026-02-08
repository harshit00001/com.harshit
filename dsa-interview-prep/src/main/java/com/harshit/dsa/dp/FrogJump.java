package com.harshit.dsa.dp;

import java.util.*;

public class FrogJump {
    public static boolean frogJump(int[] stones) {
//        int n = h.length;
//        int[] dp = new int[n];
//        Arrays.fill(dp, Integer.MAX_VALUE);
//        dp[0] = 0; // starting point
//
//        for (int i = 1; i < n; i++) {
//            dp[i] = Math.min(dp[i], dp[i-1] + Math.abs(h[i] - h[i-1]));
//            if (i > 1) {
//                dp[i] = Math.min(dp[i], dp[i-2] + Math.abs(h[i] - h[i-2]));
//            }
//        }
//        return dp[n-1];
//        int[] dp1 = new int[h.length];
//        0,1,3,5,7,10,14;
//        Arrays.fill(dp1,Integer.MAX_VALUE);
//        dp[0]=0;
//        for(int i=1;i<stones.length-1;i++)
//        {
//            dp[i] = stones[i] - stones[i-1];
//            if(dp[i]!=dp[i-1] || dp[i]!=dp[i-1]-1 || dp[i]!=dp[i-1]+1)
//            {
//                return false;
//            }
//        }
        int[] dp = new int[stones.length];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0]=0;
        dp[1]=1;
        for(int i=2;i<stones.length-1;i++)
        {
            dp[i] = stones[i] - stones[i-1];
            if(stones[i]==dp[i-1]+1 || stones[i]==dp[i-1] || stones[i]==dp[i-1]-1)
            {
                continue;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        int[] h = {0,1,3,5,6,8,12,17};
        System.out.println(frogJump(h)); // Output: 30
    }
}
