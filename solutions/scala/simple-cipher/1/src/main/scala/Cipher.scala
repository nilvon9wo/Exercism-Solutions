import ValueExtensions.{RichChar, RichInt}

case class Cipher(key: String) {
  def encode(input: String): String =
    convert(
      input, (character, index) => {
        val plainCharacterValue = character.toZeroIndexedValue
        val keyCharacterValue   = getKeyCharacterValue(index)
        (plainCharacterValue + keyCharacterValue)
          .toZeroIndexedLetter
      })

  def decode(input: String): String =
    convert(
      input, (_, index) => {
        val plainCharacterValue = input(index).toZeroIndexedValue
        val keyCharacterValue   = getKeyCharacterValue(index)
        (plainCharacterValue - keyCharacterValue)
          .toZeroIndexedLetter
      })

  private def convert(text: String, convertFunction: (Char, Int) => Char): String =
    text.zipWithIndex
        .map(convertFunction.tupled)
        .mkString

  private def getKeyCharacterValue(i: Int): Int = {
    val keyIndex = i % key.length
    key(keyIndex).toZeroIndexedValue
  }
}

object Cipher {
  def apply(keyOption: Option[String]): Cipher = {
    val key: String = keyOption match {
      case Some(key) if key.exists(_.isUpper) => throw new IllegalArgumentException("Capitals not allowed in key.")
      case Some(key) if key.exists(_.isDigit) => throw new IllegalArgumentException("Numbers not allowed in key.")
      case Some(key) if key.isEmpty           => throw new IllegalArgumentException("Value required in key.")
      case Some(key)                          => key
      case None                               => RandomKeyGenerator.create
    }
    Cipher(key)
  }
}




