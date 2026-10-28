class Vector2D {
    private double x;
    private double y;

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public void multiplyByScalar(double k) {
        this.x *= k;
        this.y *= k;
    }
    public double dotProduct(double otherX, double otherY) {
        return this.x * otherX + this.y * otherY;
    }
    public boolean isPerpendicularTo(double otherX, double otherY) {
        double dot = dotProduct(otherX, otherY);
        return Math.abs(dot) < 1e-9;
    }

    public void normalize() {
        double length = getLength();
        if (length > 0) {
            this.x /= length;
            this.y /= length;
        } else {
            System.out.println("Неможливо нормалізувати нульовий вектор!");
        }
    }

    public void printVector() {
        System.out.printf("(%.2f; %.2f), Довжина |v| = %.2f\n", x, y, getLength());
    }
}
public class Task3Vector2D {
    public static void main(String[] args) {
        System.out.println("=== ВЕКТОРНИЙ АНАЛІЗАТОР (2D GEOMETRY) ===");

        Vector2D vectorA = new Vector2D();
        vectorA.set(3, 4);

        Vector2D vectorB = new Vector2D();
        vectorB.set(-4, 3);

        System.out.print("Вектор A: ");
        vectorA.printVector();

        System.out.print("Вектор B: ");
        vectorB.printVector();

        System.out.println();
        double dot = vectorA.dotProduct(vectorB.getX(), vectorB.getY());
        System.out.printf("Скалярний добуток A · B: %.2f\n", dot);

        boolean isPerp = vectorA.isPerpendicularTo(vectorB.getX(), vectorB.getY());
        System.out.println("Чи є вектори перпендикулярними (кут 90°)? -> " + (isPerp ? "ТАК (true)" : "НІ (false)"));

        System.out.println("\nНормалізація вектора A...");
        vectorA.normalize();

        System.out.print("Одиничний вектор A: ");
        vectorA.printVector();
        System.out.println("==========================================");
    }
}
