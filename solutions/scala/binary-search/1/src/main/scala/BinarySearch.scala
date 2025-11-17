import scala.annotation.tailrec

object BinarySearch {
  def find(list: List[Int], target: Int): Option[Int] =
    binarySearch(list, target)(0, list.length - 1)

  @tailrec
  private def binarySearch(list: List[Int], target: Int)(start: Int, end: Int): Option[Int] =
    if (start > end) {
      None
    }
    else {
      val middle = extractMiddleValue(start, end, list)
      middle.value match {
        case `target`        =>
          Some(middle.index)
        case value if value > target =>
          binarySearch(list, target)(start, middle.index - 1)
        case _               =>
          binarySearch(list, target)(middle.index + 1, end)
      }
    }

  private def extractMiddleValue(start: Int, end: Int, list: List[Int]):IndexedValue = {
    val middleIndex = start + (end - start) / 2
    val middleValue = list(middleIndex)
    IndexedValue(middleIndex, middleValue)
  }
}
