public class MazeGrid {
    private MazeGrid(MazeCell[][] cells){
        this.cells = cells;
    }

    private final MazeCell[][] cells;
    MazeCell get(final Position startPosition) {
        return this.cells[startPosition.row()][startPosition.column()];
    }

    public static MazeGrid create(MazeContext context) {
        int numberOfRows = context.getNumberOfRows();
        int numberOfColumns = context.getNumberOfColumns();
        MazeCell[][] mazeCells = new MazeCell[numberOfRows][numberOfColumns];
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                mazeCells[row][column] = new MazeCell();
            }
        }

        return new MazeGrid(mazeCells);
    }
}
