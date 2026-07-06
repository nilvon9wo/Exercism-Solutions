record NormalizedRational(int numerator, int denominator) {
    static NormalizedRational create(int numerator, int denominator) {
        int adjustedNumerator = numerator;
        int adjustedDenominator = denominator;

        if (adjustedDenominator < 0) {
            adjustedNumerator = -adjustedNumerator;
            adjustedDenominator = -adjustedDenominator;
        }

        if (adjustedNumerator == 0) {
            return new NormalizedRational(0, 1);
        }

        int divisor = greatestCommonDivisor(Math.abs(adjustedNumerator), adjustedDenominator);
        return new NormalizedRational(
                adjustedNumerator / divisor,
                adjustedDenominator / divisor);
    }


    private static int greatestCommonDivisor(int left, int right) {
        return right == 0
               ? left
               : greatestCommonDivisor(right, left % right);
    }
}
