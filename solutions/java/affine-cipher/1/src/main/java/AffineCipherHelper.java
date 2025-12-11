import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AffineCipherHelper {
    public static final int ALPHABET_SIZE = 26;
    public static final int FIRST_LOWERCASE_LETTER = 'a';
    private static final int BLOCK_SIZE = 5;

    public void validateKeyCoprime(int keyAMultiplier) {
        boolean isCoprime = IntStream.range(1, ALPHABET_SIZE)
                                    .anyMatch(candidate -> doesCandidateUndoKeyAMultiplier(keyAMultiplier, candidate));
        if (!isCoprime) {
            throw new IllegalArgumentException("Error: keyA and alphabet size must be coprime.");
        }
    }

    public boolean doesCandidateUndoKeyAMultiplier(int keyAMultiplier, int candidate) {
        return (keyAMultiplier * candidate) % ALPHABET_SIZE == 1;
    }

    public int findMultiplierThatUndoesKeyAMultiplier(int keyAMultiplier) {
        return IntStream.range(1, ALPHABET_SIZE)
                       .filter(candidate -> doesCandidateUndoKeyAMultiplier(keyAMultiplier, candidate))
                       .findFirst()
                       .orElseThrow(() -> new IllegalArgumentException(
                               "Error: keyA and alphabet size must be coprime."));
    }

    public String sanitizeForEncoding(String text) {
        return text.toLowerCase()
                       .chars()
                       .filter(Character::isLetterOrDigit)
                       .mapToObj(character -> String.valueOf((char) character))
                       .collect(Collectors.joining());
    }

    public String removeSpaces(String text) {
        return text.replaceAll("\\s+", "");
    }

    public String groupIntoBlocksOfFive(String text) {
        return IntStream.range(0, text.length())
                       .mapToObj(index -> this.addSpaceIfNeeded(index, text.charAt(index)))
                       .collect(Collectors.joining());
    }

    private String addSpaceIfNeeded(int characterIndex, char characterValue) {
        if (characterIndex > 0 && characterIndex % BLOCK_SIZE == 0) {
            return " " + characterValue;
        }

        return String.valueOf(characterValue);
    }

    public String transformCharacter(char characterValue, IntUnaryOperator transformOperation) {
        if (Character.isDigit(characterValue)) {
            return String.valueOf(characterValue);
        }

        int zeroBasedIndex = characterValue - FIRST_LOWERCASE_LETTER;
        int transformedIndex = transformOperation.applyAsInt(zeroBasedIndex);
        return String.valueOf((char) (FIRST_LOWERCASE_LETTER + transformedIndex));
    }
}