import scala.annotation.tailrec

object EulerianPathSolver {

  @tailrec
  def findEulerianCircuit(state: DominoState): Option[List[Domino]] = {
    state.vertexStack match {
      case Nil =>
        validateCompletion(state)

      case currentVertex :: remainingStack =>
        state.findUnusedEdge(currentVertex) match {
          case Some(AdjacentEdge(nextVertex, edgeId, _)) =>
            findEulerianCircuit(
              state.advance(currentVertex, nextVertex, edgeId)
            )
          case None =>
            findEulerianCircuit(
              state.backtrack(remainingStack)
            )
        }
    }
  }

  private def validateCompletion(state: DominoState): Option[List[Domino]] = {
    val allEdgesUsed = state.usedEdgeIds.size == state.inputDominoes.size
    if (allEdgesUsed) {
      Some(state.partialPath.reverse)
    }
    else {
      None
    }
  }
}