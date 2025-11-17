import scala.util.matching.Regex

object RunLengthEncoding {
  val encodingPattern = new Regex("""(\d*)?(.)(.*)?""", "quantity", "character", "remainder")

  @scala.annotation.tailrec
  def decode(
              string: String,
              accumulated: List[String] = List()
            ): String =
    if (string.isEmpty) {
      accumulated.reverse
        .mkString("")
    }
    else {
      val result = this.encodingPattern.findFirstMatchIn(string).get
      val quantity = calculateQuantity(result)
      val characters = repeatCharacter(result, quantity)
      val remainder = extractRemainder(result)
      this.decode(remainder, characters :: accumulated)
    }

  @scala.annotation.tailrec
  def encode(
              string: String,
              accumulated: List[(Char, Int)] = List()
            ): String =
    if (string.isEmpty) {
      this.toEncodedString(accumulated)
    }
    else {
      val firstChracter = string.head
      val headCount = string.takeWhile(_ == firstChracter).length
      this.encode(string.substring(headCount), (firstChracter, headCount) :: accumulated)
    }

  def toEncodedString(characterCount: (Char, Int)): String = {
    val (character, count) = characterCount
    if (count > 1) {
      s"$count$character"
    }
    else {
      s"$character"
    }
  }

  private def extractRemainder(result: Regex.Match) = {
    val remainder = result.group("remainder")
    if (remainder.isBlank) {
      ""
    }
    else {
      remainder
    }
  }

  private def repeatCharacter(result: Regex.Match, quantity: Int) =
    (1 to quantity)
      .map(_ ⇒ result.group("character"))
      .mkString("")

  private def calculateQuantity(result: Regex.Match): Int = {
    val regexResult = result.group("quantity")
    if (regexResult.isBlank) {
      1
    }
    else {
      regexResult.toInt
    }
  }

  private def toEncodedString(accumulated: List[(Char, Int)]): String =
    accumulated
      .reverse
      .map(this.toEncodedString)
      .mkString("")
}
