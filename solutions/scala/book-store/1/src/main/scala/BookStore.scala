import scala.collection.mutable

object BookStore {
  val bookCost = 800
  val discountsByDifferentBooks: Map[Int, Double] = Map(
    0 → 0.00,
    1 → 0.00,
    2 → 0.05,
    3 → 0.10,
    4 → 0.20,
    5 → 0.25
  )
  val completeSetQuantity: Int = discountsByDifferentBooks.keySet.max
  val negotiableDiscountQuantity: Int = 3

  def total(bookIds: List[Int]): Int = {
    val numberOfBooks = bookIds.length
    val quantityByBooks = this.countBooks(bookIds)

    if (isNoDiscount(numberOfBooks, quantityByBooks)) {
      this.normalCost(numberOfBooks)
    }
    else if (this.isSimpleDiscount(numberOfBooks, quantityByBooks)) {
      this.discountedCost(numberOfBooks)
    }
    else {
      val groupedTowardsSeriesCompletion: Iterable[mutable.Set[Int]] =
        this.groupTowardsSeriesCompletion(quantityByBooks)

      val regroupedTowardsMaximumDiscount: Iterable[mutable.Set[Int]] =
        this.regroupTowardsMaximumDiscount(groupedTowardsSeriesCompletion)

      regroupedTowardsMaximumDiscount.map(countBooks)
        .sum
    }
  }

  private def groupTowardsSeriesCompletion(quantityByBooks: Map[Int, Int]): Iterable[mutable.Set[Int]] = {
    val groupedBooks: mutable.Map[Int, mutable.Set[Int]] = new mutable.HashMap[Int, mutable.Set[Int]]()

    quantityByBooks.foreach { case (code: Int, quantity: Int) ⇒
      (1 to quantity)
        .foreach { i ⇒
          groupedBooks.getOrElseUpdate(i, mutable.HashSet[Int]())
            .add(code)
        }
    }

    groupedBooks.values
  }

  // For maximum discount, when possible,
  // need to move books from sets of 5 into sets of 3 (resulting in 2 sets of 4 each)
  private def regroupTowardsMaximumDiscount(
                                             groupedTowardsSeriesCompletion: Iterable[mutable.Set[Int]]
                                           ): Iterable[mutable.Set[Int]] = {
    val groupedBySetSize: mutable.Map[Int, Iterable[mutable.Set[Int]]] =
      mutable.Map(groupedTowardsSeriesCompletion.groupBy(_.size).toSeq: _*)


    if (this.isRegroupingUseless(groupedBySetSize)) {
      groupedTowardsSeriesCompletion
    }
    else {
      this.regroupTowardsMaximumDiscount(groupedBySetSize)
    }
  }

  private def isRegroupingUseless(groupedBySetSize: mutable.Map[Int, Iterable[mutable.Set[Int]]]) =
    groupedBySetSize.getOrElse(completeSetQuantity, Nil).isEmpty ||
      groupedBySetSize.getOrElse(negotiableDiscountQuantity, Nil).isEmpty

  private def regroupTowardsMaximumDiscount(
                                             groupedBySetSize: mutable.Map[Int, Iterable[mutable.Set[Int]]]
                                           ): Iterable[mutable.Set[Int]] = {
    val completeGroup = groupedBySetSize.getOrElse(completeSetQuantity, Nil)
    val groupsOf3 = groupedBySetSize.getOrElse(negotiableDiscountQuantity, Nil)

    val smallerSetSize = Set(completeGroup.size, groupsOf3.size).min
    val completeGroupsToRedistribute = completeGroup.take(smallerSetSize)
    groupedBySetSize.put(completeSetQuantity, completeGroup.drop(smallerSetSize))

    val groupsOf3ToAugment = groupsOf3.take(smallerSetSize)
    groupedBySetSize.put(negotiableDiscountQuantity, groupsOf3.drop(smallerSetSize))

    val oldGroupsOf4: Iterable[mutable.Set[Int]] = groupedBySetSize.getOrElse(4, Nil)
    val newGroupsOf4: Iterable[mutable.Set[Int]] = this.regroupTowardsMaximumDiscount(completeGroupsToRedistribute, groupsOf3ToAugment)
    groupedBySetSize.put(4, newGroupsOf4 ++ oldGroupsOf4)

    groupedBySetSize.values.flatten
  }

  private def regroupTowardsMaximumDiscount(
                                             completeGroupsToRedistribute: Iterable[mutable.Set[Int]],
                                             groupsOf3ToAugment: Iterable[mutable.Set[Int]]
                                           ): Iterable[mutable.Set[Int]] = {
    val (groupOf4_1, groupOf4_2) = groupsOf3ToAugment.zip(completeGroupsToRedistribute)
      .map(this.regroupTowardsMaximumDiscount1)
      .unzip

    groupOf4_1 ++ groupOf4_2
  }

  private def regroupTowardsMaximumDiscount1(
                                              pairToRegroup: (mutable.Set[Int], mutable.Set[Int])
                                            ): (mutable.Set[Int], mutable.Set[Int]) = {
    val (receiverSet, giverSet) = pairToRegroup

    val (missingBook, _) = (1 to completeSetQuantity)
      .map(bookNumber ⇒ (bookNumber, receiverSet.contains(bookNumber)))
      .filterNot(this.isContained)(0)

    receiverSet.add(missingBook)
    giverSet.remove(missingBook)

    (receiverSet, giverSet)
  }

  private def isContained(bookContained: (Int, Boolean)): Boolean = {
    val (_, contained) = bookContained
    contained
  }


  private def isNoDiscount(numberOfBooks: Int, quantityByBooks: Map[Int, Int]) =
    numberOfBooks < 2 ||
      quantityByBooks.values.exists(_ == numberOfBooks)

  private def isSimpleDiscount(numberOfBooks: Int, quantityByBooks: Map[Int, Int]) =
    quantityByBooks.values.forall(_ == 1)

  private def countBooks(bookIds: List[Int]): Map[Int, Int] = {
    bookIds.groupBy(value ⇒ value)
      .map(this.countBooks)
  }

  private def countBooks(codeListPair: (Int, List[Int])): (Int, Int) = {
    val (code, list) = codeListPair
    (code, list.length)
  }

  private def countBooks(books: mutable.Set[Int]): Int =
    this.discountedCost(books.size)

  private def normalCost(numberOfBooks: Int): Int =
    numberOfBooks * this.bookCost

  private def discountedCost(numberOfBooks: Int) =
    this.normalCost(numberOfBooks) - this.discountForDifferentBooks(numberOfBooks)

  private def discountForDifferentBooks(numberOfBooks: Int): Int =
    (this.bookCost * numberOfBooks * this.discountsByDifferentBooks(numberOfBooks)).toInt
}