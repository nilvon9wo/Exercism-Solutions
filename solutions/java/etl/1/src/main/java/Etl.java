import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

class Etl {

    Map<String, Integer> transform(final Map<Integer, List<String>> oldScores) {
        return oldScores.entrySet()
                        .stream()
                        .flatMap(this::expandScoreEntryToLetterScores)
                        .collect(LetterScore.toLetterScoreMap());
    }

    private Stream<LetterScore> expandScoreEntryToLetterScores(
            final Entry<Integer, List<String>> scoreEntry
    ) {
        Integer key = scoreEntry.getKey();
        return scoreEntry.getValue()
                         .stream()
                         .map(letter -> LetterScore.from(key, letter));
    }
}
