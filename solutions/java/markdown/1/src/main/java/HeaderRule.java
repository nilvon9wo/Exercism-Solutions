public class HeaderRule implements BlockRule {

    @Override
    public boolean canHandle(String line) {
        return line.startsWith("#");
    }

    @Override
    public Block parse(String line, ParseState state, InlineParser inline) {

        int count = 0;
        while (count < line.length() && line.charAt(count) == '#') {
            count++;
        }

        if (count > 6) {
            return new Block("<p>" + inline.parse(line) + "</p>", false, false);
        }

        String content = line.substring(count + 1);

        String html =
                "<h" + count + ">"
                + inline.parse(content)
                + "</h" + count + ">";

        return new Block(html, false, false);
    }
}