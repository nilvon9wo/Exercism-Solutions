import java.util.List;
import java.util.stream.StreamSupport;

public final class IterableUtilities {
    public static List<Cell> belongingTo(Iterable<Cell> cells, Player player) {
        BoardContent targetContent = player.boardContent;

        return StreamSupport.stream(cells.spliterator(), false)
                            .filter(cell -> cell.content() == targetContent)
                            .toList();
    }
}