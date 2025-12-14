import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class CellCharacterMap {
    private final Map<Coordinate, Character> cellCharacterMap;
    private final List<String> boardRows;

    CellCharacterMap(Set<Coordinate> flowerCoordinates, List<String> boardRows) {
        this.boardRows = boardRows;
        this.cellCharacterMap = this.buildCellCharacterMap(flowerCoordinates);
    }

    private static final int[] ADJACENT_OFFSETS = {-1, 0, 1};
    private static final char EMPTY_CELL = ' ';
    public static final char FLOWER_CELL = '*';

    private Map<Coordinate, Character> buildCellCharacterMap(Set<Coordinate> flowerCoordinates) {
        return this.getAllCoordinates()
                   .stream()
                   .collect(Collectors.toMap(
                           coordinate -> coordinate,
                           coordinate -> this.getCellCharacter(coordinate, flowerCoordinates)
                   ));
    }

    private List<Coordinate> getAllCoordinates() {
        return IntStream.range(0, boardRows.size())
                        .boxed()
                        .flatMap(this::coordinatesInRow)
                        .toList();
    }

    private Stream<Coordinate> coordinatesInRow(final Integer rowIndex) {
        final int rowLength = boardRows.get(rowIndex)
                                       .length();
        return IntStream.range(0, rowLength)
                        .mapToObj(colIndex -> new Coordinate(colIndex, rowIndex));
    }

    private char getCellCharacter(Coordinate coordinate, Set<Coordinate> flowerCoordinates) {
        if (flowerCoordinates.contains(coordinate)) {
            return FLOWER_CELL;
        }

        long adjacentFlowerCount = this.countAdjacentFlowers(coordinate, flowerCoordinates);
        return adjacentFlowerCount == 0
               ? EMPTY_CELL
               : Character.forDigit((int) adjacentFlowerCount, 10);
    }

    private long countAdjacentFlowers(final Coordinate coordinate, final Set<Coordinate> flowerCoordinates) {
        return this.getAdjacentCoordinates(coordinate)
                   .stream()
                   .filter(flowerCoordinates::contains)
                   .count();
    }

    private List<Coordinate> getAdjacentCoordinates(Coordinate coordinate) {
        return Arrays.stream(ADJACENT_OFFSETS)
                     .boxed()
                     .flatMap(deltaY -> this.adjacentCoordinatesInRow(coordinate, deltaY))
                     .toList();
    }

    private Stream<Coordinate> adjacentCoordinatesInRow(final Coordinate coordinate, final Integer deltaY) {
        return Arrays.stream(ADJACENT_OFFSETS)
                     .filter(deltaX -> deltaX != 0 || deltaY != 0)
                     .mapToObj(deltaX -> coordinate.offsetBy(deltaX, deltaY));
    }

    public Character getCharacterAtCell(final int rowIndex, final int colIndex) {
        return cellCharacterMap.get(new Coordinate(colIndex, rowIndex));
    }
}
