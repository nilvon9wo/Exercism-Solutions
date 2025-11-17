import scala.annotation.tailrec

object House {
  private val phraseByNumber: Map[Int, String] = Map(
    1 -> "house that Jack built.",
    2 -> "malt that lay in",
    3 -> "rat that ate",
    4 -> "cat that killed",
    5 -> "dog that worried",
    6 -> "cow with the crumpled horn that tossed",
    7 -> "maiden all forlorn that milked",
    8 -> "man all tattered and torn that kissed",
    9 -> "priest all shaven and shorn that married",
    10 -> "rooster that crowed in the morn that woke",
    11 -> "farmer sowing his corn that kept",
    12 -> "horse and the hound and the horn that belonged to"
    )

  @tailrec
  def recite(nextVerse: Int, endVerse: Int, verses: List[String] = Nil): String =
    if (nextVerse > endVerse) {
      verses.mkString + "\n"
    }
    else {
      recite(nextVerse + 1, endVerse, verses :+ createVerse(nextVerse))
    }

  @tailrec
  private def createVerse(phraseNumber: Int, phrases: List[String] = Nil): String =
    if (phraseNumber == 0) {
      "This is " + phrases.mkString(" ") + "\n"
    }
    else {
      phraseByNumber.get(phraseNumber) match {
        case Some(phrase) => createVerse(phraseNumber - 1, phrases :+ s"the $phrase")
      }
    }
}
