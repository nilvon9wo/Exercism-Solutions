public class ParagraphRule implements BlockRule {

    @Override
    public boolean canHandle(String line) {
        return true;
    }

    @Override
    public Block parse(String line, ParseState state, InlineParser inline) {
        return new Block(
                "<p>" + inline.parse(line) + "</p>",
                false,
                false
        );
    }
}