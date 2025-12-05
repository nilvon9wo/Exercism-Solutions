import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record SumOfMultiples(int number, int[] set) {

    public int getSum() {
        if (set == null || set.length == 0 || number <= 0) {
            return 0;
        }

        Set<Integer> multiples = this.collectMultiples();
        return multiples.stream()
                       .mapToInt(Integer::intValue)
                       .sum();
    }

    private Set<Integer> collectMultiples() {
        return Arrays.stream(this.set)
                       .filter(number -> number != 0)
                       .flatMap(this::multiplesOf)
                       .boxed()
                       .collect(Collectors.toSet());
    }

    private IntStream multiplesOf(int factor) {
        return IntStream.range(1, this.number)
                       .map(i -> i * factor)
                       .filter(multiple -> multiple < this.number);
    }
}
