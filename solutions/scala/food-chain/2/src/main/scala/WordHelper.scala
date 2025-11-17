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
