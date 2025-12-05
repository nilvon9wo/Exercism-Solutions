import java.util.stream.IntStream;

public class PrimeCalculator {

	int nth(int nth) {
		this.validateIndex(nth);
		return this.findNthPrime(nth);
	}

	private void validateIndex(int nth) {
		if (nth < 1) {
			throw new IllegalArgumentException("nth must be >= 1");
		}
	}

	private int findNthPrime(int nth) {
		return IntStream.iterate(2, i -> i + 1)
				       .filter(this::isPrimeNumber)
				       .skip(nth - 1)
				       .findFirst()
				       .orElseThrow();
	}

	private boolean isPrimeNumber(int n) {
		return n > 1
		       && this.isPrime(n);
	}

	private boolean isPrime(int n) {
		int limit = (int) Math.sqrt(n);
		return IntStream.rangeClosed(2, limit)
				       .noneMatch(divisor -> n % divisor == 0);
	}
}
