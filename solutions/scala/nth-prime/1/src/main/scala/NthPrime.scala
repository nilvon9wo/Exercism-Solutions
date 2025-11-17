import IntegerExtensions.IntExtensions

object NthPrime {

  def prime(nth: Int): Option[Int] =
    if (nth < 1) {
      None
    }
    else {
      val prime = enumeratePrimes
        .drop(nth - 1).head
      Some(prime)
    }

  private def enumeratePrimes: LazyList[Int] = {
    2 #:: LazyList.from(3, 2).filter(_.isPrime)
  }
}
