final case class Domino(a: Int, b: Int) {
  def toTuple: (Int, Int) = {
    (a, b)
  }
}

object Domino {
  def from(domino: (Int, Int)): Domino = {
    Domino(
      domino._1,
      domino._2
    )
  }
}