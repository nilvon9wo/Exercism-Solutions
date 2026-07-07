import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class TripletListBuilder {
    private int sum;
    private int maxFactor = Integer.MAX_VALUE;
    TripletListBuilder thatSumTo(int sum) {
        this.sum = sum;
        return this;
    }

    TripletListBuilder withFactorsLessThanOrEqualTo(int maxFactor) {
        this.maxFactor = maxFactor;
        return this;
    }

    List<PythagoreanTriplet> build() {
        int maximumA = Math.min(maxFactor, sum);
        return IntStream.rangeClosed(1, maximumA)
                        .mapToObj(this::createTriplet)
                        .flatMap(triplet -> triplet)
                        .toList();
    }

    private Stream<PythagoreanTriplet> createTriplet(int a) {
        int numerator = (sum * sum) - (2 * sum * a);
        int denominator = (2 * sum) - (2 * a);
        if (denominator == 0 || numerator % denominator != 0) {
            return Stream.empty();
        }

        int b = numerator / denominator;
        int c = sum - a - b;
        return isValid(a, b, c)
               ? Stream.of(new PythagoreanTriplet(a, b, c))
               : Stream.empty();
    }

    private boolean isValid(int a, int b, int c) {
        return a < b && b < c
               && c <= maxFactor
               && (long) a * a + (long) b * b == (long) c * c;
    }
}
