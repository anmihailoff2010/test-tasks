package task3;

// Java-программа для проверки, можно ли разбить массив на K групп равной суммы

import java.util.Random;
import java.util.Scanner;

public class Main {

// Рекурсивный метод для проверки равной суммы K в группах массива

    static boolean isPartitionPossibleRec(int arr[], int subsetSum[], boolean taken[], int subset, int K, int N, int curIdx, int limitIdx) {
        if (subsetSum[curIdx] == subset) {

            if (curIdx == K - 2)
                return true;

            // рекурсивный вызов для следующего поднабора

            return isPartitionPossibleRec(arr, subsetSum, taken, subset, K, N, curIdx + 1, N - 1);
        }

        // начать с limitIdx и включить элементы в текущий раздел

        for (int i = limitIdx; i >= 0; i--) {

            // если уже занято, продолжить

            if (taken[i])
                continue;
            int tmp = subsetSum[curIdx] + arr[i];

            // если temp меньше, чем подмножество, включить только элемент и вызываем рекурсивно

            if (tmp <= subset) {

                // помечаем элемент и включаем в текущий раздел сумму

                taken[i] = true;
                subsetSum[curIdx] += arr[i];
                boolean nxt = isPartitionPossibleRec(arr, subsetSum, taken, subset, K, N, curIdx, i - 1);

                // после рекурсивного вызова снимаем отметку с элемента и удаляем из суммы группы

                taken[i] = false;
                subsetSum[curIdx] -= arr[i];
                if (nxt)
                    return true;
            }
        }
        return false;
    }

// Метод возвращает true, если массив можно разбить на K групп с равной суммой

    static boolean isPartitionPossible(int arr[], int N, int K) {

        // Если K равен 1, то нашим ответом будет весь входной массив

        if (K == 1)
            return true;

        // Если общее количество групп больше N, то деление невозможно

        if (N < K)
            return false;

        // если сумма массива не делится на K без остатка, то мы не можем разделить массив на K групп

        int sum = 0;
        for (int i = 0; i < N; i++)
            sum += arr[i];
        if (sum % K != 0)
            return false;

        // сумма каждой группы должна быть равна (= сумма элементов всего массива / K)

        int subset = sum / K;
        int []subsetSum = new int[K];
        boolean []taken = new boolean[N];

        // Инициализируем сумму каждой группы от 0

        for (int i = 0; i < K; i++)
            subsetSum[i] = 0;

        // пометить все элементы как не занятые

        for (int i = 0; i < N; i++)
            taken[i] = false;

        // инициализировать сумму элементов первой группы как последний элемент массива и пометить это как принято

        subsetSum[0] = arr[N - 1];
        taken[N - 1] = true;

        // вызов рекурсивного метода для проверки условия K-замены

        return isPartitionPossibleRec(arr, subsetSum, taken, subset, K, N, 0, N - 1);
    }

    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        int numbersCount = scanner.nextInt();
        int groupsCount = scanner.nextInt();

        int arr[] = new int[numbersCount];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(100);
        }

        int N = arr.length;
        int K = groupsCount;

        if (isPartitionPossible(arr, N, K))
            System.out.println("Разделение на группы с равной суммой возможно");
        else
            System.out.println("Разделение на группы с равной суммой невозможно");
    }
}
