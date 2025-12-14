import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CellCharacterMapFactory {
    private static final char FLOWER_CELL = CellCharacterMap.FLOWER_CELL;
    public CellCharacterMap createFrom(List<String> boardRows) {
        Set<Coordinate> flowerCoordinates = this.collectFlowerCoordinates(boardRows);
        return new CellCharacterMap(flowerCoordinates, boardRows);
    }

    private Set<Coordinate> collectFlowerCoordinates(List<String> boardRows) {
        return IntStream.range(0, boardRows.size())
                        .mapToObj(i -> this.coordinatesInRowWithFlowers(boardRows, i))
                        .flatMap(List::stream)
                        .collect(Collectors.toSet());
    }

    private List<Coordinate> coordinatesInRowWithFlowers(List<String> boardRows, final int rowIndex) {
        final String row = boardRows.get(rowIndex);
        return this.findFlowers(row, rowIndex);
    }

    private List<Coordinate> findFlowers(String row, int rowIndex) {
        return IntStream.range(0, row.length())
                        .filter(colIndex -> row.charAt(colIndex) == FLOWER_CELL)
                        .mapToObj(colIndex -> new Coordinate(colIndex, rowIndex))
                        .toList();
    }
}
