import scala.collection.immutable.HashSet
import scala.collection.mutable

object IterableUtilities {
  def extractWordInitials(values: Iterable[Iterable[Int]]): HashSet[Int] =
    values.flatMap(_.headOption)
          .to(HashSet)

  def parse(values: Iterable[Iterable[Int]]): List[(Int, List[(Int, Int)])] = {
    val reversedSequence = values
      .map(_.toSeq.reverse.map(_ + 1))
      .toSeq
      .reverse
    transpose(reversedSequence)
      .map(_.filter(i => i > 0).map(_ - 1))
      .map(processColumn)
      .toList
  }

  private def processColumn(column: Seq[Int]) =
    (
      column.head,
      column.tail.groupBy(identity)
            .view
            .mapValues(_.size)
            .toList
    )

  private def transpose(list: Seq[Seq[Int]]): Seq[Seq[Int]] =
    if (list.isEmpty || list.head.isEmpty) {
      List.empty
    }
    else {
      list.head.indices.map(extractColumnValues(list))
    }

  private def extractColumnValues(list: Seq[Seq[Int]])(x: Int) =
    list.map(_.lift(x).getOrElse(0))

  def toCombinations[TElementType](items: Seq[TElementType])(digits: Int): Seq[Seq[TElementType]] =
    if (digits == 0) {
      Seq(Seq.empty[TElementType])
    }
    else {
      items.zipWithIndex.flatMap(generateCombinationsFromIndex(items, digits))
    }

  def mapCharactersToIntegers(characters: mutable.ArraySeq[Char]): Map[Char, Int] =
    characters.zipWithIndex
              .toMap

  private def generateCombinationsFromIndex[TElementType](items: Seq[TElementType], digits: Int): (
                                                                                                  (TElementType,
                                                                                                    Int)
                                                                                                  ) =>
    Seq[Seq[TElementType]]
  = {
    case (element, index) =>
      toCombinations(items.drop(index + 1))(digits - 1)
        .map(element +: _)
  }
}
