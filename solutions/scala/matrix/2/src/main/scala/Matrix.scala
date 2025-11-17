import scala.collection.mutable

class Matrix(valuesByPoints: Map[Point, Int]) {
  def column(i: Int): Vector[Int] =
    this.extractValues(i, _.hasColumn, _.toRowIndex)

  def row(i: Int): Vector[Int] =
    this.extractValues(i, _.hasRow, _.toColumnIndex)

  private def extractValues(
                             targetIndex: Int,
                             indexFilter: Point => Int => Boolean,
                             pointSorter: Point => Int
                           ): Vector[Int] =
    this.valuesByPoints
      .keys
      .filter(indexFilter.apply(_)(targetIndex))
      .toList
      .sortBy(pointSorter.apply)
      .map(this.extractValue)
      .toVector

  private def extractValue(key: Point): Int =
    this.valuesByPoints(key)
}

case class Point(rowIndex: Int, columnIndex: Int) {
  def hasRow(requiredRow: Int): Boolean =
    this.rowIndex == requiredRow

  def hasColumn(requiredColumn: Int): Boolean =
    this.columnIndex == requiredColumn

  def toRowIndex: Int =
    this.rowIndex

  def toColumnIndex: Int =
    this.columnIndex
}

object Matrix {
  val cellSeparator = " "
  val rowSeparator = "\n"

  def apply(string: String): Matrix =
    this.apply(this.splitForRows(string))

  private def apply(rowList: Array[String]): Matrix =
    this.apply(rowList.map(this.splitForCells))

  private def apply(valuesInRows: Array[Array[String]]): Matrix =
    this.apply(this.convertToPoints(valuesInRows))

  private def apply(valuesByPoints: mutable.Map[Point, Int]): Matrix =
    new Matrix(valuesByPoints.toMap)

  private def convertToPoints(valuesInRows: Array[Array[String]]): mutable.Map[Point, Int] = {
    val valuesByPoints = mutable.Map[Point, Int]()
    valuesInRows.indices
      .foreach { rowNumber =>
        valuesByPoints ++= this.convertRowToPoints(valuesInRows(rowNumber), rowNumber)
      }
    valuesByPoints
  }

  private def convertRowToPoints(row: Array[String], rowNumber: Int): mutable.Map[Point, Int] = {
    val valuesByPoints = mutable.Map[Point, Int]()
    row.indices
      .foreach { columnNumber =>
        valuesByPoints.put(Point.apply(rowNumber, columnNumber), row(columnNumber).toInt)
      }
    valuesByPoints
  }

  private def splitForCells(string: String): Array[String] =
    string.split(cellSeparator)

  private def splitForRows(string: String): Array[String] =
    string.split(rowSeparator)

}