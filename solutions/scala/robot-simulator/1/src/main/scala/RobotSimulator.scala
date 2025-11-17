import Bearing.Bearing

case class Robot(bearing: Bearing, coordinates: (Int, Int)) {
  private val methodByCharacters: Map[Char, Robot => Robot] = Map(
    'L' -> (robot => robot.turnLeft),
    'R' -> (robot => robot.turnRight),
    'A' -> (robot => robot.advance)
    )

  def turnLeft: Robot =
    bearing match {
      case Bearing.North => Robot(Bearing.West, coordinates)
      case Bearing.East  => Robot(Bearing.North, coordinates)
      case Bearing.South => Robot(Bearing.East, coordinates)
      case Bearing.West  => Robot(Bearing.South, coordinates)
    }

  def turnRight: Robot =
    bearing match {
      case Bearing.North => Robot(Bearing.East, coordinates)
      case Bearing.East  => Robot(Bearing.South, coordinates)
      case Bearing.South => Robot(Bearing.West, coordinates)
      case Bearing.West  => Robot(Bearing.North, coordinates)
    }

  def advance: Robot =
    bearing match {
      case Bearing.North => Robot(bearing, (coordinates._1, coordinates._2 + 1))
      case Bearing.East  => Robot(bearing, (coordinates._1 + 1, coordinates._2))
      case Bearing.South => Robot(bearing, (coordinates._1, coordinates._2 - 1))
      case Bearing.West  => Robot(bearing, (coordinates._1 - 1, coordinates._2))
    }

  def simulate(instructions: String): Robot =
    instructions
      .map(instruction => methodByCharacters(instruction))
      .foldLeft(this)((robot, instruction) => instruction(robot))

}

object Bearing
  extends Enumeration {
  type Bearing = Value
  val North, South, East, West = Value
}



