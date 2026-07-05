public class ListRule implements BlockRule {

    @Override
    public boolean canHandle(String line) {
        return line.startsWith("* ");
    }

    @Override
    public Block parse(String line, ParseState state, InlineParser inline) {

        String content = line.substring(2);
        String item = "<li>" + inline.parse(content) + "</li>";

        boolean opens = state.hasNoActiveList();

        if (opens) {
            state.activateList();
        }

        return new Block(item, opens, false);
    }
}