import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public record LetterScore(String letter, int score) {
    public LetterScore {
        letter = letter.toLowerCase();
    }

    public static LetterScore from(final int score, final String letter) {
        return new LetterScore(letter, score);
    }

    public static Collector<LetterScore, ?, Map<String, Integer>> toLetterScoreMap() {
        return Collectors.toMap(LetterScore::letter, LetterScore::score);
    }
}
