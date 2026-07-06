import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class PalindromeCalculator {
    SortedMap<Long, List<List<Integer>>> getPalindromeProductsWithFactors(int minFactor, int maxFactor) {
        validateInput(minFactor, maxFactor);
        return IntStream.rangeClosed(minFactor, maxFactor)
                        .boxed()
                        .flatMap(firstFactor
                                         -> this.generateFactorPairsForFirstFactor(maxFactor, firstFactor))
                        .filter(this::isPalindromeProduct)
                        .collect(this.groupFactorPairsByProduct());
    }

    private Stream<Map.Entry<Long, List<Integer>>> generateFactorPairsForFirstFactor(
            final int maxFactor,
            final Integer firstFactor
    ) {
        return IntStream.rangeClosed(firstFactor, maxFactor)
                        .mapToObj(secondFactor -> factorPairEntry(firstFactor, secondFactor));
    }

    private Collector<
                Map.Entry<Long, List<Integer>>,
                ?,
                TreeMap<Long, List<List<Integer>>>
            > groupFactorPairsByProduct() {
            return Collectors.groupingBy(
                    Map.Entry::getKey,
                    TreeMap::new,
                    Collectors.mapping(Map.Entry::getValue, Collectors.toList())
            );
        }

    private void validateInput(int minFactor, int maxFactor) {
        if (minFactor > maxFactor) {
            throw new IllegalArgumentException("invalid input: min must be <= max");
        }
    }

    private Map.Entry<Long, List<Integer>> factorPairEntry(int a, int b) {
        long product = (long) a * b;
        List<Integer> factors = List.of(a, b);
        return new AbstractMap.SimpleEntry<>(product, factors);
    }

    private boolean isPalindromeProduct(Map.Entry<Long, List<Integer>> entry) {
        return isPalindrome(entry.getKey());
    }

    private boolean isPalindrome(long value) {
        String s = Long.toString(value);
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }
}