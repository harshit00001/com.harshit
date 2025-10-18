package com.harshit.practise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KthLargest {
        public static int findKthLargest(int[] nums, int k) {
            List<Integer> list = new ArrayList<>();
            for(int num: nums)
            {
                list.add(num);
            }
            return quickSelect(list,k);
        }

        private static int quickSelect(List<Integer> ls , int k) {
            int random = new Random().nextInt(ls.size());
            int pivot = ls.get(random);
            List<Integer> less = new ArrayList<>();
            List<Integer> equal = new ArrayList<>();
            List<Integer> greater = new ArrayList<>();

            for(int num: ls)
            {
                if(pivot<num)
                {
                    greater.add(num);
                }
                else if(pivot==num)
                {
                    equal.add(num);
                }
                else if(pivot>num)
                {
                    less.add(num);
                }
            }
            if(k<=greater.size())
            {
                return quickSelect(greater,k);
            }
            if(k>greater.size() +equal.size())
            {
                return quickSelect(less,k-greater.size()-equal.size());
            }
            return pivot;
        }
    public static void main(String[] args) {
        int[] nums = {3, 7, 1, 5, 6, 8};
        int k = 2;

        int result = findKthLargest(nums, k);
        System.out.println("The " + k + "th largest element is: " + result);
    }
}
