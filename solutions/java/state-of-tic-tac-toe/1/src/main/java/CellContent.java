import java.util.Arrays;

enum CellContent {
    X('X'), O('O'), EMPTY(' ');

    private final char symbol;
    CellContent(char symbol) {
        this.symbol = symbol;
    }

    static CellContent fromChar(char character) {
        return Arrays.stream(values())
                       .filter(content -> content.symbol == character)
                       .findFirst()
                       .orElse(EMPTY);
    }
}