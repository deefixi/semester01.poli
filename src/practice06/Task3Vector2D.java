class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        set(x, y);
    }

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
        double len = getLength();
        if (len > 0) {
            this.x /= len;
            this.y /= len;
        }
    }

    public void printVector() {
        System.out.printf("(%.2f; %.2f), Довжина |v| = %.2f%n", x, y, getLength());
    }
}

public class Task3Vector2D {
    public static void main(String[] args) {
        System.out.println("=== ВЕКТОРНИЙ АНАЛІЗАТОР (2D GEOMETRY) ===");

        Vector2D vecA = new Vector2D(3, 4);
        System.out.print("Вектор A: ");
        vecA.printVector();

        Vector2D vecB = new Vector2D(-4, 3);
        System.out.print("Вектор B: ");
        vecB.printVector();

        double dotAB = vecA.dotProduct(vecB.getX(), vecB.getY());
        System.out.printf("%nСкалярний добуток A · B: %.2f%n", dotAB);

        boolean isPerpendicular = vecA.isPerpendicularTo(vecB.getX(), vecB.getY());
        System.out.println("Чи є вектори перпендикулярними (кут 90°)? -> " + (isPerpendicular ? "ТАК (true)" : "НІ (false)"));

        System.out.println("\nНормалізація вектора A...");
        vecA.normalize();
        System.out.print("Одиничний вектор A: ");
        vecA.printVector();
        System.out.println("==========================================");
    }
}
