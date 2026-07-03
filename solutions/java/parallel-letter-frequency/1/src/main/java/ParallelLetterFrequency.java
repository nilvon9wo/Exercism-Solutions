import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class ParallelLetterFrequency {

    private final String[] texts;

    ParallelLetterFrequency(String[] texts) {
        this.texts = texts;
    }

    Map<Character, Integer> countLetters() {
        return Arrays.stream(texts)
                     .parallel()
                     .map(this::countLettersInText)
                     .reduce(new HashMap<>(), this::mergeCounts);
    }

    private Map<Character, Integer> countLettersInText(String text) {
        return text.chars()
                   .map(Character::toLowerCase)
                   .filter(Character::isLetter)
                   .mapToObj(character -> (char) character)
                   .collect(createMap());
    }

    private static Collector<Character, ?, HashMap<Character, Integer>> createMap() {
        return Collectors.toMap(
                character -> character,
                character -> 1,
                Integer::sum,
                HashMap::new
        );
    }

    private HashMap<Character, Integer> mergeCounts(
            Map<Character, Integer> left,
            Map<Character, Integer> right) {

        HashMap<Character, Integer> countByCharacters = new HashMap<>(left);
        right.forEach((character, count) ->
                              countByCharacters.merge(character, count, Integer::sum));
        return countByCharacters;
    }
}