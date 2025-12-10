import java.util.Map;

class StateOfTicTacToe {

    public GameState determineState(String[] boardRows) {
        Map<CellContent, PlayerStatus> playerStatuses = GameBoard.fromRows(boardRows)
                                                                .getPlayerStatuses();
        this.validateMoveCounts(playerStatuses);
        this.checkForImpossibleWin(playerStatuses);
        return this.determineGameState(playerStatuses);
    }

    private void validateMoveCounts(Map<CellContent, PlayerStatus> playerStatuses) {
        long xMoves = playerStatuses.get(CellContent.X)
                              .moveCount();
        long oMoves = playerStatuses.get(CellContent.O)
                              .moveCount();

        if (xMoves < oMoves) {
            throw new IllegalArgumentException("Wrong turn order: O started");
        }

        if (xMoves - oMoves > 1) {
            throw new IllegalArgumentException("Wrong turn order: X went twice");
        }
    }

    private void checkForImpossibleWin(Map<CellContent, PlayerStatus> playerStatuses) {
        PlayerStatus xStatus = playerStatuses.get(CellContent.X);
        PlayerStatus oStatus = playerStatuses.get(CellContent.O);
        this.checkBothPlayersWon(xStatus, oStatus);
        this.checkXWonWrongTurnOrder(xStatus, oStatus);
        this.checkOWonWrongTurnOrder(xStatus, oStatus);
    }

    private void checkBothPlayersWon(PlayerStatus xStatus, PlayerStatus oStatus) {
        if (xStatus.hasWon() && oStatus.hasWon()) {
            throwImpossibleGame();
        }
    }

    private void checkXWonWrongTurnOrder(PlayerStatus xStatus, PlayerStatus oStatus) {
        if (xStatus.hasWon() && xStatus.moveCount() == oStatus.moveCount()) {
            throwImpossibleGame();
        }
    }

    private void checkOWonWrongTurnOrder(PlayerStatus xStatus, PlayerStatus oStatus) {
        if (oStatus.hasWon() && xStatus.moveCount() > oStatus.moveCount()) {
            throwImpossibleGame();
        }
    }

    private void throwImpossibleGame() {
        throw new IllegalArgumentException(
                "Impossible board: game should have ended after the game was won"
        );
    }

    private GameState determineGameState(Map<CellContent, PlayerStatus> playerStatuses) {
        PlayerStatus xStatus = playerStatuses.get(CellContent.X);
        PlayerStatus oStatus = playerStatuses.get(CellContent.O);
        long totalMoves = xStatus.moveCount()
                                 + oStatus.moveCount();
        return xStatus.hasWon() || oStatus.hasWon()
                       ? GameState.WIN
                       : totalMoves == 9
                                 ? GameState.DRAW
                                 : GameState.ONGOING;
    }
}