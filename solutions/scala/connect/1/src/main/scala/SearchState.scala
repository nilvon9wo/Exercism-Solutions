case class SearchState(
                  board: Board,
                  position: Position,
                  color: Color,
                  visited: Set[Position] = Set.empty
              ):

    def isWinningCell: Boolean =
        color match
            case Color.Black
            => position.column == board.width - 1

            case Color.White
            => position.row == board.height - 1

    def containsSearchColor: Boolean =
        board.containsSearchColor(position, color)

    def moveTo(nextPosition: Position): SearchState
    = SearchState(
        board,
        nextPosition,
        color,
        visited + position
    )


