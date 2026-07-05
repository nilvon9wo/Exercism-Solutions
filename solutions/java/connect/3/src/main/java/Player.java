public enum Player
{
    Black(BoardContent.Black, Winner.PLAYER_X),
    White(BoardContent.White, Winner.PLAYER_O);

    final BoardContent boardContent;
    final Winner winner;
    Player(final BoardContent boardContent, final Winner winner) {
        this.boardContent = boardContent;
        this.winner = winner;
    }
}