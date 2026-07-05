import java.util.Map;
public class PlayerHelper {
    static final char EMPTY = ' ';
    private static final Map<Character, Player> PLAYER_BY_CHARACTER = Map.of(
            EMPTY, Player.NONE,
            'B', Player.BLACK,
            'W', Player.WHITE
    );

    static boolean isStone(Player player) {
        return player != Player.NONE;
    }

    static Player get(char marker) {
        return PLAYER_BY_CHARACTER.get(marker);
    }
}
