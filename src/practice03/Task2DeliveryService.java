import java.util.Scanner;
public class Task2DeliveryService {public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("=== ТАРИФІКАТОР СЛУЖБИ ДОСТАВКИ ===");
    System.out.print("Введіть вагу відправлення (кг): ");
    double weight = scanner.nextDouble();

    System.out.print("Введіть відстань транспортування (км): ");
    int distance = scanner.nextInt();

    if (weight <= 0 || weight > 50.0 || distance <= 0) {
        System.out.println("ПОМИЛКА: Вага повинна бути від 0 до 50 кг, а відстань — більша за 0 км!");
        scanner.close();
        return;
    }

    System.out.print("Оберіть пункт призначення (1 - Відділення, 2 - Поштомат, 3 - Кур'єр): ");
    int deliveryType = scanner.nextInt();
    scanner.nextLine();

    System.out.print("Чи є у вас карта Premium? (так/ні): ");
    String isPremiumStr = scanner.nextLine().trim();

    double baseTariff = switch (deliveryType) {
        case 1 -> 50.0;
        case 2 -> {
            if (weight > 15.0) {
                System.out.println("ПОМИЛКА: Поштомат не приймає габаритні посилки понад 15 кг!");
                yield -1.0;
            } else {
                yield 60.0;
            }
        }
        case 3 -> 100.0;
        default -> -1.0;
    };

    if (baseTariff < 0) {
        System.out.println("Розрахунок скасовано через помилку вхідних даних або обмеження тарифу.");
        scanner.close();
        return;
    }

    double extraDistanceFee;
    if (distance <= 50) {
        extraDistanceFee = 0.0;
    } else if (distance <= 200) {
        extraDistanceFee = 35.0;
    } else {
        extraDistanceFee = 80.0;
    }

    double totalBeforeDiscount = baseTariff + extraDistanceFee;
    boolean isPremium = isPremiumStr.equalsIgnoreCase("так");
    double finalTotal = isPremium ? totalBeforeDiscount * 0.80 : totalBeforeDiscount;

    String deliveryTypeName = switch (deliveryType) {
        case 1 -> "Відділення";
        case 2 -> "Поштомат";
        case 3 -> "Кур'єр";
        default -> "Невідомо";
    };

    System.out.println("\n------------- НАКЛАДНА ДОСТАВКИ -------------");
    System.out.printf("Тип доставки:              %s (базовий тариф: %.2f грн)%n", deliveryTypeName, baseTariff);
    System.out.printf("Доплата за відстань:       %.2f грн (%d км)%n", extraDistanceFee, distance);
    System.out.printf("Сума до знижки:            %.2f грн%n", totalBeforeDiscount);
    System.out.printf("Статус клієнта:            %s%n", isPremium ? "Premium (-20%)" : "Звичайний (без знижки)");
    System.out.println("---------------------------------------------");
    System.out.printf("РАЗОМ ДО СПЛАТИ:           %.2f грн%n", finalTotal);
    System.out.println("=============================================");

    scanner.close();
    }
}
