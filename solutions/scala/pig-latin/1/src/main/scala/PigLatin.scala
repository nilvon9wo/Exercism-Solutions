import scala.util.matching.Regex

object PigLatin {
  val VowelSounds: Regex = "(^[a|e|i|o|u]).*".r
  val ConstantSounds: Regex = "(^[b|c|ch|d|f|g|h|j|k|l|m|n|p|q|r|s|sch|t|th|thr|v|w|x|z]*).*".r
  val SometimesVowelLike: Regex = "(^[x|y]).*".r
  val suffix = "ay"

  def translate(english: String): String =
    english.split(" ")
      .map(this.translateWord)
      .mkString(" ")

  private def translateWord(word: String): String = {
    word match {
      case SometimesVowelLike(_*) =>
        this.handleWordBeginningWithSometimesVowelLikeLetter(word)

      case VowelSounds(_*) =>
        this.handleWordBeginningWithVowel(word)

      case ConstantSounds(_*) =>
        this.handleWordBeginningWithConstant(word)
    }
  }

  private def handleWordBeginningWithConstant(word: String): String = {
    val start: String = this.findFirstConstants(word)
    val length = start.length
    val (_, ending) = word.splitAt(length)

    if (
      start.last.toString == "q" &&
        ending.charAt(0).toString == "u"
    ) {
      val (_, ending2) = ending.splitAt(1)
      this.addSuffix(s"$ending2${start}u")
    }
    else {
      this.addSuffix(s"$ending$start")
    }
  }

  private def findFirstConstants(word: String): String = {
    ConstantSounds.findFirstMatchIn(word)
      .get
      .group(1)
  }

  private def handleWordBeginningWithVowel(word: String): String =
    this.addSuffix(word)

  private def addSuffix(word: String): String = s"$word$suffix"

  private def handleWordBeginningWithSometimesVowelLikeLetter(word: String): String = {
    val (firstLetter, ending) = word.splitAt(1)

    val capture = this.findFirstConstants(ending)
    if (capture != "") {
      this.addSuffix(word)
    }
    else {
      this.addSuffix(s"$ending$firstLetter")
    }
  }
}