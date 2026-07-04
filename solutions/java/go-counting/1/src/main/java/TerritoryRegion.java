import java.awt.*;
import java.util.*;

public class TerritoryRegion {
    private final Set<Point> region = new HashSet<>();

    Set<Point> getRegion() {
        return this.region;
    }

    @SuppressWarnings("UnusedReturnValue")
    TerritoryRegion add(Point point) {
        this.region.add(point);
        return this;
    }

    private final Set<Player> borderingPlayers = new HashSet<>();

    Set<Player> getBorderingPlayers() {
        return this.borderingPlayers;
    }

    @SuppressWarnings("UnusedReturnValue")
    TerritoryRegion add(Player player) {
        this.borderingPlayers.add(player);
        return this;
    }
}
