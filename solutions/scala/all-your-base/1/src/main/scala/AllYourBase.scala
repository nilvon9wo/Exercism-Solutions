object AllYourBase {
  def rebase(
              originalBase: Int,
              originalCharacters: List[Int],
              newBase: Int
            ): Option[List[Int]] =
    if (hasInvalidInput(originalBase, originalCharacters, newBase)) {
      None
    }
    else {
      Some(this.convert(originalBase, originalCharacters, newBase))
    }

  private def hasInvalidInput(originalBase: Int, originalCharacters: scala.List[Int], newBase: Int): Boolean =
    originalBase <= 1 ||
      newBase <= 1 ||
      originalCharacters.exists(_ < 0) ||
      originalCharacters.exists(_ >= originalBase)

  private def convert(originalBase: Int, originalCharacters: List[Int], newBase: Int): List[Int] = {
    val cleanCharacters = originalCharacters.dropWhile(_ == 0)
    val base10 = this.rebaseToBase10(originalBase, cleanCharacters)
    this.returnAsTargetBase(newBase, base10)
  }

  private def returnAsTargetBase(newBase: Int, base10: Int): List[Int] =
    if (base10 == 0)
      List(0)
    else if (newBase == 10) {
      this.toIntList(base10)
    }
    else {
      this.rebaseToNewBase(newBase, base10)
    }

  @scala.annotation.tailrec
  private def rebaseToBase10(
                              originalBase: Int,
                              originalCharacters: List[Int],
                              power: Int = 0,
                              accumulated: Int = 0
                            ): Int =
    if (power == originalCharacters.size) {
      accumulated
    } else {
      this.rebaseToBase10(
        originalBase,
        originalCharacters,
        power + 1,
        addTranslatedValue(originalBase, originalCharacters, power, accumulated)
      )
    }

  private def addTranslatedValue(
                                  originalBase: Int,
                                  originalCharacters: List[Int],
                                  power: Int,
                                  accumulated: Int
                                ) = {
    val nextValuePosition = originalCharacters.size - power - 1
    val multiplier = Math.pow(originalBase, power)
    val newValue = originalCharacters(nextValuePosition) * multiplier
    accumulated + newValue.toInt
  }

  private def toIntList(base10: Int): List[Int] =
    base10.toString
      .split("")
      .map(_.toInt)
      .toList

  private def rebaseToNewBase(newBase: Int, base10: Int): List[Int] =
    this.rebaseToNewBase(newBase, base10, this.highestPowerOfBase(newBase, base10))

  @scala.annotation.tailrec
  private def rebaseToNewBase(
                               newBase: Int,
                               base10: Int,
                               highestPower: Int,
                               accumulated: List[Int] = List()
                             ): List[Int] =
    if (highestPower < 1) {
      accumulated.reverse
    }
    else {
      this.rebaseToNewBase(
        newBase,
        base10 % highestPower,
        highestPower / newBase,
        base10 / highestPower :: accumulated
      )
    }

  @scala.annotation.tailrec
  private def highestPowerOfBase(
                                  base: Int,
                                  number: Int,
                                  lastAttempted: Int = 1
                                ): Int = {
    val nextAttempt = lastAttempted * base
    if (nextAttempt > number) {
      lastAttempted
    }
    else {
      this.highestPowerOfBase(base, number, nextAttempt)
    }
  }
}