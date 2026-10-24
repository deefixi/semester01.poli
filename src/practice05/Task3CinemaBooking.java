import java.util.Scanner;
public class Task3CinemaBooking {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int rows = 5;
        int cols = 6;
        char[][] hall = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                hall[r][c] = 'O';
            }
        }

        int choice;
        do {
            System.out.println("\n=== ТЕРМІНАЛ КІНОТЕАТРУ: ЗАЛ №1 ===");
            System.out.println("1 - Показати схему залу");
            System.out.println("2 - Забронювати квиток");
            System.out.println("3 - Фінансова каса залу");
            System.out.println("0 - Завершити роботу");
            System.out.print("Ваш вибір: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("\n      1  2  3  4  5  6");
                    for (int r = 0; r < rows; r++) {
                        System.out.print("Р" + (r + 1) + ":  ");
                        for (int c = 0; c < cols; c++) {
                            System.out.print("[" + hall[r][c] + "]");
                        }
                        System.out.println();
                    }
                }
                case 2 -> {
                    System.out.print("Введіть номер ряду (1-5): ");
                    int rInput = scanner.nextInt();
                    System.out.print("Введіть номер місця (1-6): ");
                    int cInput = scanner.nextInt();

                    if (rInput < 1 || rInput > rows || cInput < 1 || cInput > cols) {
                        System.out.println("❌ Помилка: Некоректні координати рядка або місця!");
                    } else {
                        int rIdx = rInput - 1;
                        int cIdx = cInput - 1;

                        if (hall[rIdx][cIdx] == 'X') {
                            System.out.println("⚠️ Це місце вже зайняте!");
                        } else {
                            hall[rIdx][cIdx] = 'X';
                            System.out.printf("✅ Місце [Ряд %d, Місце %d] успішно заброньовано!%n", rInput, cInput);
                        }
                    }
                }
                case 3 -> {
                    int totalSeats = rows * cols;
                    int bookedSeats = 0;
                    int freeSeats = 0;
                    double ticketPrice = 180.0;

                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            if (hall[r][c] == 'X') {
                                bookedSeats++;
                            } else {
                                freeSeats++;
                            }
                        }
                    }

                    double occupancyRate = ((double) bookedSeats / totalSeats) * 100.0;
                    double totalRevenue = bookedSeats * ticketPrice;

                    System.out.println("\n--- СТАТИСТИКА ЗАЛУ ---");
                    System.out.println("Усього місць:       " + totalSeats);
                    System.out.println("Зайнятих місць:     " + bookedSeats);
                    System.out.println("Вільних місць:      " + freeSeats);
                    System.out.printf("Заповненість залу:  %.2f%%%n", occupancyRate);
                    System.out.printf("Загальна виручка:   %.2f грн%n", totalRevenue);
                    System.out.println("================-------");
                }
                case 0 -> System.out.println("Роботу термінала завершено. Гарного перегляду!");
                default -> System.out.println("❌ Невірний вибір. Спробуйте ще раз.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
