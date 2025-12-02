import java.util.stream.IntStream;

class IsbnVerifier {

	boolean isValid(String stringToVerify) {
		if (stringToVerify == null || stringToVerify.isEmpty()) {
			return false;
		}

		String isbn = stringToVerify.replace("-", "");
		if (isbn.length() != 10) {
			return false;
		}

		try {
			int sum = this.getSum(isbn);
			return sum % 11 == 0;
		}
		catch (IllegalArgumentException e) {
			return false;
		}
	}

	private int getSum(String isbn) {
		return IntStream.range(0, 10)
				       .map(i -> this.weightedIsbnDigit(i, isbn))
				       .sum();
	}

	private int weightedIsbnDigit(int index, String isbn) {
		char character = isbn.charAt(index);
		if (index == 9 && character == 'X') {
			return 10 * (10 - index);
		}
		else if (Character.isDigit(character)) {
			return Character.getNumericValue(character) * (10 - index);
		}
		else {
			throw new IllegalArgumentException("Invalid character in ISBN");
		}
	}
}
