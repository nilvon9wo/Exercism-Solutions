import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.*;

public record Matrix(Map<IndexedCoordinate, Integer> matrix) {
    private Matrix(List<List<Integer>> values, MatrixCoordinateMapFactory mapFactory) {
        this(mapFactory.createMatrixMap(values));
    }

    public Matrix(List<List<Integer>> values) {
        this(values, new MatrixCoordinateMapFactory());
    }

    Set<MatrixCoordinate> getSaddlePoints() {
        Map<Integer, Integer> maximaByRow = this.computeRowMaxima();
        Map<Integer, Integer> minimaByColumn = this.computeColumnMinima();
        return matrix.entrySet()
                       .stream()
                       .filter(entry -> this.isSaddlePoint(entry, maximaByRow, minimaByColumn))
                       .map(this::getUnderlyingCoordinate)
                       .collect(Collectors.toSet());
    }

    private Map<Integer, Integer> computeRowMaxima() {
        return this.computeWith(IndexedCoordinate::row, BinaryOperator::maxBy);
    }

    private Map<Integer, Integer> computeColumnMinima() {
        return this.computeWith(IndexedCoordinate::column, BinaryOperator::minBy);
    }

    private Map<Integer, Integer> computeWith(
            Function<IndexedCoordinate, Integer> keyExtractor,
            Function<Comparator<Integer>, BinaryOperator<Integer>> aggregatorFactory
    ) {
        return matrix.entrySet()
                       .stream()
                       .collect(this.toIntegerMapCollector(keyExtractor, aggregatorFactory));
    }

    private Collector<Map.Entry<IndexedCoordinate, Integer>, ?, Map<Integer, Integer>> toIntegerMapCollector(
            Function<IndexedCoordinate, Integer> keyExtractor,
            Function<Comparator<Integer>, BinaryOperator<Integer>> aggregatorFactory
    ) {
        return Collectors.toMap(
                entry -> keyExtractor.apply(entry.getKey()),
                Map.Entry::getValue,
                aggregatorFactory.apply(Comparator.naturalOrder())
        );
    }

    private boolean isSaddlePoint(Map.Entry<IndexedCoordinate, Integer> entry,
                                  Map<Integer, Integer> maximaByRow,
                                  Map<Integer, Integer> minimaByColumn
    ) {
        int value = entry.getValue();
        IndexedCoordinate coordinate = entry.getKey();
        return (value == maximaByRow.get(coordinate.row())) &&
                       (value == minimaByColumn.get(coordinate.column()));
    }

    private MatrixCoordinate getUnderlyingCoordinate(Map.Entry<IndexedCoordinate, Integer> e) {
        return e.getKey()
                       .original();
    }
}