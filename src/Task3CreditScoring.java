import java.util.Scanner;
public class Task3CreditScoring {public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("=== СИСТЕМА БАНКІВСЬКОГО СКОРИНГУ ===");
    System.out.print("Введіть вік позичальника: ");
    int age = scanner.nextInt();

    System.out.print("Введіть офіційний місячний дохід (грн): ");
    double monthlyIncome = scanner.nextDouble();

    System.out.print("Чи є негативна кредитна історія? (true/false): ");
    boolean hasBadCreditHistory = scanner.nextBoolean();

    System.out.print("Введіть запитувану суму кредиту (грн): ");
    double loanAmount = scanner.nextDouble();

    System.out.print("Введіть бажаний термін (місяців): ");
    int loanMonths = scanner.nextInt();
    boolean isAgeValid = (age >= 21 && age <= 65);
    boolean isCreditHistoryGood = !hasBadCreditHistory;
    boolean isPaymentAffordable = (loanMonths > 0) && ((loanAmount / loanMonths) <= (monthlyIncome * 0.5));
    double interestRate = (monthlyIncome >= 35000.0) ? 14.5 : 21.0;

    //Полінка привіііііііт)))
    boolean isApproved = isAgeValid && isCreditHistoryGood && isPaymentAffordable;

    System.out.println("\n------------- РІШЕННЯ СКОРИНГУ -------------");
    if (isApproved) {
        double monthlyPayment = loanAmount / loanMonths;
        double dtiPercent = (monthlyPayment / monthlyIncome) * 100.0;
        double maxAllowedPayment = monthlyIncome * 0.5;

        System.out.println("Статус заявки:               СХВАЛЕНО ✅");
        System.out.printf("Орієнтовний платіж/міс:      %.2f грн (%.1f%% від доходу)%n", monthlyPayment, dtiPercent);
        System.out.printf("Персональна ставка:          %.1f%% річних%n", interestRate);
        System.out.printf("Максимально допустимий платіж: %.2f грн/міс%n", maxAllowedPayment);
    } else {
        System.out.println("Статус заявки:               ВІДХИЛЕНО ❌");
        System.out.println("Причина(и) відмови:");
        if (!isAgeValid) {
            System.out.println(" - Відмова: вік не відповідає критеріям (21-65 років)");
        }
        if (!isCreditHistoryGood) {
            System.out.println(" - Відмова: виявлено негативну кредитну історію");
        }
        if (!isPaymentAffordable) {
            System.out.println(" - Відмова: термін кредиту некоректний або щомісячний платіж перевищує 50% доходу");
        }
    }
    System.out.println("=============================================");

    scanner.close();
    }
}
