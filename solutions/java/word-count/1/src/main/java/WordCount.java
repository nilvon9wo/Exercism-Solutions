import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class WordCount {
    private static final Pattern LEADING_TRAILING_APOSTROPHES = Pattern.compile("^'+|'+$");
    private static final Pattern NON_WORD_CHARS = Pattern.compile("[^\\p{Alnum}'\\s]+");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    public Map<String, Integer> phrase(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input cannot be null or blank");
        }

        String[] words = this.tokenize(input);
        return Arrays.stream(words)
                       .filter(word -> !word.isBlank())
                       .collect(this.wordCountCollector());
    }

    private Collector<String, ?, Map<String, Integer>> wordCountCollector() {
        return Collectors.toMap(
                Function.identity(),
                word -> 1,
                Integer::sum
        );
    }

    private String[] tokenize(String input) {
        String[] rawTokens = this.splitIntoRawTokens(input);
        return Arrays.stream(rawTokens)
                       .map(this::stripLeadingAndTrailingApostrophes)
                       .toArray(String[]::new);
    }

    // Apostrophes need special handling because they appear inside contractions.
    private String stripLeadingAndTrailingApostrophes(String word) {
        return LEADING_TRAILING_APOSTROPHES.matcher(word)
                       .replaceAll("");
    }

    private String[] splitIntoRawTokens(String input) {
        String cleaned = NON_WORD_CHARS.matcher(input.toLowerCase())
                                 .replaceAll(" ")
                                 .trim();
        return WHITESPACE.split(cleaned);
    }
}
