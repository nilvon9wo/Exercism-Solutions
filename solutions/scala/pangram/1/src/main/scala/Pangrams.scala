import scala.collection.immutable.NumericRange

object Pangrams {
  val alphabet: NumericRange.Inclusive[Char] = 'A' to 'Z'

  def isPangram(input: String): Boolean = {
    hasAllLetters(input)
  }

  private def hasAllLetters(letterList: String): Boolean =
    alphabet.forall(
      letterList
        .toUpperCase()
        .contains(_)
    )
}

