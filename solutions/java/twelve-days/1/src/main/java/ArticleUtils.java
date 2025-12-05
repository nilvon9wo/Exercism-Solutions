import java.util.regex.Pattern;

final class ArticleUtils {

    private static final Pattern VOWEL_REGEX = Pattern.compile("^[aeiouAEIOU]");

    static String prependIndefiniteArticle(String input) {
        String article = startsWithVowel(input)
                                 ? "an"
                                 : "a";
        return article + " " + input;
    }

    private static boolean startsWithVowel(String input) {
        return input != null && !input.isBlank()
                       && VOWEL_REGEX.matcher(input).find();
    }
}