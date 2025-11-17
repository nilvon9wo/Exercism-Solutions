object Extractor {
  def extractLetters(word: String): Array[Char] =
    word.toLowerCase.toCharArray.filter(character => character.isLetter && !character.isDigit)

  def combineCharacterFrequency(map1: Map[Char, Int], map2: Map[Char, Int])(character: Char): (Char, Int) =
    character -> (map1.getOrElse(character, 0) + map2.getOrElse(character, 0))
}
