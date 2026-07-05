import java.util.List;

class MarkdownEngine {
    static final ListRule LIST_PARSER = new ListRule();
    static final List<BlockRule> BLOCK_RULES = List.of(
            new HeaderRule(),
            LIST_PARSER,
            new ParagraphRule()
    );

    static final List<InlineRule> INLINE_RULES = List.of(
            new BoldRule(),
            new ItalicRule()
    );
}