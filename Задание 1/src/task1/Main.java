package task1;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[] a = {45, 10, -1, 40, 25, 30, -5, 8};
        Arrays.sort(a);
        for (int i = a.length - 3; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }
}
