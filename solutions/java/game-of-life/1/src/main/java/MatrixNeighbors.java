import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MatrixNeighbors {
    public static final int MATRIX_ROW_START = MatrixConstants.MATRIX_ROW_START;
    public static final int MATRIX_COLUMN_START = MatrixConstants.MATRIX_COLUMN_START;
    public static final int NEIGHBOR_OFFSET_MINUS_ONE = -1;
    public static final int NEIGHBOR_OFFSET_ZERO = 0;
    public static final int NEIGHBOR_OFFSET_ONE = 1;
    public static final List<Integer> NEIGHBOR_OFFSETS = List.of(
            NEIGHBOR_OFFSET_MINUS_ONE,
            NEIGHBOR_OFFSET_ZERO,
            NEIGHBOR_OFFSET_ONE
    );

    public Function<Map.Entry<Coordinate, Boolean>, Stream<? extends Coordinate>> getRelevantCoordinatesStream(int maxRow, int maxColumn) {
        return entry
                       -> this.getRelevantCoordinatesStream(maxRow, maxColumn, entry);
    }

    public Stream<Coordinate> getRelevantCoordinatesStream(int maxRow, int maxColumn, Map.Entry<Coordinate, Boolean> entry) {
        return this.getValidNeighborsAndSelf(entry.getKey(), maxRow, maxColumn)
                       .stream();
    }

    public List<Coordinate> getValidNeighborsAndSelf(Coordinate coordinate, int maxRow, int maxColumn) {
        List<Coordinate> neighbors = this.getNeighborCoordinates(coordinate).stream()
                                             .filter(n -> this.isWithinBounds(n, maxRow, maxColumn))
                                             .toList();
        List<Coordinate> result = new ArrayList<>(neighbors);
        result.add(coordinate);
        return result;
    }

    public List<Coordinate> getNeighborCoordinates(Coordinate coordinate) {
        return NEIGHBOR_OFFSETS.stream()
                       .flatMap(this::generateOffsetPairsForRow)
                       .filter(this::isNonZeroOffset)
                       .map(offset -> this.applyOffset(coordinate, offset))
                       .collect(Collectors.toList());
    }

    public Coordinate applyOffset(Coordinate coordinate, int[] offset) {
        return new Coordinate(coordinate.x() + offset[0], coordinate.y() + offset[1]);
    }

    public boolean isNonZeroOffset(int[] offset) {
        return offset[0] != MATRIX_ROW_START
                       || offset[1] != MATRIX_COLUMN_START;
    }

    public Stream<int[]> generateOffsetPairsForRow(Integer dx) {
        return NEIGHBOR_OFFSETS.stream()
                       .map(dy -> new int[]{dx, dy});
    }

    public boolean isWithinBounds(Coordinate coordinate, int maxRow, int maxColumn) {
        int x = coordinate.x();
        int y = coordinate.y();
        return x >= MATRIX_ROW_START
               && y >= MATRIX_COLUMN_START
               && x <= maxRow
               && y <= maxColumn;
    }
}
