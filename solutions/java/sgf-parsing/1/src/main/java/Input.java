import java.util.Set;

import static java.lang.Character.isWhitespace;

public final class Input {
    private final String originalValue;
    private int currentIndex = -1;

    private static final Set<Character> WHITESPACE_REPLACED =
            Set.of(Symbol.TAB.value(), Symbol.RETURN.value());

    public Input(String inputString) throws SgfParsingException {
        if (inputString == null || inputString.isEmpty()) {
            throw new SgfParsingException("Input cannot be null or empty.");
        }

        this.originalValue = inputString.trim();
    }

    public char current() {
        if (currentIndex < 0 || currentIndex >= originalValue.length()) {
            throw new IllegalStateException();
        }

        return originalValue.charAt(currentIndex);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean moveNext() {
        currentIndex++;
        return currentIndex < originalValue.length();
    }

    public Result<String> takeUntil(char terminator, String description) {
        StringBuilder builder = new StringBuilder();
        boolean escaped = false;
        while (true) {
            char character = current();
            if (escaped) {
                escaped = false;
                if (character != Symbol.NEW_LINE.value()) {
                    this.appendNormalizedCharacter(builder, character);
                }
            }
            else if (character == Symbol.ESCAPE.value()) {
                escaped = true;
            }
            else if (character == terminator) {
                break;
            }
            else {
                this.appendNormalizedCharacter(builder, character);
            }

            if (!moveNext()) {
                return Result.failure(new IllegalArgumentException(description + " is missing terminator: " + builder));
            }
        }

        String value = builder.toString();
        return value.isEmpty()
               ? Result.failure(new IllegalArgumentException(description + " is missing value."))
               : Result.success(value);
    }

    private void appendNormalizedCharacter(final StringBuilder builder, final char character) {
        char normalizedCharacter = WHITESPACE_REPLACED.contains(character)
            ? Symbol.SPACE.value()
            : character;
        builder.append(normalizedCharacter );
    }
}