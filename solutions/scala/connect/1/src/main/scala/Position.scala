case class Position(row: Int, column: Int):
    def neighbors: List[Position] =
        List(
            Position(row - 1, column),
            Position(row - 1, column + 1),
            Position(row, column - 1),
            Position(row, column + 1),
            Position(row + 1, column - 1),
            Position(row + 1, column)
        )
