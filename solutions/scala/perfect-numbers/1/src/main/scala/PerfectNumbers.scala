import NumberType.NumberType

object NumberType extends Enumeration {
  type NumberType = Value
  val Perfect, Abundant, Deficient = Value
}

object PerfectNumbers {
  def classify(integer: Int): Either[String, NumberType] =
    integer match {
      case value if value < 1 =>
        Left("Classification is only possible for natural numbers.")

      case value if value == 1 =>
        Right(NumberType.Deficient)

      case value if value > 1 =>
        Right(findNumberType(integer))
    }

  private def findNumberType(integer: Int) =
    this.findFactors(integer).sum match {
      case sum if sum < integer =>
        NumberType.Deficient

      case sum if sum == integer =>
        NumberType.Perfect

      case sum if sum > integer =>
        NumberType.Abundant
    }

  private def findFactors(integer: Int): List[Int] =
    (1 until (integer - 1))
      .toList
      .filter(this.isFactor(integer, _))

  private def isFactor(target: Int, candidate: Int): Boolean =
    target % candidate == 0
}

