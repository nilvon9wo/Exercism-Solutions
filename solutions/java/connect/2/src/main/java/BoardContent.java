import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum BoardContent {

    Ignored(' '),
    Empty('.'),
    Black('X'),
    White('O');

    private final char inputCharacter;

    BoardContent(char inputCharacter) {
        this.inputCharacter = inputCharacter;
    }

    public char getInputCharacter() {
        return inputCharacter;
    }
}