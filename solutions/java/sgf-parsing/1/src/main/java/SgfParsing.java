public class SgfParsing {

    public SgfNode parse(String input) throws SgfParsingException {
        if (input == null) {
            throw new SgfParsingException("Input cannot be null.");
        }

        Input cursor = new Input(input);
        cursor.moveNext();

        Result<Group> rootResult = GroupParser.parse(cursor);
        if (!rootResult.isValid()) {
            Exception ex = rootResult.getException();
            throw new SgfParsingException(
                    ex != null
                      ? ex.getMessage()
                      : "Something went wrong."
            );
        }

        return rootResult.getValue()
                         .toTree();
    }
}