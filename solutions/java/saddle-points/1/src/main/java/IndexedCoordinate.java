public record IndexedCoordinate(MatrixCoordinate original, int row, int column) {
    public static IndexedCoordinate from(int rowIndex, int columnIndex) {
        int oneBasedRow = rowIndex + 1;
        int oneBasedColumn = columnIndex + 1;
        MatrixCoordinate originalCoordinate = new MatrixCoordinate(oneBasedRow, oneBasedColumn);
        return new IndexedCoordinate(originalCoordinate, oneBasedRow, oneBasedColumn);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof IndexedCoordinate that)) {
            return false;
        }

        return original.equals(that.original);
    }

    @Override
    public int hashCode() {
        return original.hashCode();
    }
}