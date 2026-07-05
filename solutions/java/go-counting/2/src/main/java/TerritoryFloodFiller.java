import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

final class TerritoryFloodFiller {
    private final Board board;
    private final VisitedPoints visited;
    private final TerritoryRegion territory = new TerritoryRegion();
    private final Deque<Point> frontier = new ArrayDeque<>();

    TerritoryFloodFiller(Board board, VisitedPoints visited) {
        this.board = board;
        this.visited = visited;
    }

    TerritoryRegion fill(Point startPoint) {
        this.frontier.push(startPoint);
        while (!this.frontier.isEmpty()) {
            this.visit(this.frontier.pop());
        }

        return territory;
    }

    private void visit(Point point) {
        if (!this.visited.wasNotVisited(point)) {
            return;
        }

        this.visited.setVisited(point);
        this.territory.add(point);
        this.board.getOrthogonalNeighbors(point)
             .forEach(this::processNeighbor);
    }

    private void processNeighbor(Point neighbor) {
        Player player = PlayerHelper.get(this.board.get(neighbor));
        if (player == Player.NONE) {
            this.enqueueIfUnvisited(neighbor);
            return;
        }

        this.territory.add(player);
    }

    private void enqueueIfUnvisited(Point neighbor) {
        if (this.visited.wasNotVisited(neighbor)) {
            this.frontier.push(neighbor);
        }
    }
}