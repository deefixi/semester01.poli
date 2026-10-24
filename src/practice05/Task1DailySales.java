import java.util.Scanner;
public class Task1DailySales {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] days = {"Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота", "Неділя"};
        double[] sales = new double[7];

        System.out.println("=== ФІНАНСОВИЙ ЗВІТ КАВ'ЯРНІ ЗА ТИЖДЕНЬ ===");

        for (int i = 0; i < days.length; i++) {
            System.out.print("Введіть виторг за " + days[i] + " (грн): ");
            sales[i] = scanner.nextDouble();
        }

        double totalSales = 0;
        double maxSale = sales[0];
        double minSale = sales[0];
        String maxDay = days[0];
        String minDay = days[0];

        for (int i = 0; i < sales.length; i++) {
            totalSales += sales[i];

            if (sales[i] > maxSale) {
                maxSale = sales[i];
                maxDay = days[i];
            }

            if (sales[i] < minSale) {
                minSale = sales[i];
                minDay = days[i];
            }
        }

        double averageSale = totalSales / sales.length;

        System.out.println("\n---------------- ПІДСУМКИ ----------------");
        System.out.printf("Загальний тижневий виторг: %.2f грн%n", totalSales);
        System.out.printf("Середній виторг на день:    %.2f грн%n", averageSale);
        System.out.println("Найкращий день:            " + maxDay + " (" + maxSale + " грн)");
        System.out.println("Найгірший день:            " + minDay + " (" + minSale + " грн)");

        System.out.println("\nДні з виторгом вище середнього:");
        for (int i = 0; i < sales.length; i++) {
            if (sales[i] > averageSale) {
                System.out.printf("- %s: %.2f грн%n", days[i], sales[i]);
            }
        }
        System.out.println("==========================================");

        scanner.close();
    }
}
