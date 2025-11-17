case class Triangle(sides: List[Number]) {
  lazy val equilateral: Boolean = isValidTriangle &&
                                  hasAllEqualSides
  lazy val isosceles  : Boolean = isValidTriangle &&
                                  hasAtLeastTwoEqualSides
  lazy val scalene    : Boolean = isValidTriangle &&
                                  hasNoEqualSides
  private lazy val eachValueExceedsSumOfAllOthers: Boolean = {
    val sortedSides = sides.map(_.floatValue()).sorted
    sortedSides.head + sortedSides(1) > sortedSides(2)
  }
  private lazy val hasNoEqualSides        : Boolean = sides.distinct.length == 3
  private lazy val hasAllEqualSides       : Boolean = sides.distinct.length == 1
  private lazy val hasAtLeastTwoEqualSides: Boolean = sides.distinct.length <= 2
  private lazy val isValidTriangle: Boolean = !sides.contains(0) &&
                                              eachValueExceedsSumOfAllOthers
}

object Triangle {
  def apply(side1: Number, side2: Number, side3: Number): Triangle = new Triangle(List(side1, side2, side3))
}
