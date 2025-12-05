import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MatrixCoordinateMapFactory {

    public Map<IndexedCoordinate, Integer> createMatrixMap(List<List<Integer>> values) {
        int rowCount = values.size();
        int columnCount = (rowCount == 0)
                              ? 0
                              : values.getFirst()
                                        .size();
        return this.buildMatrixMap(values, rowCount, columnCount);
    }

    private Map<IndexedCoordinate, Integer> buildMatrixMap(
            List<List<Integer>> values,
            int rowCount,
            int columnCount
    ) {
        return IntStream.range(0, rowCount)
                       .boxed()
                       .flatMap(rowIndex -> this.buildRowEntries(values, rowIndex, columnCount))
                       .collect(this.entryToMapCollector());
    }

    private Stream<Map.Entry<IndexedCoordinate, Integer>> buildRowEntries(
            List<List<Integer>> values,
            int rowIndex,
            int rowCount
    ) {
        return IntStream.range(0, rowCount)
                       .mapToObj(columnIndex -> IndexedCoordinate.from(rowIndex, columnIndex))
                       .map(coordinate -> this.createEntry(values, coordinate));
    }

    private Map.Entry<IndexedCoordinate, Integer> createEntry(
            List<List<Integer>> values,
            IndexedCoordinate coordinate
    ) {
        int zeroBasedRow = coordinate.row() - 1;
        int zeroBasedColumn = coordinate.column() - 1;
        Integer value = values.get(zeroBasedRow)
                                .get(zeroBasedColumn);
        return Map.entry(coordinate, value);
    }

    private Collector<Map.Entry<IndexedCoordinate, Integer>, ?, Map<IndexedCoordinate, Integer>> entryToMapCollector() {
        return Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        );
    }
}
