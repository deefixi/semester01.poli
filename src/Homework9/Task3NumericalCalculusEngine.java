package semester1.practice09.homework;

import java.util.Arrays;

abstract class AbstractFunction1D {
    private final String expressionName;

    public AbstractFunction1D(String expressionName) {
        this.expressionName = expressionName != null ? expressionName : "f(x)";
    }

    public String getExpressionName() {
        return expressionName;
    }

    public abstract double evaluate(double x);
    public abstract double analyticDerivative(double x);

    public final double numericalDerivative(double x) {
        double h = 1e-5;
        return (evaluate(x + h) - evaluate(x - h)) / (2 * h);
    }

    public final double getDerivativeError(double x) {
        return Math.abs(analyticDerivative(x) - numericalDerivative(x));
    }

    public final double integrateTrapezoidal(double a, double b, int intervals) {
        int n = intervals > 0 ? intervals : 1000;
        double dx = (b - a) / n;
        double sum = 0.5 * (evaluate(a) + evaluate(b));

        for (int i = 1; i < n; i++) {
            sum += evaluate(a + i * dx);
        }
        return sum * dx;
    }

    @Override
    public String toString() {
        return expressionName;
    }
}

class PolynomialFunction extends AbstractFunction1D {
    private final double[] coeffs;

    public PolynomialFunction(double[] coeffs) {
        super(buildPolyName(coeffs));
        this.coeffs = (coeffs != null) ? Arrays.copyOf(coeffs, coeffs.length) : new double[]{0.0};
    }

    private static String buildPolyName(double[] c) {
        if (c == null || c.length == 0) return "0";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 0 && c.length > 1) continue;
            if (i > 0 && c[i] > 0) sb.append(" + ");
            else if (c[i] < 0) sb.append(" - ");
            else if (i > 0) sb.append(" ");

            sb.append(Math.abs(c[i]));
            if (i == 1) sb.append("*x");
            else if (i > 1) sb.append("*x^").append(i);
        }
        return sb.length() > 0 ? sb.toString() : "0";
    }

    public double[] getCoefficients() {
        return Arrays.copyOf(coeffs, coeffs.length);
    }

    @Override
    public double evaluate(double x) {
        double result = 0;
        double xPower = 1;
        for (double coeff : coeffs) {
            result += coeff * xPower;
            xPower *= x;
        }
        return result;
    }

    @Override
    public double analyticDerivative(double x) {
        double result = 0;
        double xPower = 1;
        for (int i = 1; i < coeffs.length; i++) {
            result += i * coeffs[i] * xPower;
            xPower *= x;
        }
        return result;
    }
}

class SineWaveFunction extends AbstractFunction1D {
    private final double amplitude;
    private final double omega;
    private final double phase;

    public SineWaveFunction(double amplitude, double omega, double phase) {
        super(String.format("%.2f*sin(%.2f*x + %.2f)", amplitude, omega, phase));
        this.amplitude = amplitude;
        this.omega = omega;
        this.phase = phase;
    }

    @Override
    public double evaluate(double x) {
        return amplitude * Math.sin(omega * x + phase);
    }

    @Override
    public double analyticDerivative(double x) {
        return amplitude * omega * Math.cos(omega * x + phase);
    }
}

class CompositeFunction1D extends AbstractFunction1D {
    private final AbstractFunction1D outer;
    private final AbstractFunction1D inner;

    public CompositeFunction1D(AbstractFunction1D outer, AbstractFunction1D inner) {
        super(String.format("%s ∘ %s", outer.getExpressionName(), inner.getExpressionName()));
        this.outer = outer;
        this.inner = inner;
    }

    @Override
    public double evaluate(double x) {
        return outer.evaluate(inner.evaluate(x));
    }

    @Override
    public double analyticDerivative(double x) {
        double innerVal = inner.evaluate(x);
        return outer.analyticDerivative(innerVal) * inner.analyticDerivative(x);
    }
}

public class Task3NumericalCalculusEngine {
    public static void main(String[] args) {
        System.out.println("================ ЛАБОРАТОРІЯ ЧИСЕЛЬНОГО АНАЛІЗУ ================");

        AbstractFunction1D g = new PolynomialFunction(new double[]{1.0, 0.0, 1.0});
        System.out.println("1. Многочлен g(x) = " + g.getExpressionName());
        System.out.printf("   Значення g(2.0) = %.2f\n", g.evaluate(2.0));
        System.out.printf("   Аналітична похідна g'(2.0) = %.6f | Чисельна = %.6f (Похибка: %.2e)\n",
                g.analyticDerivative(2.0), g.numericalDerivative(2.0), g.getDerivativeError(2.0));

        double integral = g.integrateTrapezoidal(0.0, 3.0, 1000);
        System.out.printf("   Інтеграл на [0.00; 3.00] (метод трапецій, N=1000): %.4f (Точне значення: 12.0)\n\n", integral);

        AbstractFunction1D f = new SineWaveFunction(2.0, 1.0, 0.0);
        System.out.println("2. Гармонічна функція f(u) = " + f.getExpressionName());
        double trigIntegral = f.integrateTrapezoidal(0.0, Math.PI, 1000);
        System.out.printf("   Інтеграл на [0.00; 3.1416] (N=1000): %.4f\n\n", trigIntegral);

        AbstractFunction1D H = new CompositeFunction1D(f, g);
        double xTest = 1.5;
        System.out.println("3. Суперпозиція (Композиція об'єктів) H(x) = f(g(x)) = 2*sin(x^2 + 1):");
        System.out.printf("   Точка x = %.2f:\n", xTest);
        System.out.printf("   -> H(%.2f) = %.6f\n", xTest, H.evaluate(xTest));
        System.out.printf("   -> Аналітична похідна H'(%.2f) за правилом ланцюга: %.6f\n", xTest, H.analyticDerivative(xTest));
        System.out.printf("   -> Чисельна похідна H'(%.2f) через кінцеву різницю: %.6f\n", xTest, H.numericalDerivative(xTest));
        System.out.printf("   -> Розбіжність між формулами: %.2e (ІДЕАЛЬНИЙ ЗБІГ!)\n", H.getDerivativeError(xTest));
        System.out.println("================================================================");
    }
}
