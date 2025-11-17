object DifferenceOfSquares {

  def differenceOfSquares(number: Int): Int =
    this.squareOfSum(number) - this.sumOfSquares(number)

  @scala.annotation.tailrec
  def sumOfSquares(number: Int, accumulated: Int = 0): Int =
    if (number > 0) {
      this.sumOfSquares(number - 1, accumulated + number * number)
    }
    else {
      accumulated
    }

  @scala.annotation.tailrec
  def squareOfSum(number: Int, accumulated: Int = 0): Int =
    if (number > 0) {
      this.squareOfSum(number - 1, accumulated + number)
    }
    else {
      accumulated * accumulated
    }
}
