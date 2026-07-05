public enum BoardContent {
    Ignored(' '),
    Empty('.'),
    Black('X'),
    White('O');

    BoardContent(char inputCharacter) {
        this.inputCharacter = inputCharacter;
    }

    private final char inputCharacter;
    public char getInputCharacter() {
        return inputCharacter;
    }
}