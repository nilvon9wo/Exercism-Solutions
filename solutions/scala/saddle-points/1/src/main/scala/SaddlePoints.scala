import scala.collection.mutable

class Matrix(valuesByPoints: Map[Point, Int]) {
  val pointsByRows: Map[Int, Set[Point]] = this.valuesByPoints
    .keySet
    .groupBy(_.rowIndex)

  val pointsByColumns: Map[Int, Set[Point]] = this.valuesByPoints
    .keySet
    .groupBy(_.columnIndex)

  // It's called a "saddle point" because
  // it is greater than or equal to every element in its row
  // and less than or equal to every element in its column.
  def saddlePoints: Set[(Int, Int)] =
    this.pointsWithGreatestOrEqualValueInOwnRow
      .filter(this.hasLeastOrEqualValueInColumn)
      .map(_.toTuple2)

  def pointsWithGreatestOrEqualValueInOwnRow: Set[Point] =
    this.pointsByRows
      .values
      .flatten
      .filter(this.hasGreatestOrEqualValueInRow)
      .toSet

  def hasGreatestOrEqualValueInRow(point: Point): Boolean = {
    val pointsInRow: Set[Point] = this.pointsByRows(point.rowIndex)
    this.valuesByPoints(point) == this.findMaxValueInSet(pointsInRow)
  }

  def findMaxValueInSet(points: Set[Point]): Int =
    points
      .map(this.valuesByPoints(_))
      .max

  def hasLeastOrEqualValueInColumn(point: Point): Boolean = {
    val pointsInColumn: Set[Point] = this.pointsByColumns(point.columnIndex)
    this.valuesByPoints(point) == this.findLeastValueInSet(pointsInColumn)
  }

  def findLeastValueInSet(points: Set[Point]): Int =
    points
      .map(this.valuesByPoints(_))
      .min
}

case class Point(rowIndex: Int, columnIndex: Int) {
  def toTuple2: (Int, Int) = (this.rowIndex, this.columnIndex)
}

object Matrix {
  def apply(valuesInRows: List[List[Int]]): Matrix =
    this.apply(this.convertToPoints(valuesInRows))

  private def apply(valuesByPoints: mutable.Map[Point, Int]): Matrix =
    new Matrix(valuesByPoints.toMap)

  private def convertToPoints(valuesInRows: List[List[Int]]): mutable.Map[Point, Int] = {
    val valuesByPoints = mutable.Map[Point, Int]()
    valuesInRows.indices
      .foreach { rowNumber =>
        valuesByPoints ++= this.convertRowToPoints(valuesInRows(rowNumber), rowNumber)
      }
    valuesByPoints
  }

  private def convertRowToPoints(row: List[Int], rowNumber: Int): mutable.Map[Point, Int] = {
    val valuesByPoints = mutable.Map[Point, Int]()
    row.indices
      .foreach { columnNumber =>
        valuesByPoints.put(Point.apply(rowNumber, columnNumber), row(columnNumber))
      }
    valuesByPoints
  }
}