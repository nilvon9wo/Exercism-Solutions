import java.util.*;
import java.util.stream.*;

final class Board {
    private Map<HexCoordinates, Cell> cellByCoordinates;

    private Board() {
    }

    static Board from(String[] inputString) {
        if (inputString == null || inputString.length == 0) {
            throw new IllegalArgumentException("Input string cannot be null or empty.");
        }

        Board board = new Board();
        board.cellByCoordinates = IntStream.range(0, inputString.length)
                                           .mapToObj(row -> Cell.manyFrom(inputString[row], row))
                                           .flatMap(Collection::stream)
                                           .filter(Objects::nonNull)
                                           .filter(cell -> cell.content() != BoardContent.Ignored)
                                           .collect(Collectors.toMap(Cell::coordinates, cell -> cell));
        return board;
    }

    Cell get(HexCoordinates coordinates) {
        return cellByCoordinates.getOrDefault(coordinates, null);
    }

    List<Cell> getTopEdge() {
        int minimumR = cellByCoordinates.keySet()
                                    .stream()
                                    .mapToInt(HexCoordinates::r)
                                    .min()
                                    .orElseThrow();

        return cellByCoordinates.values()
                                .stream()
                                .filter(cell -> cell.coordinates().r() == minimumR)
                                .sorted(Comparator.comparing(cell -> cell.coordinates().q()))
                                .toList();
    }

    List<Cell> getBottomEdge() {
        int maximumR = cellByCoordinates.keySet()
                                    .stream()
                                    .mapToInt(HexCoordinates::r)
                                    .max()
                                    .orElseThrow();

        return cellByCoordinates.values()
                                .stream()
                                .filter(cell -> cell.coordinates().r() == maximumR)
                                .sorted(Comparator.comparing(cell -> cell.coordinates().q()))
                                .toList();
    }

    List<Cell> getLeftEdge() {
        return cellByCoordinates.values()
                                .stream()
                                .filter(cell -> {
                                    HexCoordinates coordinates = cell.coordinates();
                                    HexCoordinates westNeighbor
                                            = new HexCoordinates(
                                                    coordinates.q() - 1,
                                                    coordinates.r(),
                                                    coordinates.s() + 1
                                    );
                                    return !cellByCoordinates.containsKey(westNeighbor);
                                })
                                .collect(Collectors.groupingBy(cell -> cell.coordinates().r()))
                                .values()
                                .stream()
                                .map(group -> group.stream()
                                                   .min(Comparator.comparing(cell -> cell.coordinates().q()))
                                                   .orElseThrow()
                                )
                                .sorted(Comparator.comparing(cell -> cell.coordinates().r()))
                                .toList();
    }

    List<Cell> getRightEdge() {
        return cellByCoordinates.values()
                                .stream()
                                .filter(cell -> {
                                    HexCoordinates coordinates = cell.coordinates();
                                    HexCoordinates eastNeighbor = new HexCoordinates(
                                            coordinates.q() + 1,
                                            coordinates.r(),
                                            coordinates.s() - 1
                                    );

                                    return !cellByCoordinates.containsKey(eastNeighbor);
                                })
                                .collect(Collectors.groupingBy(c -> c.coordinates().r()))
                                .values()
                                .stream()
                                .map(group -> group.stream()
                                                   .max(Comparator.comparing(cell -> cell.coordinates().q()))
                                                   .orElseThrow()
                                )
                                .sorted(Comparator.comparing(cell -> cell.coordinates().r()))
                                .toList();
    }

    Stream<Cell> getNeighbors(HexCoordinates coordinates) {
        List<HexCoordinates> neighbors = List.of(
                new HexCoordinates(coordinates.q() - 1, coordinates.r(), coordinates.s() + 1),
                new HexCoordinates(coordinates.q() - 1, coordinates.r() + 1, coordinates.s()),
                new HexCoordinates(coordinates.q(), coordinates.r() - 1, coordinates.s() + 1),
                new HexCoordinates(coordinates.q(), coordinates.r() + 1, coordinates.s() - 1),
                new HexCoordinates(coordinates.q() + 1, coordinates.r() - 1, coordinates.s()),
                new HexCoordinates(coordinates.q() + 1, coordinates.r(), coordinates.s() - 1)
        );
        return neighbors.stream()
                        .map(this::get)
                        .filter(Objects::nonNull);
    }
}