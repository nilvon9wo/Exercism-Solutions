import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.StreamSupport;

final class Connect {
    private final Board board;
    public Connect(String[] input) {
        this.board = Board.from(input);
    }

    private static final Map<Player, BiFunction<Board, Player, Boolean>> WINNING_CONDITIONS =
            Map.of(
                    Player.White, Connect::isVerticallyConnected,
                    Player.Black, Connect::isHorizontallyConnected
            );

    public Winner computeWinner() {
        for (Map.Entry<Player, BiFunction<Board, Player, Boolean>> entry : WINNING_CONDITIONS.entrySet()) {
            Player player = entry.getKey();
            BiFunction<Board, Player, Boolean> condition = entry.getValue();
            if (condition.apply(board, player)) {
                return player.winner;
            }
        }

        return Winner.NONE;
    }

    private static boolean isVerticallyConnected(Board board, Player player) {
        List<Cell> bottom = board.getBottomEdge();
        List<Cell> top = board.getTopEdge();
        return isConnected(board, player, bottom, top);
    }

    private static boolean isHorizontallyConnected(Board board, Player player) {
        List<Cell> left = board.getLeftEdge();
        List<Cell> right = board.getRightEdge();
        return isConnected(board, player, left, right);
    }

    private static boolean isConnected(
            Board board,
            Player player,
            List<Cell> startEdge,
            List<Cell> endEdge
    ) {
        List<Cell> endCells = belongingTo(endEdge, player);
        Set<HexCoordinates> visited = new HashSet<>();
        return belongingTo(startEdge, player)
                .stream()
                .anyMatch(startCell ->
                                  depthFirstSearch(
                                        board,
                                        player,
                                        startCell.coordinates(),
                                        endCells,
                                        visited
                                )
              );
    }

    private static boolean depthFirstSearch(
            Board board,
            Player player,
            HexCoordinates current,
            List<Cell> endCells,
            Set<HexCoordinates> visited
    ) {
        boolean isTarget
                = endCells.stream()
                          .anyMatch(cell -> cell.coordinates().equals(current));
        if (isTarget) {
            return true;
        }

        if (!visited.add(current)) {
            return false;
        }

        return board.getNeighbors(current)
                    .filter(neighbor -> !visited.contains(neighbor.coordinates()))
                    .filter(neighbor -> neighbor.belongsTo(player))
                    .anyMatch(neighbor ->
                                      depthFirstSearch(
                                              board,
                                              player,
                                              neighbor.coordinates(),
                                              endCells,
                                              visited
                                      )
                    );
    }

    static List<Cell> belongingTo(Iterable<Cell> cells, Player player) {
        BoardContent targetContent = player.boardContent;

        return StreamSupport.stream(cells.spliterator(), false)
                            .filter(cell -> cell.content() == targetContent)
                            .toList();
    }
}