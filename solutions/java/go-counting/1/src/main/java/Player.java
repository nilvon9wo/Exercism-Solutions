import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

enum Player {
    NONE(' ', false),
    BLACK('B', true),
    WHITE('W', true);

    private static final Map<Character, Player> BY_MARKER =
            Arrays.stream(values())
                  .collect(Collectors.toUnmodifiableMap(
                          player -> player.marker,
                          Function.identity()
                  ));

    final char marker;
    final boolean isStone;

    Player(char marker, boolean isStone) {
        this.marker = marker;
        this.isStone = isStone;
    }

    static Player get(char marker) {
        return BY_MARKER.get(marker);
    }
}