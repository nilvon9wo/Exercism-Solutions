import java.util.regex.Pattern;

public class ItalicRule implements InlineRule {
    private static final Pattern ITALIC = Pattern.compile("_(.+?)_");

    public String apply(String text) {
        return ITALIC.matcher(text).replaceAll("<em>$1</em>");
    }
}