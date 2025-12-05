import java.util.stream.IntStream;

public class LuhnLogic {
	public int computeLuhnSum(String digits) {
		final int length = digits.length();
		return IntStream.range(0, length)
				       .map(i -> this.convertDigit(digits, length, i))
				       .sum();
	}

	private int convertDigit(String digits, int length, int indexFromLeft) {
		int indexFromRight = length - indexFromLeft - 1;
		int digit = this.toDigit(digits, indexFromLeft);
		return this.shouldDouble(indexFromRight)
				       ? this.adjustedDouble(digit)
				       : digit;
	}

	private int toDigit(String digits, int index) {
		return digits.charAt(index) - '0';
	}

	private boolean shouldDouble(int indexFromRight) {
		return indexFromRight % 2 == 1;
	}

	private int adjustedDouble(int digit) {
		int doubled = digit * 2;
		return doubled > 9
				       ? doubled - 9
				       : doubled;
	}
}
