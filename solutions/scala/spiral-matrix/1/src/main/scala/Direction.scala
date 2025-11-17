object Direction
  extends Enumeration {
  implicit class DirectionExtensions(direction: Direction.Direction) {
    def next: Direction.Direction = direction match {
      case Direction.Right => Direction.Down
      case Direction.Down  => Direction.Left
      case Direction.Left  => Direction.Up
      case Direction.Up    => Direction.Right
      case _               => throw new NotImplementedError()
    }
  }

  type Direction = Value
  val Right, Down, Left, Up = Value
}

