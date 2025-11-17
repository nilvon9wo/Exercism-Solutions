import scala.collection.mutable

object Sieve {
  def primes(integer: Int): List[Int] =
    if (integer == 1) {
      List()
    }
    else {
      primes(2 to integer)
    }


  def primes(integerRange: Range.Inclusive): List[Int] = {
    val mutableMarkedRange = createMarkableRange(integerRange)

    integerRange.foreach { factor ⇒
      ((factor * 2) to integerRange.last by factor)
        .foreach {
          mutableMarkedRange.put(_, true)
        }
    }

    mutableMarkedRange
      .filterNot { case (_, isMarked) ⇒ isMarked }
      .map { case (value, _) ⇒ value }
      .toList
      .sorted
  }

  private def createMarkableRange(integerRange: Range.Inclusive): mutable.Map[Int, Boolean] =
    mutable.Map.apply(
      integerRange
        .map(i ⇒ (i, false))
        .toMap[Int, Boolean]
        .toSeq: _*
    )
}