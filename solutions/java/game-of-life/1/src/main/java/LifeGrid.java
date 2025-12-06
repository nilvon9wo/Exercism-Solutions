import java.util.Map;
import java.util.stream.Stream;

public record LifeGrid(Map<Coordinate, Boolean> hasLifeByCoordinates) {
    public static final int MATRIX_ROW_START = MatrixConstants.MATRIX_ROW_START;
    public static final int MATRIX_COLUMN_START = MatrixConstants.MATRIX_COLUMN_START;

    public boolean isEmpty() {
        return hasLifeByCoordinates.isEmpty();
    }

    public Boolean hasLife(Coordinate neighbor) {
        return this.hasLifeByCoordinates.getOrDefault(neighbor, false);
    }

    public Stream<Map.Entry<Coordinate, Boolean>> streamEntries() {
        return this.hasLifeByCoordinates.entrySet()
                       .stream()
                       .filter(Map.Entry::getValue);
    }

    public Stream<Coordinate> streamOriginalCoordinates() {
        return this.hasLifeByCoordinates.keySet()
                       .stream()
                       .filter(this::isValidOriginalCoordinate);
    }

    private boolean isValidOriginalCoordinate(Coordinate coordinate) {
        return coordinate.x() >= MATRIX_ROW_START
                       && coordinate.y() >= MATRIX_COLUMN_START;
    }
}
