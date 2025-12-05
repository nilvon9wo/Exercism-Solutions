record LuhnValidator(LuhnLogic logic) {
	public LuhnValidator() {
		this(new LuhnLogic());
	}

	private static final char SPACE_CHARACTER = ' ';

	boolean isValid(String candidate) {
		return this.isStructureValid(candidate)
				       && this.isSumValid(stripSpaces(candidate));
	}

	private boolean isSumValid(String digits) {
		int sum = this.logic.computeLuhnSum(digits);
		return sum % 10 == 0;
	}

	private boolean isStructureValid(String string) {
		return this.containsOnlyDigitsAndSpaces(string)
				       && this.countNonSpaces(string) > 1;
	}

	private boolean containsOnlyDigitsAndSpaces(String string) {
		return string.chars()
				       .allMatch(this::isDigitOrSpace);
	}

	private boolean isDigitOrSpace(int character) {
		return Character.isDigit(character)
				       || character == SPACE_CHARACTER;
	}

	private long countNonSpaces(String string) {
		return string.chars()
				       .filter(c -> c != SPACE_CHARACTER)
				       .count();
	}

	private String stripSpaces(String string) {
		return string.replace(" ", "");
	}
}
