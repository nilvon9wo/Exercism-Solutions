import scala.collection.immutable.NumericRange

object Isogram {
  val alphabet: NumericRange.Inclusive[Char] = 'A' to 'Z'

  def isIsogram(sentence: String): Boolean =
    containsEachLetterJustOnce(
      sentence
        .toUpperCase()
        .filter(alphabet.contains(_))
        .split("")
    )

  def containsEachLetterJustOnce(sentence: Array[String]): Boolean =
    sentence.length == sentence.distinct.length
}