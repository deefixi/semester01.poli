import java.util.Scanner;
public class Task1RangeStatistics {public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("=== АНАЛІЗАТОР ДІАПАЗОНУ ЧИСЕЛ ===");
    int n;
    do {
        System.out.print("Введіть число N (більше за 0): ");
        n = scanner.nextInt();
        if (n <= 0) {
            System.out.println("Помилка: N має бути більшим за 0!");
        }
    } while (n <= 0);

    long totalSum = 0;
    long evenSum = 0;
    long oddSum = 0;
    int evenCount = 0;
    int oddCount = 0;

    for (int i = 1; i <= n; i++) {
        totalSum += i;
        if (i % 2 == 0) {
            evenSum += i;
            evenCount++;
        } else {
            oddSum += i;
            oddCount++;
        }
    }
    double average = (double) totalSum / n;

    System.out.println("\n--- РЕЗУЛЬТАТИ ДЛЯ ДІАПАЗОНУ [1 .. " + n + "] ---");
    System.out.println("Загальна сума чисел:       " + totalSum);
    System.out.printf("Сума парних чисел:         %d (кількість: %d)%n", evenSum, evenCount);
    System.out.printf("Сума непарних чисел:       %d (кількість: %d)%n", oddSum, oddCount);
    System.out.printf("Середнє арифметичне:       %.2f%n", average);
    System.out.println("=========================================");

    scanner.close();
    }
}
