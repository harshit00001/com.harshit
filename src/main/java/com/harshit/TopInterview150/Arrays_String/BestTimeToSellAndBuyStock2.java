package com.harshit.TopInterview150.Arrays_String;

/*
You are given an integer array prices where prices[i] is the price of a given stock on the ith day.
On each day, you may decide to buy and/or sell the stock.
You can only hold at most one share of the stock at any time.
However, you can sell and buy the stock multiple times on the same day,
ensuring you never hold more than one share of the stock.
Find and return the maximum profit you can achieve.

Example 1:

Input: prices = [7,1,5,3,6,4]
Output: 7You are given an integer array prices where prices[i] is the price of a given stock on the ith day.


 */
public class BestTimeToSellAndBuyStock2 {
    private static int maxProfit(int[] prices) {
        int price=0;
        for(int i=1;i<prices.length;i++)
        {
            if(prices[i-1]<prices[i])
                price=price+prices[i]-prices[i-1];
        }
        return price;
    }
    public static void main(String[] args) {
        int[] nums1 = {7,1,5,3,6,4};

        int num2 = maxProfit(nums1);
        System.out.println(num2);
    }
}
