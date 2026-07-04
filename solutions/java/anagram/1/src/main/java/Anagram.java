import java.util.Arrays;
import java.util.List;
import java.util.Locale;

class Anagram {
    private final String normalizedWord;
    private final String signature;

    public Anagram(String word) {
        this.normalizedWord = normalizeWord(word);
        this.signature = sortLetters(normalizedWord);
    }

    public List<String> match(List<String> candidates) {
        return candidates.stream()
                         .filter(this::isAnagramCandidate)
                         .toList();
    }

    private boolean isAnagramCandidate(final String candidate) {
        String normalizedCandidate = this.normalizeWord(candidate);
        return !normalizedCandidate.equals(normalizedWord)
               && this.sortLetters(normalizedCandidate).equals(signature);
    }

    private String normalizeWord(String word) {
        return word.toLowerCase(Locale.ROOT);
    }

    private String sortLetters(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}