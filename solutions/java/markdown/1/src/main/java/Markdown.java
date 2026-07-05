import java.util.regex.Pattern;

class Markdown {
    private static final Pattern NEWLINE = Pattern.compile("\n");
    private final InlineParser inlineParse = new InlineEngine(MarkdownEngine.INLINE_RULES);
    private final BlockEngine blockParse = new BlockEngine(MarkdownEngine.BLOCK_RULES, inlineParse);

    String parse(String markdown) {
        String[] lines = NEWLINE.split(markdown);
        return parse(lines);
    }

    private String parse(final String[] lines) {
        ParseState state = new ParseState();
        for (String line : lines) {
            boolean isListItem = MarkdownEngine.LIST_PARSER.canHandle(line);
            if (state.hasActiveList() && !isListItem) {
                state.append("</ul>");
                state.deactivateList();
            }

            Block block = blockParse.parse(line, state);
            if (block.opensList()) {
                state.append("<ul>");
            }

            state.append(block.html());
        }

        if (state.hasActiveList()) {
            state.append("</ul>");
            state.deactivateList();
        }

        return state.toString();
    }
}