import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Sieve {
    private final int maximumPrime;

    Sieve(int maximumPrime) {
        this.maximumPrime = maximumPrime;
    }

    List<Integer> getPrimes() {
        return this.maximumPrime < 2
                       ? List.of()
                       : IntStream.rangeClosed(2, this.maximumPrime)
                               .filter(this::isPrime)
                               .boxed()
                               .collect(Collectors.toList());
    }

    private boolean isPrime(int candidateNumber) {
        int squareRootCandidate = (int) Math.sqrt(candidateNumber);
        return IntStream.rangeClosed(2, squareRootCandidate)
                       .allMatch(divisor -> candidateNumber % divisor != 0);
    }
}
