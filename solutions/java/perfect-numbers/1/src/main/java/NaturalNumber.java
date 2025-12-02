import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

class NaturalNumber {
	private final int number;

	NaturalNumber(int number) {
		if (number < 1) {
			throw new IllegalArgumentException("You must supply a natural number (positive integer)");
		}
        this.number = number;
    }

	private static final Map<Integer, Classification> CLASSIFICATION_BY_COMPARISON_RESULT
			= Map.of(
					-1, Classification.ABUNDANT,
					0, Classification.PERFECT,
					1, Classification.DEFICIENT
			);

	private int aliquotSum;
	private int getAliquotSum() {
		if (aliquotSum == 0) {
			int squareRoot = (int) Math.sqrt(number);
			aliquotSum = this.getAliquotSum(squareRoot);
		}
		return aliquotSum;
	}

	private int getAliquotSum(int squareRoot) {
		return IntStream.rangeClosed(1, squareRoot)
				       .filter(i -> number % i == 0)
				       .flatMap(this::divisorPairStream)
				       .sum();
	}

	private IntStream divisorPairStream(int i) {
		int other = number / i;
		Set<Integer> selfDivisors = Set.of(number, i);
		return i == number
				       ? IntStream.empty()
				       : selfDivisors.contains(other)
						         ? IntStream.of(i)
						         : IntStream.of(i, other);
	}

	private Classification classification;
    Classification getClassification() {
		if (this.classification == null) {
			int compareResult = Integer.compare(number, this.getAliquotSum());
			this.classification	= CLASSIFICATION_BY_COMPARISON_RESULT.get(compareResult);
		}
        return this.classification;
    }
}
