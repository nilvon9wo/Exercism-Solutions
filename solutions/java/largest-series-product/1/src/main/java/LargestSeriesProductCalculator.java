import java.util.stream.IntStream;

class LargestSeriesProductCalculator {

	private final String inputNumber;

	LargestSeriesProductCalculator(String inputNumber) {
		if (inputNumber == null) {
			throw new IllegalArgumentException("String to search may only contain digits.");
		}

		boolean areNotAllDigits = !inputNumber.chars()
				                           .allMatch(Character::isDigit);
		if (areNotAllDigits) {
			throw new IllegalArgumentException("String to search may only contain digits.");
		}

		this.inputNumber = inputNumber;
	}

	long calculateLargestProductForSeriesLength(int numberOfDigits) {
		if (numberOfDigits < 0) {
			throw new IllegalArgumentException("Series length must be non-negative.");
		}

		if (numberOfDigits > inputNumber.length()) {
			throw new IllegalArgumentException(
					"Series length must be less than or equal to the length of the string to search."
			);
		}

		return numberOfDigits == 0
				       ? 1
				       : this.calculateLargestProduct(numberOfDigits);
	}

	private long calculateLargestProduct(int numberOfDigits) {
		return IntStream.rangeClosed(0, inputNumber.length() - numberOfDigits)
				       .mapToLong(i -> this.productFromIndex(numberOfDigits, i))
				       .max()
				       .orElse(0);
	}

	private long productFromIndex(int numberOfDigits, int i) {
		String slice = inputNumber.substring(i, i + numberOfDigits);
		return this.productOfDigits(slice);
	}

	private long productOfDigits(String digits) {
		return digits.chars()
				       .map(c -> c - '0')
				       .mapToLong(i -> i)
				       .reduce(1L, (a, b) -> a * b);
	}
}
