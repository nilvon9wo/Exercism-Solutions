import java.util.Objects;

public final class Rational {
    private final int numerator;
    private final int denominator;

    public Rational(int numerator, int denominator) {
        this(NormalizedRational.create(numerator, denominator));
    }

    private Rational(NormalizedRational normalized) {
        this.numerator = normalized.numerator();
        this.denominator = normalized.denominator();
    }

    Rational add(Rational other) {
        return new Rational(
                numerator * other.denominator + other.numerator * denominator,
                denominator * other.denominator
        );
    }

    Rational subtract(Rational other) {
        return add(other.negate());
    }

    Rational multiply(Rational other) {
        return new Rational(
                numerator * other.numerator,
                denominator * other.denominator
        );
    }

    Rational divide(Rational other) {
        return multiply(other.reciprocal());
    }

    Rational abs() {
        return new Rational(Math.abs(numerator), Math.abs(denominator));
    }

    Rational pow(int power) {
        return power == 0
               ? new Rational(1, 1)
               : power > 0
                     ? new Rational(integerPower(numerator, power), integerPower(denominator, power))
                     : reciprocal()
                            .pow(-power);
    }

    double exp(double base) {
        return Math.pow(base, (double) numerator / denominator);
    }

    private Rational negate() {
        return new Rational(-numerator, denominator);
    }

    private Rational reciprocal() {
        return new Rational(denominator, numerator);
    }

    private static int integerPower(int value, int power) {
        return (int) Math.round(Math.pow(value, power));
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Rational other
               && numerator == other.numerator
               && denominator == other.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    @Override
    public String toString() {
        return "%d/%d".formatted(numerator, denominator);
    }
}