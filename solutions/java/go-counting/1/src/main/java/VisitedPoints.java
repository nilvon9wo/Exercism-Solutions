import java.awt.*;

public class VisitedPoints {
    private final boolean[][] visited;
    VisitedPoints(int height, int width) {
        visited = new boolean[height][width];
    }

    boolean wasNotVisited(Point point) {
        return !visited[point.y][point.x];
    }

    @SuppressWarnings("UnusedReturnValue")
    VisitedPoints setVisited(Point point) {
        visited[point.y][point.x] = true;
        return this;
    }
}
