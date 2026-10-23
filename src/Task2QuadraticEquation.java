class QuadraticEquation {
    private double a;
    private double b;
    private double c;

    public QuadraticEquation(double a, double b, double c) {
        setCoefficients(a, b, c);
    }

    public void setCoefficients(double a, double b, double c) {
        if (a == 0) {
            System.out.println("Увага: коефіцієнт 'a' не може дорівнювати 0! Встановлено a = 1.0.");
            this.a = 1.0;
        } else {
            this.a = a;
        }
        this.b = b;
        this.c = c;
    }

    public double getDiscriminant() {
        return (b * b) - (4 * a * c);
    }

    public boolean hasRealRoots() {
        return getDiscriminant() >= 0;
    }

    public double getRoot1() {
        double d = getDiscriminant();
        if (d < 0) return Double.NaN;
        return (-b + Math.sqrt(d)) / (2 * a);
    }

    public double getRoot2() {
        double d = getDiscriminant();
        if (d < 0) return Double.NaN;
        return (-b - Math.sqrt(d)) / (2 * a);
    }

    public void printSolution() {
        System.out.printf("Рівняння: %.1f*x^2 + (%.1f)*x + %.1f = 0%n", a, b, c);
        double d = getDiscriminant();
        System.out.printf("Дискримінант D = %.2f%n", d);

        if (d > 0) {
            System.out.println("Рівняння має 2 дійсні корені:");
            System.out.printf("  x1 = %.2f%n", getRoot1());
            System.out.printf("  x2 = %.2f%n", getRoot2());
        } else if (d == 0) {
            System.out.println("Рівняння має один дійсний корінь:");
            System.out.printf("  x = %.2f%n", getRoot1());
        } else {
            System.out.println("Рівняння не має дійсних коренів у множині R (D < 0).");
        }
    }
}

public class Task2QuadraticEquation {
    public static void main(String[] args) {
        System.out.println("=== РОЗВ'ЯЗУВАЧ КВАДРАТНИХ РІВНЯНЬ ===");

        System.out.println("Тест 1 (два корені):");
        QuadraticEquation eq1 = new QuadraticEquation(1, -5, 6);
        eq1.printSolution();

        System.out.println("\n--------------------------------------");
        System.out.println("Тест 2 (один корінь):");
        QuadraticEquation eq2 = new QuadraticEquation(1, -4, 4);
        eq2.printSolution();

        System.out.println("\n--------------------------------------");
        System.out.println("Тест 3 (без дійсних коренів):");
        QuadraticEquation eq3 = new QuadraticEquation(1, 2, 5);
        eq3.printSolution();
        System.out.println("======================================");
    }
}