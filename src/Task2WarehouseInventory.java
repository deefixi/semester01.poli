public class Task2WarehouseInventory {
    public static void main(String[] args) {
    String[] items = {"Ноутбук", "Смартфон", "Бездротові навушники", "Механічна клавіатура", "Монітор 27\""};
    int[] quantities = {8, 3, 25, 4, 12};
    double[] prices = {28500.0, 19200.0, 1850.0, 3100.0, 9400.0};

    double[] totalValues = new double[items.length];
    double grandTotal = 0;

    double maxCapital = 0;
    String maxCapitalItem = "";

    System.out.println("=================== ЗВІТ СКЛАДУ ЕЛЕКТРОНІКИ ===================");
    System.out.println("№ | Назва товару            | К-сть | Ціна (грн) | Загальна вартість");
    System.out.println("----------------------------------------------------------------");

    for (int i = 0; i < items.length; i++) {
        totalValues[i] = quantities[i] * prices[i];
        grandTotal += totalValues[i];

        if (totalValues[i] > maxCapital) {
            maxCapital = totalValues[i];
            maxCapitalItem = items[i];
        }

        System.out.printf("%d | %-23s | %-5d | %-10.2f | %.2f грн%n",
                (i + 1), items[i], quantities[i], prices[i], totalValues[i]);
    }

    System.out.println("----------------------------------------------------------------");
    System.out.printf("Загальна вартість складу: %.2f грн%n", grandTotal);
    System.out.println("Найбільший капітал у товарі: " + maxCapitalItem + " (" + maxCapital + " грн)");

    System.out.println("\n🚨 ДЕФІЦИТНІ ПОЗИЦІЇ (менше 5 шт.):");
    boolean hasDeficit = false;
    for (int i = 0; i < items.length; i++) {
        if (quantities[i] < 5) {
            System.out.printf("- %s (залишилось: %d шт.) -> ⚠️ Терміново дозамовити!%n", items[i], quantities[i]);
            hasDeficit = true;
        }
    }
    if (!hasDeficit) {
        System.out.println("Дефіцитних позицій немає.");
    }
    System.out.println("================================================================");
    }
}
