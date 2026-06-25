object Dominoes {
  def chain(input: List[(Int, Int)]): Option[List[(Int, Int)]] = {
    val dominoes = input.map(Domino.from)
    val result = dominoes match {
      case Nil =>
        Some(Nil)

      case first :: Nil =>
        Option.when(first.a == first.b)(
          List(first)
        )

      case _ =>
        solveDominoChain(dominoes)
    }

    result.map(_.map(_.toTuple))
  }

  private def solveDominoChain(input: List[Domino]): Option[List[Domino]] = {
    val indexedDominoes = input.zipWithIndex
    val adjacencyMap = buildAdjacencyMap(indexedDominoes)
    if (hasOddDegreeVertices(input)) {
      return None
    }

    val initialState = DominoState.initial(input, adjacencyMap)
    EulerianPathSolver.findEulerianCircuit(initialState)
  }

  private def hasOddDegreeVertices(input: List[Domino]): Boolean = {
    buildDegreeMap(input)
      .values.exists(_ % 2 != 0)
  }

  private def buildDegreeMap(input: List[Domino]): Map[Int, Int] = {
    val empty = Map.empty[Int, Int].withDefaultValue(0)
    input.foldLeft(empty) {
      case (acc, Domino(a, b)) =>
        acc.updated(a, acc(a) + 1)
           .updated(b, acc(b) + 1)
    }
  }

  private def buildAdjacencyMap(indexed: List[(Domino, Int)]): Map[Int, List[AdjacentEdge]] = {
    val empty = Map.empty[Int, List[AdjacentEdge]]
    indexed.foldLeft(empty) {
      case (acc, (Domino(a, b), id)) =>
        acc.updated(a, AdjacentEdge(b, id, a) :: acc.getOrElse(a, Nil))
           .updated(b, AdjacentEdge(a, id, b) :: acc.getOrElse(b, Nil))
    }
  }
}




