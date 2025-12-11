import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class BaseConverter {

    private final int originalBase;
    private final int[] originalDigits;

    public BaseConverter(int originalBase, int[] originalDigits) {
        this.validateBase(originalBase);
        this.validateDigits(originalBase, originalDigits);

        this.originalBase = originalBase;
        this.originalDigits = originalDigits.clone();
    }

    public int[] convertToBase(int targetBase) {
        this.validateBase(targetBase);
        long decimalValue = this.convertDigitsToDecimal(this.originalDigits, this.originalBase);
        return decimalValue == 0
                       ? new int[]{0}
                       : this.convertDecimalToTargetBase(decimalValue, targetBase);
    }

    private void validateBase(int base) {
        if (base < 2) {
            throw new IllegalArgumentException("Bases must be at least 2.");
        }
    }

    private void validateDigits(int base, int[] digits) {
        if (digits == null) {
            throw new IllegalArgumentException("Digits array must not be null.");
        }

        IntStream.of(digits)
                .forEach(digit -> {
                    if (digit < 0) {
                        throw new IllegalArgumentException("Digits may not be negative.");
                    }
                    if (digit >= base) {
                        throw new IllegalArgumentException("All digits must be strictly less than the base.");
                    }
                });
    }

    private long convertDigitsToDecimal(int[] digits, int base) {
        return IntStream.range(0, digits.length)
                       .mapToLong(index -> this.computeDigitContribution(digits, base, index))
                       .sum();
    }

    private long computeDigitContribution(int[] digits, int base, int index) {
        return digits[index] * (long) Math.pow(base, digits.length - 1 - index);
    }

    private int[] convertDecimalToTargetBase(long value, int base) {
        return LongStream.iterate(value, current -> current > 0, current -> current / base)
                       .mapToInt(current -> this.getRemainder(base, current))
                       .collect(IntArrayCollector::new, IntArrayCollector::add, IntArrayCollector::combine)
                       .toReversedArray();
    }

    private int getRemainder(int base, long value) {
        return (int) (value % base);
    }
}
