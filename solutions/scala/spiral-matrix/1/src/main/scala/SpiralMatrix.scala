import scala.annotation.tailrec

object SpiralMatrix {
  def spiralMatrix(size: Int): List[List[Int]] = {
    if (size == 0) {
      return List.fill(0)(List.fill(0)(0))
    }

    val spiralConfiguration = SpiralConfiguration(size)
    generateSpiral(spiralConfiguration)
  }

  @tailrec
  private def generateSpiral(
                              spiralConfiguration: SpiralConfiguration,
                            ): List[List[Int]] = {
    val oldMatrix = spiralConfiguration.matrix
    if (spiralConfiguration.nextCellContent > spiralConfiguration.maximumCellContent) {
      oldMatrix
    }
    else {
      val thisMovement      = spiralConfiguration.nextMovement
      val coordinate        = thisMovement.coordinate
      val oldCurrentColumn  = oldMatrix(coordinate.X)
      val newCurrentColumn  = oldCurrentColumn.updated(coordinate.Y, spiralConfiguration.nextCellContent)
      val newMatrix         = oldMatrix.updated(coordinate.X, newCurrentColumn)
      val nextMovement      = coordinate.next(newMatrix, thisMovement.direction)
      val nextConfiguration = spiralConfiguration.update(newMatrix, nextMovement)
      generateSpiral(nextConfiguration)
    }
  }
}
