import scala.annotation.tailrec

object FoodChain {
  @tailrec
  def recite(nextVerse: Int, endVerse: Int, verses: List[String] = Nil): String =
    if (nextVerse > endVerse) {
      verses.mkString
    }
    else {
      recite(nextVerse + 1, endVerse, verses :+ recite(nextVerse))
    }

  private def recite(verseNumber: Int): String = {
    val animal = Animal.getByVerse(verseNumber)
    val start  = s"I know an old lady who swallowed ${animal.indefiniteForm}."
    val verse  = animal.remark match {
      case Some(verse) => s"\n$verse"
      case _           => ""
    }
    new StringBuilder(start).append(verse)
                            .append(createRefrain(verseNumber))
                            .toString
  }

  private def createRefrain(verseNumber: Int): StringBuilder = {
    var stringBuilder: StringBuilder = new StringBuilder()
    if (verseNumber != Animal.lastAnimalNumber) {
      stringBuilder = stringBuilder.append("\n")
                                   .append(createSwallowLines(verseNumber))
                                   .append("I don't know why she swallowed the fly. Perhaps she'll die.\n\n")
    }
    else {
      stringBuilder = stringBuilder.append("\n\n")
    }
    stringBuilder
  }

  @tailrec
  private def createSwallowLines(nextAnimalNumber: Int, stringBuilder: StringBuilder = new StringBuilder())
  : StringBuilder =
    if (nextAnimalNumber == 1) {
      stringBuilder
    }
    else {
      val swallowLine: String = createSwallowLine(nextAnimalNumber)
      createSwallowLines(nextAnimalNumber - 1, stringBuilder.append(swallowLine))
    }

  private def createSwallowLine(animalNumber: Int): String = {
    val currentAnimal  = Animal.getByVerse(animalNumber)
    val previousAnimal = Animal.getByVerse(animalNumber - 1)
    val lineEnd        = if (previousAnimal.hasExtendedVerse) {
      createExtendedVerse(previousAnimal)
    }
                         else {
                           "."
                         }
    s"She swallowed the ${currentAnimal.name} to catch the ${previousAnimal.name}" + s"$lineEnd\n"
  }

  private def createExtendedVerse(previousAnimal: Animal) = {
    previousAnimal.remark match {
      case Some(verse) => verse.replace("It", " that")
    }
  }
}
