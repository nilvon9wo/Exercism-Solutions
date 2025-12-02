import java.util.Map;
import java.util.Set;

class Scrabble {
	private static final MultiKeyMap<Character, Integer> SCORE_BY_LETTERS = MultiKeyMap.fromMapOfSets(Map.of(
			Set.of('A', 'E', 'I', 'O', 'U', 'L', 'N', 'R', 'S', 'T'), 1,
			Set.of('D', 'G'), 2,
			Set.of('B', 'C', 'M', 'P'), 3,
			Set.of('F', 'H', 'V', 'W', 'Y'), 4,
			Set.of('K'), 5,
			Set.of('J', 'X'), 8,
			Set.of('Q', 'Z'), 10
	));

	private final String word;
	Scrabble(String word) {
        this.word = word.toUpperCase();
    }

	int score = -1;
    int getScore() {
        if (this.score == -1) {
			this.score = this.word.chars()
					             .mapToObj(c -> (char) c)
					             .map(SCORE_BY_LETTERS::get)
					             .mapToInt(Integer::intValue)
					             .sum();
        }
		
		return this.score;
    }
}
