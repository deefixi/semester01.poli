import java.util.Scanner;
public class Task3FibonacciSeries {public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("=== ЧИСЛА ФІБОНАЧЧІ ТА ЗОЛОТИЙ ПЕРЕТИН ===");
    System.out.print("Скільки чисел згенерувати (N >= 3): ");
    int n = scanner.nextInt();

    if (n < 3) {
        System.out.println("Помилка: N має бути не менше за 3!");
        scanner.close();
        return;
    }

    int f1 = 1;
    int f2 = 1;

    System.out.println("\n№ 1  | Число: 1");
    System.out.println("№ 2  | Число: 1");

    double lastRatio = 0.0;

    for (int i = 3; i <= n; i++) {
        int next = f1 + f2;
        double ratio = (double) next / f2;
        lastRatio = ratio;

        System.out.printf("№ %-2d | Число: %-10d | Відношення F(n)/F(n-1) = %.6f%n", i, next, ratio);

        f1 = f2;
        f2 = next;
    }

    double goldenRatioReference = 1.618034;

    System.out.println("\nПідсумок: знайдено наближення Золотого перетину: " + lastRatio);
    System.out.printf("Еталонне значення:                             %.6f%n", goldenRatioReference);
    System.out.println("=========================================================");

    scanner.close();
    }
}
