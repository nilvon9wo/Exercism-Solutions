import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

class WordSearcher {

    Map<String, Optional<WordLocation>> search(final Set<String> words, final char[][] grid) {
        Grid searchGrid = new Grid(grid);

        return words.stream()
                    .collect(Collectors.toMap(
                            identityKey(),
                            searchGrid::findWord,
                            (existing, replacement) -> replacement
                    ));
    }

    private static Function<String, String> identityKey() {
        return word -> word;
    }
}