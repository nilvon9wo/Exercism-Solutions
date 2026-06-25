case class Board(board: List[String]):

    val height: Int = board.length
    val width: Int = if board.isEmpty
                     then 0
                     else board.head.length

    def containsSearchColor(position: Position, color: Color): Boolean =
        colorAt(position)
             .contains(color)
                 
    def colorAt(position: Position): Option[Color] =
        if isOutOfBounds(position)
        then None
        else Color.fromChar(cellAt(position))

    private def isOutOfBounds(position: Position): Boolean =
        position.row < 0
            || position.row >= height
            || position.column < 0
            || position.column >= width

    private def cellAt(position: Position): Char =
        board(position.row)(position.column)

    def positionsOnLeftEdge: List[Position] =
        (0 until height)
            .map(row => Position(row, 0))
            .toList

    def positionsOnTopEdge: List[Position] =
        (0 until width)
            .map(column => Position(0, column))
            .toList

