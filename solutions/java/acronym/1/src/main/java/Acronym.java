import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Acronym {
	private final String phrase;
	private String acronym;

	public Acronym(String phrase) {
		this.phrase = phrase;
	}

	public String get() {
		if (this.acronym == null) {
			this.acronym = createAcronym();
		}

		return this.acronym;
	}

	private String createAcronym() {
		return Arrays.stream(this.splitOnSpacesAndHyphens())
				       .map(this.removeNonLetters())
				       .filter(s -> !s.isEmpty())
				       .map(this.getFirstLetter())
				       .collect(Collectors.joining());
	}

	private String[] splitOnSpacesAndHyphens() {
		return this.phrase.split("[\\s-]+");
	}

	private Function<String, String> removeNonLetters() {
		return word -> word.replaceAll("[^\\p{L}]", "");
	}

	private Function<String, String> getFirstLetter() {
		return s -> String.valueOf(Character.toUpperCase(s.charAt(0)));
	}
}
