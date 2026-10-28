class Fraction {
    private int numerator;
    private int denominator;

    public void setValues(int num, int den) {
        if (den == 0) {
            System.out.println("Помилка: знаменник не може дорівнювати 0!");
            den = 1;
        }
        if (den < 0) {
            num = -num;
            den = -den;
        }
        this.numerator = num;
        this.denominator = den;
    }

    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public void simplify() {
        int g = gcd(numerator, denominator);
        numerator /= g;
        denominator /= g;
    }

    public double toDecimal() {
        return (double) numerator / denominator;
    }

    public void add(int otherNum, int otherDen) {
        this.numerator = this.numerator * otherDen + otherNum * this.denominator;
        this.denominator = this.denominator * otherDen;
        simplify();
    }

    public void printFraction() {
        if (denominator == 1) {
            System.out.println(numerator);
        } else {
            System.out.println(numerator + "/" + denominator);
        }
    }
}
//я наруто удзумаке я хокаге.
public class Task1Fraction {
    public static void main(String[] args) {
        System.out.println("=== КАЛЬКУЛЯТОР ЗВИЧАЙНИХ ДРОБІВ ===");

        Fraction f1 = new Fraction();
        f1.setValues(8, 12);

        System.out.print("Дріб 1: ");
        f1.printFraction();

        f1.simplify();
        System.out.printf("Після скорочення: %s (десятковий вигляд: %.4f)\n",
                f1.toDecimal() == 1 ? "1" : (8/4) + "/" + (12/4), f1.toDecimal());

        System.out.println("\nДодаємо дріб 3/4 до 2/3...");
        System.out.println("Формула: 2/3 + 3/4 = (2*4 + 3*3) / (3*4) = 17/12");

        f1.add(3, 4);
        System.out.print("Результат додавання: ");
        f1.printFraction();
        System.out.printf("Десятковий вигляд: %.4f\n", f1.toDecimal());
        System.out.println("=====================================");
    }
}