record Queen(int row, int column) {

    Queen {
        validate(row, "row");
        validate(column, "column");
    }

    private static void validate(int value, String label) {
        if (value < 0) {
            throw new IllegalArgumentException("Queen position must have positive %s.".formatted(label));
        }
        if (value > 7) {
            throw new IllegalArgumentException("Queen position must have %s <= 7.".formatted(label));
        }
    }
}