import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.function.IntPredicate;

class PigLatinTranslator {

    private static final List<String> SPECIAL_CLUSTERS =
            Arrays.asList("squ", "sch", "thr", "th", "ch", "qu");

    public String translate(String phrase) {
        return Arrays.stream(phrase.split(" "))
                       .map(this::translateWord)
                       .collect(Collectors.joining(" "));
    }

    private String translateWord(String word) {
        return this.startsWithVowelSound(word)
                       ? (word + "ay")
                       : this.translateConsonantWord(word);
    }

    private boolean startsWithVowelSound(String word) {
        return word.matches("^[aeiou].*")
                       || word.startsWith("xr")
                       || word.startsWith("yt");
    }

    private String translateConsonantWord(String word) {
        String cluster = this.extractCluster(word);
        String tail = word.substring(cluster.length());
        return tail + cluster + "ay";
    }

    private String extractCluster(String word) {
        return this.findSpecialCluster(word)
                       .orElseGet(() -> this.simpleCluster(word));
    }

    private Optional<String> findSpecialCluster(String word) {
        return SPECIAL_CLUSTERS.stream()
                       .filter(word::startsWith)
                       .findFirst();
    }

    private String simpleCluster(String word) {
        return IntStream.range(0, word.length())
                       .filter(this.stopsCluster(word))
                       .boxed()
                       .findFirst()
                       .map(index -> word.substring(0, index))
                       .orElse(word);
    }

    private IntPredicate stopsCluster(String word) {
        return index ->
                       this.isVowel(word.charAt(index))
                               || this.isYAfterFirstConsonant(word, index);
    }

    private boolean isYAfterFirstConsonant(String word, int index) {
        boolean isY = word.charAt(index) == 'y';
        boolean notFirst = index > 0;
        return isY && notFirst;
    }

    private boolean isVowel(char c) {
        return "aeiou".indexOf(c) >= 0;
    }
}
