object SumOfMultiples {
  def sum(factors: Set[Int], limit: Int): Int = {
    factors
      .flatMap(find_multiples(_, limit))
      .sum
  }

  private def find_multiples(factor: Int, upper_limit: Int): List[Int] = {
    (1 to ((upper_limit - 1) / factor))
      .toList
      .map(_ * factor)
  }
}

