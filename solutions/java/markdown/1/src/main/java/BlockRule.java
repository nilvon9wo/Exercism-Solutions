public interface BlockRule {
    boolean canHandle(String line);
    Block parse(String line, ParseState state, InlineParser inline);
}