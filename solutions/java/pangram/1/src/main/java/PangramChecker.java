import java.util.Set;
import java.util.stream.Collectors;

public class PangramChecker {
    public boolean isPangram(String input) {
        Set<Character> uniqueLetters = this.extractUniqueCharacters(input);
		return uniqueLetters.size() == 26;
    }

	private Set<Character> extractUniqueCharacters(String input) {
		return  input.chars()
                      .mapToObj(character -> (char) character)
                      .filter(Character::isLetter)
                      .map(Character::toLowerCase)
                      .collect(Collectors.toSet());
	}
}
