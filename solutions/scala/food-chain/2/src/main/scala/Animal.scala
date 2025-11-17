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
