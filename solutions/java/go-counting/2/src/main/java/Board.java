import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Board implements Iterable<Point> {
    public Board(char[][] matrix) {
        this.matrix = matrix;
        this.height = matrix.length;
        this.width = matrix[0].length;
    }

    public static Board create(String boardInput) {
        String[] rows = boardInput.split("\n");
        char[][] matrix = createMatrix(rows);
        return new Board(matrix);
    }

    private static char[][] createMatrix(final String[] rows) {
        int height = rows.length;
        int width = rows[0].length();

        final char[][] board = new char[height][width];
        for (int y = 0; y < height; y++) {
            board[y] = rows[y].toCharArray();
        }

        return board;
    }

    private final char[][] matrix;
    private final int width;
    private final int height;

    public boolean isEmpty(Point point) {
        return this.get(point) == PlayerHelper.EMPTY;
    }

    public boolean isOccupied(Point point) {
        return !this.isEmpty(point);
    }

    public char get(Point point) {
        return this.matrix[point.y][point.x];
    }

    public void validate(Point point) {
        int x = point.x;
        int y = point.y;
        if (
                x < 0
                || y < 0
                || x >= this.width
                || y >= this.height
        ) {
            throw new IllegalArgumentException("Invalid coordinate");
        }
    }

    public VisitedPoints createVisitedMatrix() {
        return new VisitedPoints(height, width);
    }

    @Override
    public Iterator<Point> iterator() {
        return new Iterator<>() {
            private int x = 0;
            private int y = 0;

            @Override
            public boolean hasNext() {
                return y < height;
            }

            @Override
            public Point next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                Point current = new Point(x, y);
                x++;
                if (x == width) {
                    x = 0;
                    y++;
                }

                return current;
            }
        };
    }

    List<Point> getOrthogonalNeighbors(Point point) {
        int x = point.x;
        int y = point.y;

        List<Point> result = new ArrayList<>();

        if (x > 0) {
            result.add(new Point(x - 1, y));
        }
        if (x < width - 1) {
            result.add(new Point(x + 1, y));
        }
        if (y > 0) {
            result.add(new Point(x, y - 1));
        }
        if (y < height - 1) {
            result.add(new Point(x, y + 1));
        }

        return result;
    }
}
