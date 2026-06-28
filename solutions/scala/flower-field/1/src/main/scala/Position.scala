case class Position(row: Int, column: Int):

    def add(other: Position): Position =
        Position(row + other.row, column + other.column)