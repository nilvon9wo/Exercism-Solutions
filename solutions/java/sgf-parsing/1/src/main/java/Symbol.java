public enum Symbol {
    GROUP_START('('),
    GROUP_END(')'),
    OPTION_START('['),
    OPTION_END(']'),
    PROPERTY_SEPARATOR(';'),
    NEW_LINE('\n'),
    RETURN('\r'),
    TAB('\t'),
    ESCAPE('\\'),
    SPACE(' ');

    Symbol(char value) {
        this.value = value;
    }

    private final char value;
    public char value() {
        return value;
    }
}