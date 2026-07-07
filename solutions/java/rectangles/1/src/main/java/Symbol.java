import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Symbol {
    CORNER('+'),
    HORIZONTAL_EDGE('-'),
    VERTICAL_EDGE('|'),
    EMPTY(' ');

    private static final Map<Character, Symbol> SYMBOLS_BY_CHARACTER
            = Arrays.stream(values())
                    .collect(Collectors.toMap(Symbol::character, Function.identity()));

    Symbol(char character) {
        this.character = character;
    }

    private final char character;
    private char character() {
        return character;
    }

    static boolean isCorner(Symbol symbol) {
        return symbol == CORNER;
    }

    static boolean isHorizontalEdge(Symbol symbol) {
        return symbol == HORIZONTAL_EDGE
               || symbol == CORNER;
    }

    static boolean isVerticalEdge(Symbol symbol) {
        return symbol == VERTICAL_EDGE
               || symbol == CORNER;
    }

    static Symbol[][] convertToSymbols(String[] rows) {
        return Arrays.stream(rows)
                     .map(Symbol::convertRowToSymbols)
                     .toArray(Symbol[][]::new);
    }

    static Symbol[] convertRowToSymbols(String row) {
        return row.chars()
                  .mapToObj(character -> Symbol.from((char) character))
                  .toArray(Symbol[]::new);
    }

    static Symbol from(char character) {
        Symbol symbol = SYMBOLS_BY_CHARACTER.get(character);
        if (symbol == null) {
            throw new IllegalArgumentException("Unknown grid symbol: " + character);
        }

        return symbol;
    }
}