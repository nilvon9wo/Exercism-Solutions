import scala.annotation.tailrec

object PascalsTriangle {
  def rows(rowCount: Int): List[List[Int]] = rowCount match {
    case _ if rowCount < 1 => List.empty
    case 1                 => List(List(1))
    case _                 => calculateRows(rowCount, List(List(1), List(1, 1)))
  }

  @tailrec
  private def calculateRows(rows: Int, accumulator: List[List[Int]]): List[List[Int]] = {
    if (rows == accumulator.length) {
      accumulator
    }
    else {
      calculateRows(rows, accumulator :+ makeNextRow(accumulator.last, List(1)))
    }
  }

  @tailrec
  private def makeNextRow(previousRow: List[Int], accumulator: List[Int]): List[Int] = {
    val head = previousRow.head
    if (previousRow.length == 2) {
      accumulator :+ (head + 1) :+ 1
    }
    else {
      val second    = previousRow.tail.head
      val clonedRow = previousRow.tail
      makeNextRow(clonedRow, accumulator :+ (head + second))
    }
  }
}
