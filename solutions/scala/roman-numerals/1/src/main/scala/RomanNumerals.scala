object RomanNumerals {
  @scala.annotation.tailrec
  def roman(i: Int, romanNumeral: String = ""): String =
    i match {
      case x if x == 0 ⇒
        romanNumeral

      case x if x >= 1000 ⇒
        this.roman(i - 1000, s"${romanNumeral}M")

      case x if x >= 900 ⇒
        this.roman(i - 900, s"${romanNumeral}CM")

      case x if x >= 500 ⇒
        this.roman(i - 500, s"${romanNumeral}D")

      case x if x >= 400 ⇒
        this.roman(i - 400, s"${romanNumeral}CD")

      case x if x >= 100 ⇒
        this.roman(i - 100, s"${romanNumeral}C")

      case x if x >= 90 ⇒
        this.roman(i - 90, s"${romanNumeral}XC")

      case x if x >= 50 ⇒
        this.roman(i - 50, s"${romanNumeral}L")

      case x if x >= 40 ⇒
        this.roman(i - 40, s"${romanNumeral}XL")

      case x if x >= 10 ⇒
        this.roman(i - 10, s"${romanNumeral}X")

      case x if x >= 9 ⇒
        this.roman(i - 9, s"${romanNumeral}IX")

      case x if x >= 5 ⇒
        this.roman(i - 5, s"${romanNumeral}V")

      case x if x >= 4 ⇒
        this.roman(i - 4, s"${romanNumeral}IV")

      case x if x >= 1 ⇒
        this.roman(i - 1, s"${romanNumeral}I")
    }
}