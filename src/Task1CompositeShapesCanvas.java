package semester1.practice09.homework;

class Point2D {
    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D other) {
        if (other != null) {
            this.x = other.x;
            this.y = other.y;
        } else {
            this.x = 0.0;
            this.y = 0.0;
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public double distanceTo(Point2D other) {
        if (other == null) return 0.0;
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    @Override
    public String toString() {
        return String.format("(%.2f; %.2f)", x, y);
    }
}

abstract class AbstractShape2D {
    private final String name;
    private final Point2D anchorPoint;

    public AbstractShape2D(String name, Point2D anchorPoint) {
        this.name = (name != null) ? name : "UnknownShape";
        this.anchorPoint = (anchorPoint != null) ? new Point2D(anchorPoint) : new Point2D(0, 0);
    }

    public String getName() { return name; }

    public Point2D getAnchorPoint() {
        return new Point2D(anchorPoint);
    }

    public void moveBy(double dx, double dy) {
        this.anchorPoint.translate(dx, dy);
    }

    public abstract double getArea();
    public abstract double getPerimeter();

    @Override
    public String toString() {
        return String.format("center=%s | S=%.2f, P=%.2f", anchorPoint, getArea(), getPerimeter());
    }
}

class Circle2D extends AbstractShape2D {
    private final double radius;

    public Circle2D(String name, Point2D anchorPoint, double radius) {
        super(name, anchorPoint);
        this.radius = Math.max(0.0, radius);
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String toString() {
        return String.format("Circle2D[name='%s', %s, r=%.2f]", getName(), super.toString(), radius);
    }
}

class Rectangle2D extends AbstractShape2D {
    private final double width;
    private final double height;

    public Rectangle2D(String name, Point2D anchorPoint, double width, double height) {
        super(name, anchorPoint);
        this.width = Math.max(0.0, width);
        this.height = Math.max(0.0, height);
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }

    @Override
    public String toString() {
        return String.format("Rectangle2D[name='%s', %s, %.2fx%.2f]", getName(), super.toString(), width, height);
    }
}

class RightTriangle2D extends AbstractShape2D {
    private final double legA;
    private final double legB;

    public RightTriangle2D(String name, Point2D anchorPoint, double legA, double legB) {
        super(name, anchorPoint);
        this.legA = Math.max(0.0, legA);
        this.legB = Math.max(0.0, legB);
    }

    public double getHypotenuse() {
        return Math.sqrt(legA * legA + legB * legB);
    }

    @Override
    public double getArea() {
        return 0.5 * legA * legB;
    }

    @Override
    public double getPerimeter() {
        return legA + legB + getHypotenuse();
    }

    @Override
    public String toString() {
        return String.format("RightTriangle2D[name='%s', %s, a=%.2f, b=%.2f, c=%.2f]",
                getName(), super.toString(), legA, legB, getHypotenuse());
    }
}

class GeometryCanvas {
    private final String title;
    private final AbstractShape2D[] shapes;
    private int count;

    public GeometryCanvas(String title, int capacity) {
        this.title = (title != null) ? title : "Default Canvas";
        this.shapes = new AbstractShape2D[Math.max(1, capacity)];
        this.count = 0;
    }

    public boolean addShape(AbstractShape2D shape) {
        if (shape != null && count < shapes.length) {
            shapes[count++] = shape;
            return true;
        }
        return false;
    }

    public double getTotalArea() {
        double sum = 0;
        for (int i = 0; i < count; i++) {
            sum += shapes[i].getArea();
        }
        return sum;
    }

    public double getTotalPerimeter() {
        double sum = 0;
        for (int i = 0; i < count; i++) {
            sum += shapes[i].getPerimeter();
        }
        return sum;
    }

    public AbstractShape2D findLargestShape() {
        if (count == 0) return null;
        AbstractShape2D largest = shapes[0];
        for (int i = 1; i < count; i++) {
            if (shapes[i].getArea() > largest.getArea()) {
                largest = shapes[i];
            }
        }
        return largest;
    }

    public void moveAllShapes(double dx, double dy) {
        for (int i = 0; i < count; i++) {
            shapes[i].moveBy(dx, dy);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Полотно: '%s' (Фігур: %d/%d)\n", title, count, shapes.length));
        for (int i = 0; i < count; i++) {
            sb.append(String.format("  %d. %s\n", i + 1, shapes[i].toString()));
        }
        return sb.toString();
    }
}

public class Task1CompositeShapesCanvas {
    public static void main(String[] args) {
        System.out.println("================ ГЕОМЕТРИЧНЕ ПОЛОТНО (КОМПОЗИЦІЯ + ABSTRACT) ================");
        GeometryCanvas canvas = new GeometryCanvas("Інженерне креслення №1", 5);

        canvas.addShape(new Circle2D("Коло-А", new Point2D(0.0, 0.0), 5.0));
        canvas.addShape(new Rectangle2D("Плита-Б", new Point2D(10.0, 5.0), 4.0, 6.0));
        canvas.addShape(new RightTriangle2D("Клин-В", new Point2D(-3.0, 2.0), 3.0, 4.0));

        System.out.print(canvas);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("Сумарна площа креслення:    %.2f кв.од.\n", canvas.getTotalArea());
        System.out.printf("Сумарний периметр:          %.2f од.\n", canvas.getTotalPerimeter());

        AbstractShape2D largest = canvas.findLargestShape();
        if (largest != null) {
            System.out.printf("Найбільша фігура:           %s (S = %.2f)\n", largest.getName(), largest.getArea());
        }

        System.out.println("-> Переміщуємо всі фігури полотна на вектор (+5.0; -2.0)...");
        canvas.moveAllShapes(5.0, -2.0);

        System.out.println("Новий центр 'Коло-А': " + canvas.findLargestShape().getAnchorPoint());
        System.out.println("=============================================================================");
    }
}