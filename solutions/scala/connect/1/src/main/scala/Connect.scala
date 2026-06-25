import Color._
class Connect(input: List[String]):
    val board = Board(input)

    def winner: Option[Color] =
        if hasPath(board, Color.Black)
        then Some(Color.Black)
        else Some(Color.White)
            .filter(color => hasPath(board, color))

    private def hasPath(board: Board, color: Color): Boolean =
        startPositions(board, color)
            .exists(start =>
                canReachWinningEdge(SearchState(board, start, color))
        )

    private def startPositions(board: Board, color: Color): List[Position] =
        color match
            case Color.Black =>
                positionsContainingColor(board, board.positionsOnLeftEdge, Color.Black)

            case Color.White =>
                positionsContainingColor(board, board.positionsOnTopEdge, Color.White)

    private def positionsContainingColor(
                                            board: Board,
                                            positions: List[Position],
                                            color: Color
                                        ): List[Position] =
        positions.filter(
            position => board.containsSearchColor(position, color)
        )

    private def canReachWinningEdge(searchState: SearchState): Boolean = {
        def canReachWinningCell =
            searchState.isWinningCell
                || searchState.position.neighbors.exists(
                neighbor => canReachWinningEdge(searchState.moveTo(neighbor))
            )

        !searchState.visited.contains(searchState.position)
            && searchState.containsSearchColor
            && canReachWinningCell
    }

