import java.util.Scanner;

public class Task5RobloxPetSimulator {
    public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            String[] petNames = {
                    "Dog",
                    "Cat",
                    "Dragon",
                    "Unicorn",
                    "Huge Titanic Cat"
            };

            int[] petPowers = {15, 25, 120, 350, 2000};
            int[] dropChances = {45, 30, 15, 8, 2};

            String[] inventoryNames = new String[8];
            int[] inventoryPowers = new int[8];

            int petCount = 0;
            int coins = 300;
            final int EGG_PRICE = 100;

            int choice;

            System.out.println("======================================================");
            System.out.println("           ROBLOX: PET SIMULATOR");
            System.out.println("======================================================");

            do {
                System.out.println("\nБаланс: " + coins
                        + " монет | Вихованців у рюкзаку: "
                        + petCount + "/8");

                System.out.println("\n1 - Відкрити чарівне яйце (100 монет)");
                System.out.println("2 - Відкрити рюкзак-інвентар");
                System.out.println("3 - Порахувати силу команди");
                System.out.println("4 - Фармити монети (+50..150 монет)");
                System.out.println("0 - Вийти з гри");
                System.out.print("Ваш вибір: ");

                choice = scanner.nextInt();

                switch (choice) {

                    case 1:
                        if (coins < EGG_PRICE) {
                            System.out.println(
                                    "Недостатньо монет! Потрібно 100 монет.");
                            break;
                        }

                        if (petCount >= inventoryNames.length) {
                            System.out.println(
                                    "Рюкзак переповнений! "
                                            + "Продайте або видаліть зайвих петів.");
                            break;
                        }

                        int roll = (int) (Math.random() * 100) + 1;

                        int cumulativeChance = 0;
                        int petIndex = 0;

                        for (int i = 0; i < dropChances.length; i++) {
                            cumulativeChance += dropChances[i];

                            if (roll <= cumulativeChance) {
                                petIndex = i;
                                break;
                            }
                        }

                        inventoryNames[petCount] = petNames[petIndex];
                        inventoryPowers[petCount] = petPowers[petIndex];
                        petCount++;

                        coins -= EGG_PRICE;

                        System.out.println("\nЯйце розколюється...");

                        if (petIndex == 4) {
                            System.out.println(
                                    "⭐⭐⭐ НЕЙМОВІРНО! СУПЕР-ДЖЕКПОТ! ⭐⭐⭐");
                            System.out.println("Вам випав: "
                                    + petNames[petIndex]
                                    + "! (Сила: " + petPowers[petIndex] + ")");
                        } else {
                            System.out.println("Вам випав: "
                                    + petNames[petIndex]
                                    + "! (Сила: " + petPowers[petIndex] + ")");
                        }

                        System.out.println(
                                "Вихованця додано в інвентар!");
                        System.out.println("Залишок монет: " + coins);
                        break;

                    case 2:
                        System.out.println("\n--- ВАШ РЮКЗАК ---");

                        if (petCount == 0) {
                            System.out.println(
                                    "Рюкзак порожній. Відкрийте своє перше яйце!");
                        } else {
                            for (int i = 0; i < petCount; i++) {
                                System.out.println("[Слот " + (i + 1) + "] "
                                        + inventoryNames[i]
                                        + " (Сила: " + inventoryPowers[i] + ")");
                            }
                        }

                        System.out.println("Зайнято слотів: "
                                + petCount + "/8");
                        break;

                    case 3:
                        System.out.println(
                                "\n--- СТАТИСТИКА ВАШОЇ КОМАНДИ ---");

                        if (petCount == 0) {
                            System.out.println(
                                    "Команда порожня! Спочатку відкрийте яйце.");
                            break;
                        }

                        int totalPower = 0;
                        int bestPetIndex = 0;

                        for (int i = 0; i < petCount; i++) {
                            totalPower += inventoryPowers[i];

                            if (inventoryPowers[i]
                                    > inventoryPowers[bestPetIndex]) {
                                bestPetIndex = i;
                            }
                        }

                        System.out.println(
                                "Кількість вихованців: " + petCount + " шт.");
                        System.out.println(
                                "Загальна сила команди: "
                                        + totalPower + " очок шкоди");

                        System.out.println("Найсильніший улюбленець: "
                                + inventoryNames[bestPetIndex]
                                + " (Сила: "
                                + inventoryPowers[bestPetIndex] + ")");

                        break;

                    case 4:
                        int earnedCoins = (int) (Math.random() * 101) + 50;
                        coins += earnedCoins;

                        System.out.println("\nВи фармите монети на локації...");
                        System.out.println("Отримано +" + earnedCoins + " монет!");
                        System.out.println("Поточний баланс: "
                                + coins + " монет");
                        break;

                    case 0:
                        System.out.println("\nДякуємо за гру!");
                        System.out.println(
                                "До зустрічі у Roblox: Pet Simulator!");
                        break;

                    default:
                        System.out.println(
                                "Невірна команда! Введіть число від 0 до 4.");
                        break;
                }

            } while (choice != 0);

            scanner.close();
    }
        }
//ну я дуже хочу суші джава топ джава класс хто не вірить тому в глаз
