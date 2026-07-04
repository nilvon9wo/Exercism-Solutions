import java.awt.Point;
import java.util.*;

class GoCounting {
    Board board;
    Territories territories = new Territories();

    GoCounting(String boardInput) {
        this.board = Board.create(boardInput);
        this.computeTerritories();
    }

    private void computeTerritories() {
        VisitedPoints visited = board.createVisitedMatrix();
        for (Point point: board) {
            if (board.isEmpty(point) && visited.wasNotVisited(point)) {
                TerritoryRegion territoryRegion = new TerritoryFloodFiller(this.board, visited)
                        .fill(point);
                Player owner = this.determineOwner(territoryRegion.getBorderingPlayers());
                this.territories.assignTerritory(owner, territoryRegion.getRegion());
            }
        }
    }

    Player getTerritoryOwner(int x, int y) {
        return getTerritoryOwner(new Point(x, y));
    }

    private Player getTerritoryOwner(Point point) {
        board.validate(point);
        if (board.isOccupied(point)) {
            return Player.NONE;
        }

        return this.territories.getOwner(point);
    }

    Set<Point> getTerritory(int x, int y) {
        return this.getTerritory(new Point(x, y));
    }

    private Set<Point> getTerritory(Point point) {
        board.validate(point);
        return board.isOccupied(point)
               ? Collections.emptySet()
               : this.territories.getRegion(point);

    }

    Map<Player, Set<Point>> getTerritories() {
        return this.territories.getTerritories();
    }

    private Player determineOwner(Set<Player> borders) {
        return borders.size() == 1
               ? borders.contains(Player.BLACK)
                    ? Player.BLACK
                    : Player.WHITE
               : Player.NONE;
    }
}