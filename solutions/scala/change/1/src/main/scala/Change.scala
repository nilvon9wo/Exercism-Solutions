object Change {

  def findFewestCoins(
                       targetAmount: Int,
                       coinValues: List[Int],
                       accumulatedCoins: List[Int] = List[Int]()
                     ): Option[List[Int]] =
    if (targetAmount == 0) {
      Some(accumulatedCoins)
    }
    else if (coinValues.nonEmpty && targetAmount == coinValues.max) {
      Some(coinValues.max :: accumulatedCoins)
    }
    else if (coinValues.isEmpty || targetAmount < coinValues.min) {
      None
    }
    else {
      combineCoins(targetAmount, coinValues, accumulatedCoins)
    }

  private def combineCoins(
                            targetAmount: Int,
                            coinValues: List[Int],
                            accumulatedCoins: List[Int]): Option[List[Int]] = {
    val sortedCoins = coinValues.sorted(Ordering.Int.reverse)
    sortedCoins match {
      case largestCoin :: _
        if largestCoin < targetAmount =>
        evaluateObvious(targetAmount, sortedCoins, accumulatedCoins)

      case _ :: moreCoins =>
        findFewestCoins(targetAmount, moreCoins, accumulatedCoins)
    }
  }

  private def evaluateObvious(
                               targetAmount: Int,
                               sortedCoins: List[Int],
                               accumulatedCoins: List[Int]
                             ): Option[List[Int]] = {
    val largestCoin = sortedCoins.head

    val obviousResult = findFewestCoins(targetAmount - largestCoin, sortedCoins, largestCoin :: accumulatedCoins)
    obviousResult match {
      case Some(obviousCollection)
        if obviousCollection.size == 1
          || sortedCoins.size == 1
          || !hasNonFactors(sortedCoins) =>
        obviousResult

      case Some(_) =>
        evaluateAlternatives(targetAmount, sortedCoins, obviousResult, accumulatedCoins)

      case None =>
        findFewestCoins(targetAmount, sortedCoins.tail, accumulatedCoins)
    }
  }

  private def hasNonFactors(sortedCoins: List[Int]): Boolean = {
    val largestCoin = sortedCoins.head
    val moreCoins = sortedCoins.tail
    moreCoins.exists(largestCoin % _ != 0)
  }

  private def evaluateAlternatives(
                                    targetAmount: Int,
                                    sortedCoins: List[Int],
                                    obviousResult: Option[List[Int]],
                                    accumulatedCoins: List[Int]
                                  ): Option[List[Int]] = {
    val alternativeResult = findFewestCoins(targetAmount, sortedCoins.tail, accumulatedCoins)

    alternativeResult match {
      case Some(alternativeCollection)
        if alternativeCollection.size < obviousResult.get.size =>
        alternativeResult

      case _ =>
        obviousResult
    }
  }
}