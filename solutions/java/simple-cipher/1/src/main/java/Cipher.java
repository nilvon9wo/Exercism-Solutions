import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("ClassCanBeRecord")
public class Cipher {
    private static final int ALPHABET_SIZE = 26;
    private static final int RANDOM_KEY_LENGTH = 100;

    private final String key;
    public Cipher() {
        this(generateRandomKey());
    }

    public Cipher(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String encode(String plainText) {
        return transform(plainText, 1);
    }

    public String decode(String cipherText) {
        return transform(cipherText, -1);
    }

    private String transform(String text, int direction) {
        return IntStream.range(0, text.length())
                        .mapToObj(index -> transformCharacter(text, index, direction))
                        .collect(StringBuilder::new,StringBuilder::append,StringBuilder::append)
                        .toString();
    }

    private char transformCharacter(final String text, final int index, final int direction) {
        return transformCharacter(text.charAt(index), key.charAt(index % key.length()), direction);
    }

    private char transformCharacter(char character, char keyCharacter, int direction) {
        int shift = keyCharacter - 'a';
        return (char) ('a' + calculateShiftedAlphabetIndex(character, direction, shift));
    }

    private static int calculateShiftedAlphabetIndex(
            final char character,
            final int direction,
            final int shift
    ) {
        return Math.floorMod(character - 'a' + (shift * direction), ALPHABET_SIZE);
    }

    private static String generateRandomKey() {
        Random random = new Random();
        return IntStream.range(0, RANDOM_KEY_LENGTH)
                        .mapToObj(index -> (char) ('a' + random.nextInt(ALPHABET_SIZE)))
                        .map(String::valueOf)
                        .collect(Collectors.joining());
    }
}