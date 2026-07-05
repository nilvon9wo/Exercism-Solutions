import java.util.List;
public class BlockEngine {
    private final List<BlockRule> parsers;
    private final InlineParser inlineParser;

    public BlockEngine(List<BlockRule> parsers, InlineParser inlineParser) {
        this.parsers = parsers;
        this.inlineParser = inlineParser;
    }

    public Block parse(String line, ParseState state) {
        for (BlockRule parser : this.parsers) {
            if (parser.canHandle(line)) {
                return parser.parse(line, state, inlineParser);
            }
        }
        return new Block(line, false, false);
    }
}
