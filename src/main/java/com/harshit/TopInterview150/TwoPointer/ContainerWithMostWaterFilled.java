package com.harshit.TopInterview150.TwoPointer;

import java.util.Arrays;
/*
You are given an integer array height of length n.
There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).

Find two lines that together with the x-axis form a container,
such that the container contains the most water.

Return the maximum amount of water a container can store.
Notice that you may not slant the container.
 */
public class ContainerWithMostWaterFilled {
    public static int maxArea(int[] height) {
        int j= height.length-1;
        int i=0,max=0;
        while(i<j)
        {
            int height1=Math.min(height[i],height[j]);
            int width=j-i;
            int area=height1*width;
            max=Math.max(max,area);
            if(height[i]<height[j])
            {
                i++;
            }
            else
            {
                j--;
            }
        }
        return max;
    }
    public static void main(String[] args) {
        int[] num= {1,8,6,2,5,4,8,3,7};

        int num2 = maxArea(num);
        System.out.println(num2);
    }
}
