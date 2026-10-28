import java.util.Scanner;

public class Task5BrawlStarsArena {
    public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("======================================================");
            System.out.println("           BRAWL STARS: BOSS FIGHT");
            System.out.println("======================================================");
            System.out.println("Оберіть свого бійця:");
            System.out.println("1. Шеллі (Shelly)    - HP: 4000 | Атака: 1200 | Супер: 2800");
            System.out.println("2. Кольт (Colt)      - HP: 3000 | Атака: 1600 | Супер: 3600");
            System.out.println("3. Ель Прімо (Primo) - HP: 6000 | Атака: 1100 | Супер: 2200");

            int choice;

            do {
                System.out.print("Ваш вибір (1-3): ");
                choice = scanner.nextInt();
            } while (choice < 1 || choice > 3);

            String playerName;
            int maxPlayerHp;
            int playerDamage;
            int superDamage;

            switch (choice) {
                case 1:
                    playerName = "Шеллі";
                    maxPlayerHp = 4000;
                    playerDamage = 1200;
                    superDamage = 2800;
                    break;
                case 2:
                    playerName = "Кольт";
                    maxPlayerHp = 3000;
                    playerDamage = 1600;
                    superDamage = 3600;
                    break;
                default:
                    playerName = "Ель Прімо";
                    maxPlayerHp = 6000;
                    playerDamage = 1100;
                    superDamage = 2200;
                    break;
            }

            int playerHp = maxPlayerHp;
            int bossMaxHp = 10000;
            int bossHp = bossMaxHp;
            int bossBaseDamage = 700;

            int ammo = 3;
            int superCharge = 0;
            int powerCubes = 0;
            int rounds = 0;

            boolean bossKnockedBack = false;
            boolean bossStunned = false;

            System.out.println("\nВи обрали бійця: " + playerName
                    + "! Бій починається!");

            while (playerHp > 0 && bossHp > 0) {
                rounds++;
                ammo = Math.min(3, ammo + 1);

                double bossPercent = (double) bossHp / bossMaxHp;
                int bossDamage = bossBaseDamage;
                String bossPhase;

                if (bossPercent < 0.30) {
                    bossDamage = (int) (bossBaseDamage * 1.7);
                    bossPhase = "ENRAGED / GOD MODE";
                } else if (bossPercent <= 0.60) {
                    bossDamage = (int) (bossBaseDamage * 1.3);
                    bossPhase = "ANGRY";
                } else {
                    bossPhase = "СПОКІЙНИЙ";
                }

                if (bossKnockedBack) {
                    bossDamage = (int) (bossDamage * 0.5);
                }

                System.out.println("\n-------------------- РАУНД "
                        + rounds + " --------------------");

                int playerBars = (int) (((double) playerHp
                        / maxPlayerHp) * 10);

                System.out.print("[" + playerName + "] HP: [");
                for (int i = 0; i < 10; i++) {
                    if (i < playerBars) {
                        System.out.print("█");
                    } else {
                        System.out.print("░");
                    }
                }

                System.out.println("] " + playerHp + "/" + maxPlayerHp
                        + " | Набої: " + ammo + "/3"
                        + " | Супер: " + superCharge + "%");

                int bossBars = (int) (((double) bossHp
                        / bossMaxHp) * 10);

                System.out.print("[Робо-Бос] HP: [");
                for (int i = 0; i < 10; i++) {
                    if (i < bossBars) {
                        System.out.print("█");
                    } else {
                        System.out.print("░");
                    }
                }

                System.out.println("] " + bossHp + "/" + bossMaxHp
                        + " | Фаза: " + bossPhase
                        + " | Шкода: " + bossDamage);

                System.out.println("Куби підсилення: " + powerCubes
                        + " шт. (+" + (powerCubes * 15) + "% до атаки)");

                System.out.println("\nОберіть дію:");
                System.out.println("1 - Базова атака (-1 набій)");
                System.out.println("2 - СУПЕР АТАКА");
                System.out.println("3 - Захисний маневр (+15% HP, +2 набої)");
                System.out.println("4 - Пошук Енергетичного куба");

                System.out.print("Ваш хід: ");
                int action = scanner.nextInt();

                int totalDamage = 0;

                switch (action) {
                    case 1:
                        if (ammo > 0) {
                            ammo--;

                            totalDamage = (int) (playerDamage
                                    * (1 + powerCubes * 0.15));

                            bossHp -= totalDamage;
                            superCharge += 35;

                            System.out.println("\n" + playerName
                                    + " атакує! Завдано "
                                    + totalDamage + " шкоди!");

                            System.out.println("Заряд супера: +35% "
                                    + "(поточний: " + superCharge + "%)");
                        } else {
                            System.out.println("Немає набоїв! Хід втрачено.");
                        }
                        break;

                    case 2:
                        if (superCharge >= 100) {
                            totalDamage = (int) (superDamage
                                    * (1 + powerCubes * 0.15));

                            bossHp -= totalDamage;
                            superCharge = 0;

                            System.out.println("\nСУПЕР-АТАКА!");
                            System.out.println(playerName
                                    + " завдає " + totalDamage + " шкоди!");

                            if (choice == 1) {
                                bossKnockedBack = true;
                                System.out.println("Боса відкинуто! "
                                        + "Його наступна атака слабша на 50%.");
                            } else if (choice == 3) {
                                bossStunned = true;
                                System.out.println("Боса оглушено! "
                                        + "Він пропустить наступний хід.");
                            }
                        } else {
                            System.out.println("Супер ще не заряджений! "
                                    + "Потрібно 100%, зараз: "
                                    + superCharge + "%.");
                        }
                        break;

                    case 3:
                        ammo = Math.min(3, ammo + 2);

                        int healing = (int) (maxPlayerHp * 0.15);
                        playerHp = Math.min(maxPlayerHp, playerHp + healing);

                        System.out.println("\n" + playerName
                                + " виконує захисний маневр!");
                        System.out.println("Відновлено здоров'я: "
                                + (playerHp > 0 ? healing : 0));
                        System.out.println("Набої: " + ammo + "/3");
                        break;

                    case 4:
                        if (Math.random() < 0.60) {
                            powerCubes++;
                            System.out.println("\nЗнайдено Power Cube!");
                            System.out.println("Загальна шкода збільшена на 15%!");
                        } else {
                            System.out.println("\nНа жаль, куб не знайдено.");
                        }
                        break;

                    default:
                        System.out.println("Невідома дія! Хід втрачено.");
                        break;
                }

                if (bossHp <= 0) {
                    bossHp = 0;
                    break;
                }

                System.out.println("\nХід Робо-Боса:");

                if (bossStunned) {
                    System.out.println("Бос оглушений і пропускає хід!");
                    bossStunned = false;
                } else {
                    if (Math.random() < 0.15) {
                        System.out.println(playerName
                                + " спритно ухиляється від удару!");
                    } else {
                        playerHp -= bossDamage;

                        if (playerHp < 0) {
                            playerHp = 0;
                        }

                        System.out.println("Робо-Бос атакує! "
                                + playerName + " отримує "
                                + bossDamage + " шкоди!");
                    }
                }

                bossKnockedBack = false;

                System.out.println("-------------------------------------------------");
            }

            //я хочу квіточки
            System.out.println("\n======================================================");

            if (playerHp > 0 && bossHp <= 0) {
                System.out.println("ПЕРЕМОГА! РОБО-БОСА ЗНИЩЕНО!");
                System.out.println("Раундів зіграно: " + rounds);
                System.out.println("Зібрано Power Cubes: " + powerCubes);
                System.out.println("Нагорода: +10 Трофеїв, 100 Starr Drops!");
                System.out.println("Ви - Зірковий Гравець!");
            } else {
                System.out.println("ПОРАЗКА! Сейф зруйновано!");
                System.out.println("Раундів зіграно: " + rounds);
                System.out.println("Спробуйте ще раз за Ель Прімо "
                        + "- у нього 6000 HP!");
            }

            System.out.println("======================================================");

            scanner.close();
        }
    }
