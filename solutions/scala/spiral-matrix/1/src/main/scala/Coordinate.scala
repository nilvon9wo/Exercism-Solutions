class Coordinate(val X: Int, val Y: Int) {

  def next(matrix: List[List[Int]], direction: Direction.Direction): Movement = {
    require(matrix != null, "result array must not be null")

    direction match {
      case Direction.Right if cantUseNext(matrix, X, Y + 1) =>
        Movement(new Coordinate(X + 1, Y), direction.next)

      case Direction.Right =>
        Movement(new Coordinate(X, Y + 1), direction)

      case Direction.Down if cantUseNext(matrix, X + 1, Y) =>
        Movement(new Coordinate(X, Y - 1), direction.next)

      case Direction.Down =>
        Movement(new Coordinate(X + 1, Y), direction)

      case Direction.Left if cantUseNext(matrix, X, Y - 1) =>
        Movement(new Coordinate(X - 1, Y), direction.next)

      case Direction.Left =>
        Movement(new Coordinate(X, Y - 1), direction)

      case Direction.Up if cantUseNext(matrix, X - 1, Y) =>
        Movement(new Coordinate(X, Y + 1), direction.next)

      case Direction.Up =>
        Movement(new Coordinate(X - 1, Y), direction)
    }
  }

  private def cantUseNext(matrix: List[List[Int]], x: Int, y: Int): Boolean = {
    (x < 0) ||
    (y < 0) ||
    (x >= matrix.length) ||
    (y >= matrix.head.length) ||
    (matrix(x)(y) != 0)
  }
}
