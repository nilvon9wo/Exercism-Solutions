import java.awt.*;
import java.util.*;

public class Territories {
    private final Map<Player, Set<Point>> territories = createTerritories();
    Map<Player, Set<Point>> getTerritories() {
        return this.territories;
    }

    private Map<Player, Set<Point>> createTerritories() {
        Map<Player, Set<Point>> territories = new EnumMap<>(Player.class);
        for (Player player : Player.values()) {
            territories.put(player, new HashSet<>());
        }

        return territories;
    }

    private final Map<Point, Player> ownerByPoint = new HashMap<>();
    Player getOwner(Point point) {
        Set<Point> region = this.getRegion(point);
        return this.getOwner(region);
    }
    Player getOwner(Set<Point> region) {
        final Point firstPoint = region.iterator()
                                 .next();
        return this.ownerByPoint.getOrDefault(firstPoint, Player.NONE);
    }

    private final Map<Point, Set<Point>> regionCache = new HashMap<>();
    Set<Point> getRegion(Point point) {
        return this.regionCache.getOrDefault(point, new HashSet<>());
    }

    @SuppressWarnings("UnusedReturnValue")
    Territories assignTerritory(Player owner, Set<Point> region) {
        this. territories.get(owner)
                         .addAll(region);

        region.forEach(point -> this.ownerByPoint.put(point, owner));
        this.addRegion(region);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    private Territories addRegion(Set<Point> region) {
        for (Point point : region) {
            this.regionCache.put(point, region);
        }
        return this;
    }
}
