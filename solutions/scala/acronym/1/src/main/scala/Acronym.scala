object Acronym {
  def abbreviate(phrase: String): String =
    phrase.trim()
      .replace("-", " ")
      .split(" ")
      .filterNot(_.isBlank)
      .map(_.charAt(0))
      .mkString("")
      .toUpperCase()
}
