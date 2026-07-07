import java.util.stream.Stream;

public class Grid {
    private final Symbol[][] grid;

    public Grid(String[] rows) {
        this.grid = Symbol.convertToSymbols(rows);
    }

    public boolean isEmpty() {
        return this.grid.length == 0
               || this.grid[0].length == 0;
    }

    public Symbol get(Position position) {
        return this.grid[position.row()][position.column()];
    }


    boolean isCorner(Position position) {
        return Symbol.isCorner(this.get(position));
    }

    boolean isHorizontalEdge(Position position) {
        return Symbol.isHorizontalEdge(this.get(position));
    }

    boolean isVerticalEdge(Position position) {
        return Symbol.isVerticalEdge(this.get(position));
    }

    Stream<RectanglePosition> rectanglePositions() {
        int rowCount = this.grid.length;
        int columnCount = this.grid[0].length;
        return Stream.iterate(0, row -> row + 1)
                     .limit(rowCount)
                     .flatMap(topRow -> rectanglePositionsForBottomRows(topRow, rowCount, columnCount));
    }

    private static Stream<RectanglePosition> rectanglePositionsForBottomRows(
            final Integer topRow,
            final int rowCount,
            final int columnCount
    ) {
        return Stream.iterate(topRow + 1, row -> row + 1)
                     .limit(rowCount - topRow - 1)
                     .flatMap(bottomRow -> rectanglePositionsForLeftColumns(topRow, columnCount, bottomRow));
    }

    private static Stream<RectanglePosition> rectanglePositionsForLeftColumns(
            final Integer topRow,
            final int columnCount,
            final Integer bottomRow
    ) {
        return Stream.iterate(0, column -> column + 1)
                     .limit(columnCount)
                     .flatMap(leftColumn
                                      -> rectanglePositionsForRightColumns(topRow, leftColumn, bottomRow, columnCount)
                     );
    }

    private static Stream<RectanglePosition> rectanglePositionsForRightColumns(
            final Integer topRow, final Integer leftColumn, final Integer bottomRow, final int columnCount
    ) {
        return Stream.iterate(leftColumn + 1, column -> column + 1)
                     .limit(columnCount - leftColumn - 1)
                     .map(rightColumn -> new RectanglePosition(topRow, leftColumn, bottomRow, rightColumn));
    }
}