package task2;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int digitSum = 0;
        while (n != 0) {
            int lastDigit = n % 10;
            digitSum = digitSum + lastDigit;
            n = n / 10;
        }
        System.out.println("Сумма цифр равна - " + digitSum);
    }
}

