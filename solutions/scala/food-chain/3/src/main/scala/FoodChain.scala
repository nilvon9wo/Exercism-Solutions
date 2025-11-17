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

sealed trait Animal {
  val remark: Option[String]
  val hasExtendedVerse: Boolean = false

  def indefiniteForm: String = WordHelper.indefiniteForm(name)

  def name: String = this.getClass.getSimpleName.toLowerCase.replace("$", "")
}

object Animal {
  private val allAnimals: List[Animal] = List(Fly, Spider, Bird, Cat, Dog, Goat, Cow, Horse)
  val lastAnimalNumber: Int          = allAnimals.length

  def getByVerse(number: Int): Animal = {
    val animal = allAnimals(number - 1)
    animal
  }

  private case object Fly
    extends Animal {
    val remark: Option[String] = None
  }

  private case object Spider
    extends Animal {
    override val hasExtendedVerse: Boolean = true
    val remark: Option[String] = Some("It wriggled and jiggled and tickled inside her.")
  }

  private case object Bird
    extends Animal {
    val remark: Option[String] = Some("How absurd to swallow a bird!")
  }

  private case object Cat
    extends Animal {
    val remark: Option[String] = Some("Imagine that, to swallow a cat!")
  }

  private case object Dog
    extends Animal {
    val remark: Option[String] = Some("What a hog, to swallow a dog!")
  }

  private case object Goat
    extends Animal {
    val remark: Option[String] = Some("Just opened her throat and swallowed a goat!")
  }

  private case object Cow
    extends Animal {
    val remark: Option[String] = Some("I don't know how she swallowed a cow!")
  }

  private case object Horse
    extends Animal {
    val remark: Option[String] = Some("She's dead, of course!")
  }
}

object WordHelper {

  private val vowels = Set('A', 'E', 'I', 'O', 'U')

  def indefiniteForm(word: String): String = {
    if (startsWithVowel(word)) {
      s"an $word"
    }
    else {
      s"a $word"
    }
  }

  private def startsWithVowel(word: String): Boolean = vowels.contains(word.head.toUpper)
}
