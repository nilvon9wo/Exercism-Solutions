import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PuzzleFactory {
    public Puzzle parsePuzzle(String puzzleInput) {
        String[] equationSides = puzzleInput.split("==");
        List<String> addendWords = this.parseAddendWords(equationSides[0]);
        String resultWord = equationSides[1].trim();
        return new Puzzle(
                addendWords,
                resultWord,
                this.collectAllUniqueLetters(addendWords, resultWord),
                this.determineLeadingLetters(addendWords, resultWord)
        );
    }

    private List<String> parseAddendWords(String addendSection) {
        return Arrays.stream(addendSection.split("\\+"))
                       .map(String::trim)
                       .collect(Collectors.toList());
    }

    private Set<Character> collectAllUniqueLetters(List<String> addendWords, String resultWord) {
        return this.collectFromAddendsAndResult(addendWords, resultWord, this::lettersInWord);
    }

    private Stream<Character> lettersInWord(String word) {
        return this.extractLettersFromWord(word)
                       .stream();
    }

    private Set<Character> extractLettersFromWord(String word) {
        return word.chars()
                       .mapToObj(character -> (char) character)
                       .collect(Collectors.toSet());
    }

    private Set<Character> determineLeadingLetters(List<String> addendWords, String resultWord) {
        return this.collectFromAddendsAndResult(addendWords, resultWord, word -> Stream.of(this.firstLetterOf(word)));
    }

    private Set<Character> collectFromAddendsAndResult(
            List<String> addendWords,
            String resultWord,
            Function<String, Stream<Character>> addendMapper
    ) {
        Stream<Character> addendStream = addendWords.stream()
                                                 .flatMap(addendMapper);
        Stream<Character> resultStream = addendMapper.apply(resultWord);
        return Stream.concat(addendStream, resultStream)
                       .collect(Collectors.toUnmodifiableSet());
    }

    private Character firstLetterOf(String word) {
        return word.charAt(0);
    }
}
