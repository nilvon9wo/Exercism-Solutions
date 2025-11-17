import scala.annotation.tailrec

object PrimeFactors {
  @tailrec
  def factors(number: Long, currentFactor: Long = 2, accumulator: List[Long] = Nil): List[Long] = {
    if (number <= 1) {
      accumulator
    }
    else {
      val nextFactor     = findNextFactor(currentFactor, number)
      val notYetFactored = number / nextFactor
      factors(notYetFactored, nextFactor, accumulator :+ nextFactor)
    }
  }


  @tailrec
  private def findNextFactor(factor: Long, notYetFactored: Long): Long =
    if (notYetFactored % factor == 0) {
      factor
    }
    else {
      findNextFactor(factor + 1, notYetFactored)
    }
}
