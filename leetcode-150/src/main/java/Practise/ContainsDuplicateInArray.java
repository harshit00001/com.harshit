package Practise;

import java.util.Arrays;
import java.util.HashSet;

public class ContainsDuplicateInArray {
    public static void main(String[] args) {
        int[] num1 = {1,2,3,4,5,6,7,2,5,7};
        int[] num2 = {1,2,3,4,5,6,7};

        System.out.println(extracted(num1));


    }

    private static Boolean extracted(int[] num1 ) {
//        HashSet<Integer> set= new HashSet<>();
//        for(int i: num1)
//        {
//            if(set.contains(i))
//                return true;
//            set.add(i);
//        }
//        return false;

        return Arrays.stream(num1).distinct().count() != num1.length;
    }
}
