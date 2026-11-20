package semester1.practice10.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task1WrapperAndAutoboxingMemory {

    public static void inspectIntegerCache(int[] testValues) {
        System.out.println("[1. ЕКСПЕРИМЕНТ: INTEGER CACHE [-128..127] (== проти .equals())]:");
        for (int val : testValues) {
            Integer first = Integer.valueOf(val);
            Integer second = Integer.valueOf(val);

            boolean sameReference = (first == second);
            boolean sameValue = first.equals(second);
            String source = (val >= -128 && val <= 127) ? "IntegerCache" : "Новий об'єкт у Heap!";

            System.out.printf("  Значення: %5d | (a == b): %-5b | a.equals(b): %-5b | Джерело: %s%n",
                    val, sameReference, sameValue, source);
        }
    }

    public static void demonstrateAutoboxingUnderTheHood() {
        System.out.println("\n[2. AUTOBOXING / UNBOXING ПІД КАПОТОМ ТА ЗАХИСТ ВІД NULL]:");

        Integer autoInt = 42;
        Integer manualInt = Integer.valueOf(42);
        int unboxedInt = manualInt.intValue();
        System.out.println("  Явне пакування: Integer.valueOf(42) -> " + manualInt
                + " | Розпакування: .intValue() -> " + unboxedInt);
        System.out.println("  Неявне і явне пакування дають однаковий результат: " + autoInt.equals(manualInt));

        Double autoDouble = 3.14;
        Double manualDouble = Double.valueOf(3.14);
        double unboxedDouble = manualDouble.doubleValue();
        System.out.println("  Double.valueOf(3.14) -> " + manualDouble + " | .doubleValue() -> " + unboxedDouble
                + " | однакові: " + autoDouble.equals(manualDouble));

        Boolean autoBool = true;
        Boolean manualBool = Boolean.valueOf(true);
        boolean unboxedBool = manualBool.booleanValue();
        System.out.println("  Boolean.valueOf(true) -> " + manualBool + " | .booleanValue() -> " + unboxedBool
                + " | однакові: " + autoBool.equals(manualBool));

        List<Integer> sensors = new ArrayList<>(Arrays.asList(45, null, -12, 88, null, 102));
        System.out.println("  Вхідний список із датчиків (з null): " + sensors);

        int sum = 0;
        for (int i = 0; i < sensors.size(); i++) {
            Integer value = sensors.get(i);
            if (value == null) {
                System.out.println("  ⚠️ Пропущено null-елемент на індексі " + i
                        + " (запобігання NullPointerException при intValue()!)");
                continue;
            }
            sum += value.intValue();
        }
        System.out.println("  Сума валідних розпакованих елементів: " + sum);
    }

    //я крокодил
    public static List<Integer> parseTelemetryStream(String[] rawPackets) {
        List<Integer> result = new ArrayList<>();
        for (String packet : rawPackets) {
            if (packet == null || packet.trim().isEmpty()) {
                System.out.println("  ❌ Порожній або null-пакет відхилено");
                continue;
            }
            String cleaned = packet.trim();
            try {
                int number = Integer.parseInt(cleaned);
                result.add(number);
            } catch (NumberFormatException e) {
                System.out.println("  ❌ Пакет '" + cleaned + "' відхилено (NumberFormatException)");
            }
        }
        return result;
    }

    public static void printExtremesAndStats(List<Integer> validData) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        long sum = 0; // я хочу спати

        System.out.println("  Початкові межі пошуку: min = " + min + " (MAX_VALUE), max = " + max + " (MIN_VALUE)");

        for (Integer value : validData) {
            int current = value; //макдоналдс очень вкусно
            if (current < min) {
                min = current;
            }
            if (current > max) {
                max = current;
            }
            sum += current;
        }

        System.out.println("  Успішно розпізнаний список Integer: " + validData);
        System.out.println("  Знайдений мінімум: " + min + " | Знайдений максимум: " + max);
        System.out.println("  Сума елементів: " + sum);

        List<Integer> byIndex = new ArrayList<>(validData);
        byIndex.remove(0); // тут число 0 - це ІНДЕКС, видалиться перший елемент
        System.out.println("  Після remove(0) (за індексом): " + byIndex);

        List<Integer> byValue = new ArrayList<>(validData);
        byValue.remove(Integer.valueOf(-30)); // видаляємо саме об'єкт зі значенням -30
        System.out.println("  Після remove(Integer.valueOf(-30)) (за значенням): " + byValue);
    }

    public static void main(String[] args) {
        System.out.println("================ ДОСЛІДЖЕННЯ WRAPPER CLASSES ТА INTEGER CACHE ================");

        int[] testValues = {-128, -50, 0, 127, 128, 500, 2026};
        inspectIntegerCache(testValues);

        demonstrateAutoboxingUnderTheHood();

        System.out.println("\n[3. ПАРСИНГ РЯДКІВ (Integer.parseInt) ТА ЕКСТРЕМУМИ (MAX_VALUE / MIN_VALUE)]:");
        String[] rawPackets = {"  145 ", "-30", "ERR_404", "980", "", null, "2147483647", "-85"};
        List<Integer> parsed = parseTelemetryStream(rawPackets);
        printExtremesAndStats(parsed);

        System.out.println("==============================================================================");
    }
}