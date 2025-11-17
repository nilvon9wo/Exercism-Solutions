object MapUtilities {
  implicit class RichMap(tokens: Map[Char, Int]) {
    def tokenize(equation: String): List[List[Int]] =
      equation.replace("==", "=")
              .replace(" ", "")
              .split("[+=]".toCharArray)
              .map(_.map(tokens).toList)
              .toList
  }
}
