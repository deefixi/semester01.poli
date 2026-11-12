package semester1.practice10;
//РАМЕН БУДЕ МОЇМ!!!
import java.util.Arrays;

interface Evaluable1D {
    double EPSILON = 1e-7;
    double evaluate(double x);
}

interface Integrable1D extends Evaluable1D {
    default double integrateSimpson(double a, double b, int n) {
        if (n < 2) {
            n = 100;
        }
        if (n % 2 != 0) {
            n++;
        }

        double h = (b - a) / n;
        double sum = evaluate(a) + evaluate(b);

        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += (i % 2 == 0 ? 2.0 : 4.0) * evaluate(x);
        }
        return sum * h / 3.0;
    }
}

interface Differentiable1D extends Evaluable1D {
    double derivative(double x);

    default double numericalDerivative(double x) {
        double h = 1e-5;
        return (evaluate(x + h) - evaluate(x - h)) / (2.0 * h);
    }
}

interface NewtonRootFindable extends Differentiable1D {
    default double findRootNewton(double x0, int maxIter) {
        double x = x0;
        for (int i = 0; i < maxIter; i++) {
            double value = evaluate(x);
            if (Math.abs(value) <= EPSILON) {
                return x;
            }

            double slope = derivative(x);
            if (Math.abs(slope) < 1e-12 || !Double.isFinite(slope)) {
                return Double.NaN;
            }

            double nextX = x - value / slope;
            if (!Double.isFinite(nextX)) {
                return Double.NaN;
            }
            x = nextX;
        }
        return Math.abs(evaluate(x)) <= EPSILON ? x : Double.NaN;
    }
}
//Не люблю математику((
abstract class AbstractMathFunction implements Integrable1D {
    private final String formulaTitle;

    protected AbstractMathFunction(String formulaTitle) {
        this.formulaTitle = formulaTitle;
    }

    public final String getFormulaTitle() {
        return formulaTitle;
    }
}

final class PolynomialFunction extends AbstractMathFunction implements NewtonRootFindable {
    private final double[] coeffs;

    public PolynomialFunction(String title, double[] coeffs) {
        super(title);
        this.coeffs = Arrays.copyOf(coeffs, coeffs.length);
    }

    public double[] getCoefficientsCopy() {
        return Arrays.copyOf(coeffs, coeffs.length);
    }

    @Override
    public double evaluate(double x) {
        double result = 0.0;
        for (int i = coeffs.length - 1; i >= 0; i--) {
            result = result * x + coeffs[i];
        }
        return result;
    }

    @Override
    public double derivative(double x) {
        double result = 0.0;
        for (int i = coeffs.length - 1; i >= 1; i--) {
            result = result * x + i * coeffs[i];
        }
        return result;
    }
}

class DampedHarmonyFunction extends AbstractMathFunction implements NewtonRootFindable {
    private final double amplitude;
    private final double decayRate;
    private final double omega;

    public DampedHarmonyFunction(String title, double amplitude, double decayRate, double omega) {
        super(title);
        this.amplitude = amplitude;
        this.decayRate = decayRate;
        this.omega = omega;
    }

    @Override
    public double evaluate(double x) {
        return amplitude * Math.exp(-decayRate * x) * Math.cos(omega * x);
    }

    @Override
    public double derivative(double x) {
        return -amplitude * Math.exp(-decayRate * x)
                * (decayRate * Math.cos(omega * x) + omega * Math.sin(omega * x));
    }
}

class StepHeavisideFunction extends AbstractMathFunction {
    private final double threshold;
    private final double lowValue;
    private final double highValue;

    public StepHeavisideFunction(String title, double threshold, double lowValue, double highValue) {
        super(title);
        this.threshold = threshold;
        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    @Override
    public double evaluate(double x) {
        return x < threshold ? lowValue : highValue;
    }
}

public class Task3NumericalCalculusAndRootPolymorphism {

    public static void main(String[] args) {
        AbstractMathFunction[] functions = {
                new PolynomialFunction("P(x) = x^3 - 2x - 5", new double[]{-5.0, -2.0, 0.0, 1.0}),
                new DampedHarmonyFunction("D(x) = 4 * exp(-0.5x) * cos(2x)", 4.0, 0.5, 2.0),
                new StepHeavisideFunction("Step(x): 2 при x < 1.5; 5 при x >= 1.5", 1.5, 2.0, 5.0)
        };
//хочу чай
        System.out.println("================ ЧИСЕЛЬНИЙ АНАЛІЗ: ІНТЕРФЕЙСИ ТА ПОЛІМОРФІЗМ ================");
        System.out.println("Дослідження поліморфного масиву AbstractMathFunction[3] на [0.0; 3.0]:\n");

        for (int i = 0; i < functions.length; i++) {
            AbstractMathFunction fn = functions[i];
            int steps = (i == 2) ? 1000 : 100;

            System.out.println((i + 1) + ". Функція: " + fn.getFormulaTitle()
                    + " [" + fn.getClass().getSimpleName() + "]");
            System.out.printf("   -> Інтеграл Сімпсона на [0.0; 3.0] (N=%d): %.6f%n",
                    steps, fn.integrateSimpson(0.0, 3.0, steps));

            if (fn instanceof NewtonRootFindable rootFunction) {
                double point = (i == 0) ? 2.0 : 1.0;
                if (i == 1) {
                    point = 1.0;
                }
                System.out.printf("   -> Похідна в x=%.1f: аналітична = %.6f | чисельна = %.6f%n",
                        point, rootFunction.derivative(point), rootFunction.numericalDerivative(point));

                double initialGuess = (i == 0) ? 2.0 : 0.7;
                double root = rootFunction.findRootNewton(initialGuess, 100);
                if (Double.isNaN(root)) {
                    System.out.println("   -> Метод Ньютона: не вдалося знайти корінь із заданого наближення.");
                } else {
                    System.out.printf("   -> Корінь (старт x0=%.1f): x* = %.7f | f(x*) = %.2e%n",
                            initialGuess, root, fn.evaluate(root));
                }
            } else {
                System.out.println("   -> [Info] Функція розривна: не реалізує Differentiable1D / NewtonRootFindable.");
            }
            System.out.println();
        }
        System.out.println("=============================================================================");
    }
}
//суші з лососем дуже смачно
