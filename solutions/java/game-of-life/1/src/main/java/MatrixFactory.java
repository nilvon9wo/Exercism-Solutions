import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class MatrixFactory {
    public final int HAS_LIFE = Matrix.HAS_LIFE;

    public Matrix from(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new Matrix(new LifeGrid(Collections.emptyMap()));
        }

        List<Coordinate> coordinates = this.generateAllCoordinates(matrix);
        Map<Coordinate, Boolean> hasLifeByCoordinates = this.mapCoordinatesToLifeStatus(coordinates, matrix);
        LifeGrid lifeGrid = new LifeGrid(hasLifeByCoordinates);
        return new Matrix(lifeGrid);
    }

    private Map<Coordinate, Boolean> mapCoordinatesToLifeStatus(
            List<Coordinate> coordinates,
            int[][] sourceMatrix
    ) {
        return coordinates.stream()
                       .collect(this.createCoordinateLifeCollector(sourceMatrix));
    }

    private Collector<Coordinate, ?, Map<Coordinate, Boolean>> createCoordinateLifeCollector(int[][] sourceMatrix) {
        return Collectors.toMap(
                coordinate -> coordinate,
                coordinate -> this.cellHasLife(coordinate, sourceMatrix)
        );
    }

    private boolean cellHasLife(Coordinate coordinate, int[][] sourceMatrix) {
        return sourceMatrix[coordinate.x()][coordinate.y()] == HAS_LIFE;
    }

    private List<Coordinate> generateAllCoordinates(int[][] matrix) {
        return IntStream.range(MatrixNeighbors.MATRIX_ROW_START, matrix.length)
                       .boxed()
                       .flatMap(x -> this.generateRowCoordinates(matrix, x))
                       .toList();
    }

    private Stream<Coordinate> generateRowCoordinates(int[][] matrix, Integer x) {
        return IntStream.range(MatrixNeighbors.MATRIX_COLUMN_START, matrix[0].length)
                       .mapToObj(y -> new Coordinate(x, y));
    }
}
