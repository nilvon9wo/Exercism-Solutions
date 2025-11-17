case class Point(
                  row: Integer,
                  column: Integer,
                  value: String,
                  var annotation: String = ""
                ) {
  def isAdjacentPoint(that: Point): Boolean = {
    (this.row != that.row || this.column != that.column) &&
      (this.row >= that.row - 1 && this.row <= that.row + 1) &&
      (this.column >= that.column - 1 && this.column <= that.column + 1)
  }

  def setAnnotation(adjacentPoints: Array[Point]): Point = {
    this.annotation = if (this.value.equals("*")) {
      "*"
    }
    else {
      this.calculateAnnotation(adjacentPoints.count(_.value.equals("*")))
    }
    this
  }

  private def calculateAnnotation(mineCount: Integer): String =
    if (mineCount > 0) {
      mineCount.toString
    }
    else {
      this.value
    }
}

case class Matrix(points: Array[Point]) {
  def annotate(): Matrix = {
    points.foreach(point => {
      val surroundingPoints = points.filter(_.isAdjacentPoint(point))
      point.setAnnotation(surroundingPoints)
    })
    this
  }

  def toStringList(): List[String] =
    points.groupBy(_.row)
      .toList
      .sortBy(_._1)
      .map(toRowString)
      .toList

  def toRowString(rowIndexWithPoints: (Integer, Array[Point])): String = {
    val (_, rowPoints) = rowIndexWithPoints
    rowPoints.sortBy(_.column)
      .map(_.annotation)
      .mkString("")
  }
}

object Matrix {
  def fromStringList(board: List[String]): Matrix = {
    new Matrix(
      board.toArray
        .zipWithIndex
        .flatMap(toPointList)
    )
  }

  def toPointList(rowValuesWithRowIndex: (String, Int)): Array[Point] = {
    val (values, row) = rowValuesWithRowIndex
    values.split("")
      .zipWithIndex
      .map(columnValueWithColumnIndex => {
        val (value, column) = columnValueWithColumnIndex
        Point(row = row, column = column, value = value)
      })
  }
}

object Minesweeper {
  def annotate(board: List[String]): List[String] =
    if (board.isEmpty) {
      List()
    }
    else {
      Matrix.fromStringList(board)
        .annotate()
        .toStringList()
    }
}


