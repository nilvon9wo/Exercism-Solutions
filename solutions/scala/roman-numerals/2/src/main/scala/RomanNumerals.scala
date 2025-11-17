object RomanNumerals {
  def roman(i: Int)(implicit romanNumeral: String = ""): String =
    this.tryRoman(romanNumeral)
      .orElse(this.tryRoman(1000, "M", romanNumeral))
      .orElse(this.tryRoman(900, "CM", romanNumeral))
      .orElse(this.tryRoman(500, "D", romanNumeral))
      .orElse(this.tryRoman(400, "CD", romanNumeral))
      .orElse(this.tryRoman(100, "C", romanNumeral))
      .orElse(this.tryRoman(90, "XC", romanNumeral))
      .orElse(this.tryRoman(50, "L", romanNumeral))
      .orElse(this.tryRoman(40, "XL", romanNumeral))
      .orElse(this.tryRoman(10, "X", romanNumeral))
      .orElse(this.tryRoman(9, "IX", romanNumeral))
      .orElse(this.tryRoman(5, "V", romanNumeral))
      .orElse(this.tryRoman(4, "IV", romanNumeral))
      .orElse(this.tryRoman(1, "I", romanNumeral))
      .apply(i)

  private def tryRoman(romanNumeral: String = ""): PartialFunction[Int, String] = {
    case value
      if value == 0 =>
      romanNumeral
  }

  private def tryRoman(
                        upperGuard: Int,
                        token: String,
                        romanNumeral: String
                      ): PartialFunction[Int, String] = {
    case value
      if value >= upperGuard =>
      this.roman(value - upperGuard)(s"$romanNumeral$token")
  }
}