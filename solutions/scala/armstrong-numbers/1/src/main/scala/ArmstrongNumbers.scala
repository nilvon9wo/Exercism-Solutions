object ArmstrongNumbers {
  private val tolerance: Double = 1e-323

  def isArmstrongNumber(number: Int): Boolean =
    math.abs(number - sumOfDigitsToPowerOfLength(number)) < tolerance

  private def sumOfDigitsToPowerOfLength(number: Int): Double = {
    val numberString = number.toString
    numberString
      .toCharArray
      .map(x => sumOfDigitsToPowerOfLength(x, numberString.length))
      .sum
  }

  private def sumOfDigitsToPowerOfLength(x: Char, power: Int): Double =
    math.pow(x.toString.toInt, power)
}
