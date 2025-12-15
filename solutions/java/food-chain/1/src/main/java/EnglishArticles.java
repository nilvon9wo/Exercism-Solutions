import java.util.Set;

public final class EnglishArticles {
    private static final Set<Character> vowels = Set.of('a', 'e', 'i', 'o', 'u');

    public static String prefixArticle(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("text cannot be null or blank");
        }
        return startsWithVowel(text)
               ? "an " + text
               : "a " + text;
    }

    private static boolean startsWithVowel(String text) {
        final char character = text.charAt(0);
        char lowerCaseCharacter = Character.toLowerCase(character);
        return vowels.contains(lowerCaseCharacter);
    }
}