import java.util.HashSet;
import java.util.Locale;

class IsogramChecker {
	boolean isIsogram(String phrase) {
		if (phrase == null || phrase.isEmpty()) {
			return true;
		}

		HashSet<Character> seen = new HashSet<>();
		return phrase.toLowerCase(Locale.ROOT)
				       .chars()
				       .filter(Character::isLetter)
				       .mapToObj(character -> (char) character)
				       .allMatch(seen::add);
	}
}
