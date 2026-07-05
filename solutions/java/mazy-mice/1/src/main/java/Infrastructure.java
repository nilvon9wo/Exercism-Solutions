public enum Infrastructure {
    SPACE(' '),
    ENTRANCE_EXIT_SYMBOL('⇨'),

    HORIZONTAL_WALL('─'),
    VERTICAL_WALL('│'),
    INTERSECTION('┼'),

    TOP_LEFT_CORNER('┌'),
    TOP_RIGHT_CORNER('┐'),
    BOTTOM_LEFT_CORNER('└'),
    BOTTOM_RIGHT_CORNER('┘');

    private final char symbol;

    Infrastructure(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return symbol;
    }
}