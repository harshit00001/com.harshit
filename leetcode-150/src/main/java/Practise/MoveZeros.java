package Practise;

public class MoveZeros {
    public static void main(String[] args) {
        int[] arr = {1,5,0,9,2,0,6,0,8,4,0};
        int pos=0;
        for(int i:arr) {
            System.out.print(i+ " ");
        }
        for(int i=0;i<arr.length;i++)
        {
            if(arr[i]!=0) {
                arr[pos++] = arr[i];
            }
        }
        while(pos<arr.length)
        {
            arr[pos++]=0;
        }
        System.out.println();
        for(int i:arr) {
            System.out.print(i+ " ");
        }
    }
}
