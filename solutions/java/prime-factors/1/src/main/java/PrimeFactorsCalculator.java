import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PrimeFactorsCalculator {
    List<Long> calculatePrimeFactorsOf(long number) {
        PrimeFactorsState seed = new PrimeFactorsState(number, 2L);
        return Stream.iterate(seed, PrimeFactorsState::hasMoreFactors, PrimeFactorsState::getNextState)
                       .filter(PrimeFactorsState::foundFactor)
                       .map(PrimeFactorsState::factor)
                       .collect(Collectors.toList());
    }
}
