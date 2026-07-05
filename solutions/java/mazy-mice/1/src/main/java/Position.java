public record Position(int row, int column) {
    public Position nextPosition(int addToRow, int addToColumn) {
        return new Position(row + addToRow, column + addToColumn);
    }
}
