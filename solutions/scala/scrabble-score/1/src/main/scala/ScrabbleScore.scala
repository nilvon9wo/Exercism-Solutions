object ScrabbleScore {
  val pointsByValues: Map[List[String], Int] = Map(
    List("A", "E", "I", "O", "U", "L", "N", "R", "S", "T") → 1,
    List("D", "G") → 2,
    List("B", "C", "M", "P") → 3,
    List("F", "H", "V", "W", "Y") → 4,
    List("K") → 5,
    List("J", "X") → 8,
    List("Q", "Z") → 10
  )

  def score(string: String): Int =
    string
      .toUpperCase()
      .split("")
      .map(convertToPoints)
      .sum

  private def convertToPoints(letter: String): Int =
    pointsByValues
      .map(evaluate(_, letter))
      .sum

  private def evaluate(letterPointsPair: (List[String], Int), letter: String): Int = {
    val (letters, points) = letterPointsPair
    if (letters.contains(letter)) {
      points
    }
    else {
      0
    }
  }
}