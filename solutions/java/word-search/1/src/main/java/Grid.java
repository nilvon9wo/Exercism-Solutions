import java.util.Optional;

public class Grid {
    private final char[][] grid;
    public Grid(char[][] grid) {
        this.grid = grid;
    }

    Optional<WordLocation> findWord(final String word) {
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                for (Direction direction : Direction.values()) {
                    Position startPosition = new Position(row, column);
                    Optional<WordLocation> match = this.matchFrom(word, startPosition, direction);
                    if (match.isPresent()) {
                        return match;
                    }
                }
            }
        }

        return Optional.empty();
    }

    private Optional<WordLocation> matchFrom(
            final String word,
            final Position startPosition,
            final Direction direction
    ) {
        int row = startPosition.row();
        int column = startPosition.column();
        int deltaRow =  direction.x;
        int deltaColumn = direction.y;

        for (int i = 0; i < word.length(); i++) {
            if (!inBounds(new Position(row, column)) || grid[row][column] != word.charAt(i)) {
                return Optional.empty();
            }

            row += deltaRow;
            column += deltaColumn;
        }

        Pair start = new Pair(startPosition.column() + 1, startPosition.row() + 1);
        Pair end = new Pair(column - deltaColumn + 1, row - deltaRow + 1);
        return Optional.of(new WordLocation(start, end));
    }

    private boolean inBounds(final Position position) {
        int row = position.row();
        int column = position.column();
        return row >= 0
               && row < grid.length
               && column >= 0
               && column < grid[row].length;
    }
}
