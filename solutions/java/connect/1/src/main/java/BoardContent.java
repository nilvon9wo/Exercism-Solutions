public enum BoardContent {
    @InputCharacterAttribute(character = ' ')
    Ignored(' '),

    @InputCharacterAttribute(character = '.')
    Empty('.'),

    @InputCharacterAttribute(character = 'X')
    Black('X'),

    @InputCharacterAttribute(character = 'O')
    White('O');

    private char inputCharacter;

    BoardContent(char inputCharacter) {
        this.inputCharacter = inputCharacter;
    }
}
