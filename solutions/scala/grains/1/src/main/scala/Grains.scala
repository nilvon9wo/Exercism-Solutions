object Grains {
  def square(value: Int):Option[BigInt] =
    if (value < 1 || value > 64) {
      None
    }
    else {
      Some(BigInt(2).pow(value - 1))
    }

  def total:BigInt = (1 to 64)
    .flatMap(square)
    .sum
}
