object AtbashCipher {
  val cipherByPlain: Map[Char, Char] = Map(
    'a' → 'z',
    'b' → 'y',
    'c' → 'x',
    'd' → 'w',
    'e' → 'v',
    'f' → 'u',
    'g' → 't',
    'h' → 's',
    'i' → 'r',
    'j' → 'q',
    'k' → 'p',
    'l' → 'o',
    'm' → 'n',
    'n' → 'm',
    'o' → 'l',
    'p' → 'k',
    'q' → 'j',
    'r' → 'i',
    's' → 'h',
    't' → 'g',
    'u' → 'f',
    'v' → 'e',
    'w' → 'd',
    'x' → 'c',
    'y' → 'b',
    'z' → 'a',
    '0' → '0',
    '1' → '1',
    '2' → '2',
    '3' → '3',
    '4' → '4',
    '5' → '5',
    '6' → '6',
    '7' → '7',
    '8' → '8',
    '9' → '9'
  )

  def encode(input: String): String = {
    val encoding = input
      .toLowerCase
      .toList
      .map(cipherByPlain.getOrElse(_, ' '))
      .filterNot(_ == ' ')

    this.insertSpaces(encoding)
      .mkString("")
      .trim
  }

  @scala.annotation.tailrec
  private def insertSpaces(
                            encoding: List[Char],
                            spacedEncoding: List[Char] = List()
                          ): List[Char] =
    if (encoding.isEmpty) {
      spacedEncoding
    }
    else {
      this.insertSpaces(encoding.drop(5), spacedEncoding ::: encoding.take(5) ::: List(' ') )
    }


  def decode(input: String): String =
    this.encode(input)
      .replace(" ", "")

}