import java.util.Scanner;
public class Task2DigitAnalyzer {
    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("=== АНАЛІЗАТОР РОЗРЯДІВ ЧИСЛА ===");
    System.out.print("Введіть додатне ціле число: ");
    int originalNumber = scanner.nextInt();

    if (originalNumber <= 0) {
        System.out.println("Помилка: Число повинно бути додатним!");
        scanner.close();
        return;
    }

    int temp = originalNumber;
    int digitCount = 0;
    int sumOfDigits = 0;
    int maxDigit = 0;
    int reversedNumber = 0;

    while (temp > 0) {
        int digit = temp % 10;
        digitCount++;
        sumOfDigits += digit;

        if (digit > maxDigit) {
            maxDigit = digit;
        }

        reversedNumber = reversedNumber * 10 + digit;
        temp /= 10;
    }

    boolean isPalindrome = (originalNumber == reversedNumber);

    System.out.println("\n--- СТАТИСТИКА ЧИСЛА ---");
    System.out.println("Початкове число:     " + originalNumber);
    System.out.println("Кількість цифр:      " + digitCount);
    System.out.println("Сума цифр:           " + sumOfDigits);
    System.out.println("Найбільша цифра:     " + maxDigit);
    System.out.println("Перевернуте число:   " + reversedNumber);

    if (isPalindrome) {
        System.out.println("Результат:           ✨ Це число є ПАЛІНДРОМОМ!");
    } else {
        System.out.println("Результат:           Це звичайне число (не паліндром).");
    }
    System.out.println("================================================");

    scanner.close();
    }
}
