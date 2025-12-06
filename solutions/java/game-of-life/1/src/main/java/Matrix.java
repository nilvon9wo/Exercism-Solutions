import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Matrix(
        LifeGrid lifeGrid,
        MatrixLifeRules lifeRules,
        MatrixNeighbors matrixNeighbors
) {

    public Matrix(LifeGrid lifeGrid) {
        this(lifeGrid, new MatrixLifeRules(), new MatrixNeighbors());
    }

    public static final int MATRIX_ROW_START = MatrixConstants.MATRIX_ROW_START;
    public static final int MATRIX_COLUMN_START = MatrixConstants.MATRIX_COLUMN_START;
    public static final int HAS_LIFE = 1;
    private static final int IS_LIFELESS = 0;

    public Matrix toNextState() {
        Map<Coordinate, Boolean> nextStateLifeByCoordinates =
                this.getAllCoordinatesToCheck().stream()
                        .collect(this.createNextStateCollector());
        LifeGrid nextGrid = new LifeGrid(nextStateLifeByCoordinates);
        return new Matrix(nextGrid);
    }

    public int[][] toIntArray() {
        if (this.lifeGrid.isEmpty()) return new int[0][0];
        int maxX = this.getMaxCoordinate(Coordinate::x);
        int maxY = this.getMaxCoordinate(Coordinate::y);
        return this.buildMatrix(maxX, maxY);
    }

    private Collector<Coordinate, ?, Map<Coordinate, Boolean>> createNextStateCollector() {
        return Collectors.toMap(
                coordinate -> coordinate,
                coordinate -> this.lifeRules.willHaveLifeInNextState(this.lifeGrid, coordinate)
        );
    }

    private Set<Coordinate> getAllCoordinatesToCheck() {
        Optional<int[]> bounds = this.getOriginalMatrixBounds();
        if (bounds.isEmpty()) return new HashSet<>();
        int[] matrixBounds = bounds.get();
        int maxRow = matrixBounds[0];
        int maxColumn = matrixBounds[1];

        return this.lifeGrid.streamEntries()
                       .flatMap(this.matrixNeighbors.getRelevantCoordinatesStream(maxRow, maxColumn))
                       .collect(Collectors.toSet());
    }

    private Optional<int[]> getOriginalMatrixBounds() {
        List<Coordinate> coordinates = this.lifeGrid.streamOriginalCoordinates()
                                               .toList();
        if (coordinates.isEmpty()) return Optional.empty();
        int maxX = this.getMaxCoordinateValue(coordinates, Coordinate::x, MATRIX_ROW_START);
        int maxY = this.getMaxCoordinateValue(coordinates, Coordinate::y, MATRIX_COLUMN_START);
        return Optional.of(new int[]{maxX, maxY});
    }


    private int getMaxCoordinateValue(List<Coordinate> coordinates, ToIntFunction<Coordinate> mapper, int defaultValue) {
        return coordinates.stream()
                       .mapToInt(mapper)
                       .max()
                       .orElse(defaultValue);
    }

    private int getMaxCoordinate(ToIntFunction<Coordinate> coordinateMapper) {
        return this.lifeGrid.streamOriginalCoordinates()
                       .mapToInt(coordinateMapper)
                       .max()
                       .orElse(MATRIX_ROW_START);
    }



    private int[][] buildMatrix(int maxX, int maxY) {
        return IntStream.range(MATRIX_ROW_START, maxX + 1)
                       .mapToObj(row -> this.buildRow(row, maxY))
                       .toArray(int[][]::new);
    }

    private int[] buildRow(int row, int maxY) {
        return IntStream.range(MATRIX_COLUMN_START, maxY + 1)
                       .map(column -> this.cellValue(row, column))
                       .toArray();
    }

    private int cellValue(int row, int column) {
        return this.lifeRules.hasLifeByCoordinates(this.lifeGrid, new Coordinate(row, column))
                       ? HAS_LIFE
                       : IS_LIFELESS;
    }
}
