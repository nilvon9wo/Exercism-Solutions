import Plant.Plant

object Plant extends Enumeration {
  type Plant = Value
  val Radishes: Plant.Value = Value('R')
  val Clover: Plant.Value = Value('C')
  val Grass: Plant.Value = Value('G')
  val Violets: Plant.Value = Value('V')
}

class Garden(code: String) {
  def plants(studentName: String): List[Plant] =
    this.code.split("\n")
      .flatMap(extractPlantCodes(studentName))
      .map(Plant(_))
      .toList

  private def extractPlantCodes(studentName: String)(rowCode: String): List[Char] = {
    val studentPosition = Garden.position(studentName)
    List(
      rowCode.charAt(studentPosition * 2),
      rowCode.charAt(studentPosition * 2 + 1)
    )
  }
}

object Garden {
  val students = List(
    "Alice", "Bob", "Charlie", "David",
    "Eve", "Fred", "Ginny", "Harriet",
    "Ileana", "Joseph", "Kincaid", "Larry"
  )

  def position(studentName: String): Int =
    students.indexOf(studentName)

  def defaultGarden(code: String): Garden =
    new Garden(code)
}

