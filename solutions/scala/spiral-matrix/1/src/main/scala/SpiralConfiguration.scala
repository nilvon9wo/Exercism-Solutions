case class SpiralConfiguration(
                                matrix: List[List[Int]],
                                maximumCellContent: Int,
                                nextCellContent: Int,
                                nextMovement: Movement,
                              ) {
  def update(updatedMatrix: List[List[Int]], nextMovement: Movement): SpiralConfiguration =
    new SpiralConfiguration(
      matrix = updatedMatrix,
      maximumCellContent = maximumCellContent,
      nextCellContent = nextCellContent + 1,
      nextMovement = nextMovement,
      )


}

object SpiralConfiguration {
  def apply(size: Int): SpiralConfiguration = {
    new SpiralConfiguration(
      matrix = List.fill(size)(List.fill(size)(0)),
      maximumCellContent = math.pow(size, 2).toInt,
      nextCellContent = 1,
      nextMovement = Movement(new Coordinate(0, 0), Direction.Right),
      )
  }
}
