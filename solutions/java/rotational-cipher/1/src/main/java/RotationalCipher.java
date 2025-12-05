import java.util.function.IntUnaryOperator;

class RotationalCipher {
	private final int shiftKey;
	RotationalCipher(int shiftKey) {
		this.shiftKey = shiftKey % ALPHABET_SIZE;
	}

	private static final int ALPHABET_SIZE = 26;
	private static final int LOWERCASE_A = 'a';
	private static final int UPPERCASE_A = 'A';

    private final IntUnaryOperator lowerCaseRotator = character
                    -> this.rotateWithinAlphabet(character, LOWERCASE_A);
    private final IntUnaryOperator upperCaseRotator = character
                    -> this.rotateWithinAlphabet(character, UPPERCASE_A);
    private final IntUnaryOperator identity = character
                    -> character;

    String rotate(String data) {
		return data.chars()
				       .map(this::rotateChar)
				       .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				       .toString();
	}


    private int rotateChar(int character) {
        return this.pickRotator(character)
                       .applyAsInt(character);
    }

    private IntUnaryOperator pickRotator(int character) {
        return Character.isLowerCase(character)
                ? lowerCaseRotator
                : Character.isUpperCase(character)
                          ? upperCaseRotator
                          : identity;
    }

    private int rotateWithinAlphabet(int character, int base) {
        int shiftedPosition = character - base + this.shiftKey;
        return base + shiftedPosition % ALPHABET_SIZE;
    }
}