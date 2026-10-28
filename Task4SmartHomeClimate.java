import java.util.Scanner;

public class Task4SmartHomeClimate {public static void main(String[] args)
    Scanner scanner = new Scanner(System.in);
    System.out.println("=== ПАНЕЛЬ КЛІМАТ-КОНТРОЛЮ SMART HOME ===");
    System.out.print("Поточна температура (°C): ");
    double currentTemp = scanner.nextDouble();
    System.out.print("Бажана температура (°C): ");
    double desiredTemp = scanner.nextDouble();
    System.out.print("Вологість повітря (%): ");
    int humidity = scanner.nextInt();
    System.out.print("Сезон (зима/літо/міжсезоння): ");
    String season = scanner.next();
    System.out.print("Режим будинку (1-Standby, 2-Eco, 3-Party, 4-Night, 5-Vacation): ");
    int mode = scanner.nextInt();
    System.out.print("Чи відчинені вікна? (true/false): ");
    boolean isWindowOpen = scanner.nextBoolean();
    System.out.println("\n------------- СТАТУС СИСТЕМИ -------------");
    if (isWindowOpen && mode != 5) {
        System.out.println("ПОПЕРЕДЖЕННЯ: Відчинені вікна! Обігрів та кондиціонування примусово заблоковано!");
        System.out.println("Стан терморегуляції:       ВИМКНЕНО (Безпека)");
        System.out.println("==========================================");
        scanner.close();
    } else {
        String modeName;
        double var10000;
        switch (mode) {
            case 1:
                modeName = "Standby (Звичайний)";
                var10000 = desiredTemp;
                break;
            case 2:
                modeName = "Eco (Енергозбереження)";
                double adjustment = (double)0.0F;
                if (season.equalsIgnoreCase("зима")) {
                    adjustment = (double)-2.0F;
                } else if (season.equalsIgnoreCase("літо")) {
                    adjustment = (double)2.0F;
                }

                var10000 = desiredTemp + adjustment;
                break;
            case 3:
                modeName = "Party (Гості)";
                var10000 = desiredTemp - (double)1.5F;
                break;
            case 4:
                modeName = "Night (Ніч)";
                var10000 = (double)19.0F;
                break;
            case 5:
                modeName = "Vacation (Відпустка)";
                var10000 = season.equalsIgnoreCase("зима") ? (double)12.0F : (double)28.0F;
                break;
            default:
                modeName = "Невідомий режим";
                var10000 = desiredTemp;
        }

        double effectiveTargetTemp = var10000;
        String climateStatus;
        if (currentTemp - effectiveTargetTemp > (double)0.5F) {
            climateStatus = "Кондиціонер УВІМКНЕНО (Охолодження)";
        } else if (effectiveTargetTemp - currentTemp > (double)0.5F) {
            climateStatus = "Опалення УВІМКНЕНО (Обігрів)";
        } else {
            climateStatus = "Температура в зоні комфорту (Кліматичні прилади в очікуванні)";
        }

        String humidityStatus = humidity < 40 ? "Зволожувач УВІМКНЕНО (вологість " + humidity + "% < 40%)" : (humidity > 65 ? "Осушувач УВІМКНЕНО (вологість " + humidity + "% > 65%)" : "Вологість оптимальна (" + humidity + "%)");
        System.out.printf("Режим роботи:              %s%n", modeName);
        System.out.printf("Цільова температура:       %.2f°C%n", effectiveTargetTemp);
        System.out.printf("Поточна температура:       %.2f°C%n", currentTemp);
        System.out.printf("Стан терморегуляції:       %s%n", climateStatus);
        System.out.printf("Стан мікроклімату:         %s%n", humidityStatus);
        System.out.println("Енергоспоживання:          Оптимізоване");
        System.out.println("==========================================");
        scanner.close();
    }
}
}
