import java.util.HashMap;
import java.util.Map;

class SqueakyClean {
	private static final Map<Character, Character> leetSpeakMap = new HashMap<>() {{
		put('0', 'o');
		put('1', 'l');
		put('3', 'e');
		put('4', 'a');
		put('5', 's');
		put('7', 't');
		put('8', 'b');
		put('9', 'g');
	}};

	public static String clean(String identifier) {
		if (identifier == null || identifier.isEmpty()) {
			return "";
		}

		String cleanIdentifier = identifier.replaceAll(" ", "_")
		                                   .chars()
		                                   .filter(x -> !Character.isISOControl(x))
		                                   .mapToObj(x -> String.valueOf((char) x))
		                                   .reduce("", (seed, part) -> seed + part);

		String camelIdentifier = convertDashToCamelCase(cleanIdentifier);
		String letters = replaceLeetSpeak(camelIdentifier);
		return removeIllegalCharacters(letters);
	}

	private static String convertDashToCamelCase(String input) {
		StringBuilder stringBuilder = new StringBuilder();
		boolean caseFlag = false;
		for (char character : input.toCharArray()) {
			if (character == '-') {
				caseFlag = true;
			}
			else if (caseFlag || Character.isUpperCase(character)) {
				stringBuilder.append(Character.toUpperCase(character));
				caseFlag = false;
			}
			else {
				stringBuilder.append(Character.toLowerCase(character));
			}
		}
		return stringBuilder.toString();
	}

	private static String replaceLeetSpeak(String characters) {
		StringBuilder stringBuilder = new StringBuilder();
		for (char character : characters.toCharArray()) {
			stringBuilder.append(leetSpeakMap.getOrDefault(character, character));
		}
		return stringBuilder.toString();
	}

	public static String removeIllegalCharacters(String str) {
		StringBuilder stringBuilder = new StringBuilder();
		for (char character : str.toCharArray()) {
			if (isLegalLetter(character) || character == '_') {
				stringBuilder.append(character);
			}
		}
		return stringBuilder.toString();
	}

	private static boolean isLegalLetter(char character) {
		return Character.isLetter(character)
		       && Character.UnicodeBlock.of(character) == Character.UnicodeBlock.BASIC_LATIN;
	}
}
