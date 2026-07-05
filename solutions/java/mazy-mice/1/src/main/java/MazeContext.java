import java.util.Random;

public class MazeContext {
    public MazeContext(int rows, int columns, Random randomGenerator) {
        this.validateDimensions(rows, columns);
        this.numberOfRows = rows;
        this.numberOfColumns = columns;
        this.random = randomGenerator;
    }

    private final int numberOfRows;
    public int getNumberOfRows() {
        return this.numberOfRows;
    }

    private final int numberOfColumns;
    public int getNumberOfColumns() {
        return this.numberOfColumns;
    }
    private final Random random;
    public Random getRandom() {
        return this.random;
    }

    private void validateDimensions(int rows, int columns) {
        if (rows < 5 || rows > 100 || columns < 5 || columns > 100) {
            throw new IllegalArgumentException();
        }
    }

    private MazeGrid grid;
    public MazeGrid getGrid() {
        return this.grid;
    }
    @SuppressWarnings("UnusedReturnValue")
    public MazeContext setGrid(MazeGrid grid) {
        this.grid = grid;
        return this;
    }
}
