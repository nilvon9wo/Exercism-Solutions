import scala.collection.mutable.ListBuffer

class Matrix(cells: List[Cell]) {
  def column(i: Int): Vector[Int] =
    this.cells
      .filter(forColumn(i))
      .map(extractValue)
      .toVector

  private def forColumn(i: Int)(cell: Cell): Boolean =
    cell.y == i

  def row(i: Int): Vector[Int] =
    this.cells
      .filter(forRow(i))
      .map(extractValue)
      .toVector

  private def extractValue(cell: Cell): Int =
    cell.value

  private def forRow(i: Int)(cell: Cell): Boolean =
    cell.x == i
}

case class Cell(x: Int, y: Int, value: Int)

object Matrix {
  val cellSeparator = " "
  val rowSeparator = "\n"

  def apply(string: String): Matrix =
    this.apply(this.splitForRows(string))

  private def apply(rowList: Array[String]): Matrix =
    this.apply(rowList.map(this.splitForCells))

  private def apply(valuesInRows: Array[Array[String]]): Matrix =
    this.apply(this.convertToCells(valuesInRows))

  private def apply(cells: List[Cell]): Matrix =
    new Matrix(cells)

  private def convertToCells(valuesInRows: Array[Array[String]]): List[Cell] = {
    var cellBuffer = ListBuffer[Cell]()
    valuesInRows.indices
      .foreach { rowNumber =>
        cellBuffer = cellBuffer ++ this.convertRowValuesToCells(valuesInRows(rowNumber), rowNumber)
      }
    cellBuffer.toList
  }

  private def convertRowValuesToCells(row: Array[String], rowNumber: Int): ListBuffer[Cell] = {
    val cellBuffer = ListBuffer[Cell]()
    row.indices
      .foreach { columnNumber =>
        cellBuffer += Cell.apply(rowNumber, columnNumber, row(columnNumber).toInt)
      }
    cellBuffer
  }

  private def splitForCells(string: String): Array[String] =
    string.split(cellSeparator)

  private def splitForRows(string: String): Array[String] =
    string.split(rowSeparator)

}