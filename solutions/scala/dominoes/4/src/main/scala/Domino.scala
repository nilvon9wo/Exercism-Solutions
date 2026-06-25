final case class Domino(left: Int, right: Int) {
    def toTuple: (Int, Int) = {
        (left, right)
    }

    def swap: Domino = {
        Domino(right, left)
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