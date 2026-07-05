import java.util.regex.Pattern;

public class BoldRule implements InlineRule {
    private static final Pattern BOLD = Pattern.compile("__(.+?)__");

    public String apply(String text) {
        return BOLD.matcher(text).replaceAll("<strong>$1</strong>");
    }
}