import java.util.stream.IntStream;

class NaturalNumber {
	private final int number;

	NaturalNumber(int number) {
		if (number < 1) {
			throw new IllegalArgumentException("You must supply a natural number (positive integer)");
		}
		this.number = number;
	}

	private int aliquotSum = -1;
	private int getAliquotSum() {
		if (aliquotSum == -1) {
			int squareRoot = (int) Math.sqrt(number);
			aliquotSum = this.computeAliquotSum(squareRoot);
		}
		return aliquotSum;
	}

	private int computeAliquotSum(int squareRoot) {
		return IntStream.rangeClosed(1, squareRoot)
				       .filter(divisorCandidate -> number % divisorCandidate == 0)
				       .flatMap(this::divisorPairStream)
				       .sum();
	}

	private IntStream divisorPairStream(int divisorCandidate) {
		int pairedDivisor = number / divisorCandidate;
		return divisorCandidate == number
			       ? IntStream.empty()
			       : this.shouldReturnSingleDivisor(divisorCandidate, pairedDivisor)
		                    ? IntStream.of(divisorCandidate)
		                    : IntStream.of(divisorCandidate, pairedDivisor);
	}

	private boolean shouldReturnSingleDivisor(int divisorCandidate, int pairedDivisor) {
		return pairedDivisor == divisorCandidate
				       || pairedDivisor == number;
	}

	private Classification classification;
	Classification getClassification() {
		if (this.classification == null) {
			this.classification = Classification.fromNumberAndAliquotSum(number, getAliquotSum());
		}
		return this.classification;
	}
}
