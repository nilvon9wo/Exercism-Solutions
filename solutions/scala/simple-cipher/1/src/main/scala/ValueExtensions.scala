object ValueExtensions {
  private val valueOfLowerCaseA = 'a'
  private val valueOfLowerCaseZ = 'z'
  private val alphabetSize   = valueOfLowerCaseZ - valueOfLowerCaseA;

  implicit class RichChar(val character: Char)
    extends AnyVal {
    def toZeroIndexedValue: Int =
      character.toInt - valueOfLowerCaseA
  }

  implicit class RichInt(val value: Int) extends AnyVal {
    def toZeroIndexedLetter: Char = {
      val characterValue = value match {
        case _ if value < 0 =>
          (valueOfLowerCaseZ + value + 1).toChar

        case _ if value > alphabetSize =>
          (valueOfLowerCaseA - alphabetSize + value - 1).toChar

        case _ =>
          (valueOfLowerCaseA + value).toChar
      }

      characterValue
    }
  }
}
