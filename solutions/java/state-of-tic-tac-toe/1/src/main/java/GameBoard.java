import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class GameBoard {
    private final Map<Coordinate, CellContent> cells;
    private GameBoard(Map<Coordinate, CellContent> cells) {
        this.cells = cells;
    }

    static GameBoard fromRows(String[] boardRows) {
        Map<Coordinate, CellContent> cells = createBoardIndexes()
                                                     .boxed()
                                                     .flatMap(row -> createRowCells(boardRows, row))
                                                     .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new GameBoard(cells);
    }

    private static Stream<Map.Entry<Coordinate, CellContent>> createRowCells(String[] boardRows, int row) {
        return createBoardIndexes()
                       .mapToObj(column -> Map.entry(
                               new Coordinate(row, column),
                               CellContent.fromChar(boardRows[row].charAt(column))
                       ));
    }

    Map<CellContent, PlayerStatus> getPlayerStatuses() {
        return Map.of(
                CellContent.X, buildPlayerStatus(CellContent.X),
                CellContent.O, buildPlayerStatus(CellContent.O)
        );
    }

    private PlayerStatus buildPlayerStatus(CellContent player) {
        return new PlayerStatus(this.count(player), this.hasWon(player));
    }

    long count(CellContent player) {
        return cells.values()
                       .stream()
                       .filter(content -> content == player)
                       .count();
    }

    boolean hasWon(CellContent player) {
        return this.hasRowVictory(player)
                       || this.hasColumnVictory(player)
                       || this.hasDiagonalVictory(player);
    }

    private boolean hasRowVictory(CellContent player) {
        return this. hasLineVictory(row -> isRowFullyOccupiedBy(row, player));
    }

    private boolean hasColumnVictory(CellContent player) {
        return this.hasLineVictory(column -> isColumnFullyOccupiedBy(column, player));
    }

    private boolean hasLineVictory(Function<Integer, Boolean> lineChecker) {
        return createBoardIndexes()
                       .anyMatch(lineChecker::apply);
    }

    private boolean hasDiagonalVictory(CellContent player) {
        List<Function<Integer, Coordinate>> diagonals = List.of(
                i -> new Coordinate(i, i),       // main diagonal
                i -> new Coordinate(i, 2 - i)    // anti-diagonal
        );

        return diagonals.stream()
                       .anyMatch(mapper -> this.hasDiagonalVictory(player, mapper));
    }

    private boolean hasDiagonalVictory(CellContent player, Function<Integer, Coordinate> coordinateMapper) {
        return this.doAllCellsMatch(player, coordinateMapper);
    }

    private boolean isRowFullyOccupiedBy(int row, CellContent player) {
        return this.doAllCellsMatch(player, column -> new Coordinate(row, column));
    }

    private boolean isColumnFullyOccupiedBy(int column, CellContent player) {
        return this.doAllCellsMatch(player, row -> new Coordinate(row, column));
    }

    private boolean doAllCellsMatch(CellContent player, Function<Integer, Coordinate> coordinateMapper) {
        return createBoardIndexes()
                       .allMatch(i -> this.isCellOccupiedBy(coordinateMapper.apply(i), player));
    }

    private boolean isCellOccupiedBy(Coordinate coordinate, CellContent player) {
        return cells.get(coordinate) == player;
    }

    private static IntStream createBoardIndexes() {
        return IntStream.range(0, 3);
    }
}
