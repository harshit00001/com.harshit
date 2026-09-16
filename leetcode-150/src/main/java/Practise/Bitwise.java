package Practise;

public class Bitwise {
    public static void main(String[] args) {
        int n=1234;
        int sum=0;
        while(n>0)
        {
            int j = n%10;
            System.out.println(j);
            sum = sum*10 + j;
            System.out.println(sum);
            n=n/10;
        }
        System.out.println(sum);
    }
}
