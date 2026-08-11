package Practise;

import java.util.*;

/**
Arrays requestIDs and timestamps are given, along with an integer timeWindow.

requestIDs[i] represents the ID of a request.
timestamps[i] represents the time (in seconds) when that request occurred.

A request ID is considered repeated within the window if:

It appears at least twice, and
There exist two occurrences whose time difference is ≤ timeWindow.

Your task is to count how many distinct request IDs satisfy this condition.
**/
public class FrequentRequestIDs {
        public static int countFrequentRequestIDs(String[] requestIDs, int[] timestamps, int timeWindow) {
            int count =0;
            List<Integer> ls = new ArrayList<>();
            ls.add(2);
            ls.add(2);
            Map<String, List<Integer>> map = new HashMap<>();
            for(int i=0;i<requestIDs.length;i++)
            {
                map.computeIfAbsent(requestIDs[i],k->new ArrayList<>()).add(timestamps[i]);
            }
            for(List<Integer> i : map.values())
            {
                Collections.sort(i);
                for(int j=1 ;j<i.size();j++)
                {
                    if(i.get(j)-i.get(j-1)<=timeWindow)
                    {
                        count++;
                        break;
                    }
                }
            }
        return count;
        }
    public static void main(String[] args) {
        String[] requestIDs   = {"A", "B", "A", "C", "B", "A", "D"};
        int[]    timestamps   = { 1,   2,   3,   10,  5,  5, 200};
        int      timeWindow   = 5;
        int result = countFrequentRequestIDs(requestIDs, timestamps, timeWindow);
        System.out.println("requestIDs : " + java.util.Arrays.toString(requestIDs));
        System.out.println("timestamps : " + java.util.Arrays.toString(timestamps));
        System.out.println("timeWindow : " + timeWindow);
        System.out.println("Frequent request IDs count = " + result);
    }
}
