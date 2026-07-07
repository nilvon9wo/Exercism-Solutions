import java.util.stream.IntStream;
class RectangleCounter {
    long countRectangles(String[] input) {
        Grid grid = new Grid(input);
        return (grid.isEmpty())
               ? 0
               : grid.rectanglePositions()
                    .filter(position -> hasFourCorners(grid, position))
                    .filter(position -> hasHorizontalSides(grid, position))
                    .filter(position -> hasVerticalSides(grid, position))
                    .count();

    }

    private boolean hasFourCorners(Grid grid, RectanglePosition position) {
        return grid.isCorner(new Position(position.topRow(), position.leftColumn()))
               && grid.isCorner(new Position(position.topRow(), position.rightColumn()))
               && grid.isCorner(new Position(position.bottomRow(), position.leftColumn()))
               && grid.isCorner(new Position(position.bottomRow(), position.rightColumn()));
    }

    private boolean hasHorizontalSides(Grid grid, RectanglePosition position) {
        return IntStream.rangeClosed(position.leftColumn(), position.rightColumn())
                        .allMatch(column -> hasHorizontalEdgesAtColumn(grid, position, column));
    }

    private boolean hasHorizontalEdgesAtColumn(
            final Grid grid,
            final RectanglePosition position,
            final int column
    ) {
        return grid.isHorizontalEdge(new Position(position.topRow(), column))
               && grid.isHorizontalEdge(new Position(position.bottomRow(), column));
    }

    private boolean hasVerticalSides(
            Grid grid,
            RectanglePosition position
    ) {
        return IntStream.rangeClosed(position.topRow(), position.bottomRow())
                        .allMatch(row -> hasVerticalEdgesAtRow(grid, position, row));
    }

    private boolean hasVerticalEdgesAtRow(
            final Grid grid,
            final RectanglePosition position,
            final int row
    ) {
        return grid.isVerticalEdge(new Position(row, position.leftColumn()))
               && grid.isVerticalEdge(new Position(row, position.rightColumn()));
    }
}