import java.util.List;

public class InlineEngine implements InlineParser {

    private final List<InlineRule> rules;

    public InlineEngine(List<InlineRule> rules) {
        this.rules = rules;
    }

    @Override
    public String parse(String text) {
        String result = text;
        for (InlineRule rule : rules) {
            result = rule.apply(result);
        }
        return result;
    }
}