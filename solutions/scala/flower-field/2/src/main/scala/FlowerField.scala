object FlowerField:
    def annotate(garden: List[String]): List[String] =
        Board(garden.toVector)
            .render

case class Position(row: Int, column: Int):
    def add(other: Position): Position =
        Position(row + other.row, column + other.column)

case class Board(grid: Vector[String]):
    private val height: Int = grid.length
    val width: Int = if height == 0
                     then 0
                     else grid.head.length

    private def inBounds(position: Position): Boolean =
        position.row >= 0
            && position.row < height
            && position.column >= 0
            && position.column < width

    def charAt(position: Position): Char =
        grid(position.row)(position.column)

    private def isFlower(position: Position): Boolean =
        charAt(position) == '*'

    private def countAdjacentFlowers(position: Position): Int =
        Board.directions.count {
            direction =>
                val neighbour = position.add(direction)
                inBounds(neighbour) && isFlower(neighbour)
        }

    private def renderCell(position: Position): Char =
        if isFlower(position)
        then '*'
        else
            val count = countAdjacentFlowers(position)
            if count == 0
            then ' '
            else ('0' + count).toChar

    def render: List[String] =
        (0 until height).toList.map {
            row =>
                (0 until width).map {
                    column => renderCell(Position(row, column))
                }.mkString
        }

object Board:
    private val directions: List[Position] =
        List(
            Position(-1, -1), Position(-1, 0), Position(-1, 1),
            Position(0, -1),              Position(0, 1),
            Position(1, -1),  Position(1, 0),  Position(1, 1)
        )