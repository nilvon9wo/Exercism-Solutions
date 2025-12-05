import java.util.stream.Collectors;

public record AtbashLetterInverter(String alphabet) {

    private static final int ALPHABET_LENGTH = 26;
    private static final int ASCII_OFFSET_LOWERCASE_A = 'a';

    String invertLetters(String text) {
        return text.chars()
                       .mapToObj(this::invertCharacter)
                       .collect(Collectors.joining());
    }

    private String invertCharacter(int character) {
        return Character.isLetter(character)
                       ? this.invertLetterCharacter(character)
                       : String.valueOf((char) character);
    }

    private String invertLetterCharacter(int character) {
        int invertedIndex = (ALPHABET_LENGTH - 1) - (character - ASCII_OFFSET_LOWERCASE_A);
        return String.valueOf(alphabet.charAt(invertedIndex));
    }
}
