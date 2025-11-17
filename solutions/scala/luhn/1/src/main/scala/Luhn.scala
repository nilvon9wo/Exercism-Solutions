object Luhn {
  def valid(input: String): Boolean = {
    val cleanInput = input.replace(" ", "")
    if (isInvalidInput(cleanInput)) {
      false
    }
    else {
      val sum = cleanInput
        .reverse
        .split("")
        .zipWithIndex
        .map(double)
        .map(reduceOverage)
        .sum

      sum % 10 == 0
    }
  }

  private def isInvalidInput(cleanInput: String) =
    cleanInput.length <= 1 || !cleanInput.matches("\\d+")

  private def double(characterWithIndex: (String, Int)): Int = {
    val (character, index) = characterWithIndex
    val value = character.toInt
    if (this.isEven(index + 1)) {
      value * 2
    }
    else {
      value
    }
  }

  private def isEven(i: Int) =
    i % 2 == 0

  private def reduceOverage(i: Int): Int =
    if (i > 9) {
      i - 9
    }
    else {
      i
    }
}