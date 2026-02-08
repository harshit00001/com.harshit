package Practise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SecondHighestNumber {
    public static void main(String[] args) {
        List<Integer> num1= Arrays.asList(23,14,56,73,96,35,74);
        Integer result=num1.stream().distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst()
                .orElseThrow(()-> new RuntimeException());
        System.out.println(result);
    }
}
