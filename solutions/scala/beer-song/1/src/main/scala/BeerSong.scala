object BeerSong {

  private val noMoreBottleVerse =
    "No more bottles of beer on the wall, no more bottles of beer.\nGo to the store and buy some more, 99 bottles of beer on the wall.\n"

  def recite(startBottleCount: Int, takeDown: Int): String = {
    (startBottleCount - takeDown + 1 to startBottleCount)
      .reverse
      .map {
             case x if x > 0 => recite(x)
             case _          =>
               noMoreBottleVerse
           }
      .mkString("\n")
  }

  private def recite(startBottleCount: Int) = {
    val firstLine  = reciteFirstLine(startBottleCount)
    val secondLine = reciteSecondLine(startBottleCount)
    s"$firstLine\n$secondLine\n"
  }

  private def reciteFirstLine(startBottleCount: Int): String = {
    val startBottleNoun = getBottleNoun(startBottleCount)
    s"$startBottleCount $startBottleNoun of beer on the wall, $startBottleCount $startBottleNoun of beer."
  }

  private def reciteSecondLine(startBottleCount: Int): String = {
    val remainingBottleCount = startBottleCount - 1
    if (remainingBottleCount > 0) {
      val remainingBottleNoun = getBottleNoun(remainingBottleCount)
      return s"Take one down and pass it around, $remainingBottleCount $remainingBottleNoun of beer on the wall."
    }
    "Take it down and pass it around, no more bottles of beer on the wall."
  }

  private def getBottleNoun(count: Int) =
    if (count == 1) {
      "bottle"
    }
    else {
      "bottles"
    }
}
