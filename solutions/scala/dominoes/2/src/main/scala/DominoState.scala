case class DominoState(
	                      inputDominoes: List[Domino],
	                      vertexStack: List[Int],
	                      adjacency: Map[Int, List[AdjacentEdge]],
	                      usedEdgeIds: Set[Int],
	                      partialPath: List[Domino]
                      ) {

	def findUnusedEdge(fromVertex: Int): Option[AdjacentEdge] = {
		adjacency
			.getOrElse(fromVertex, Nil)
			.find {
				case AdjacentEdge(_, edgeId, _) =>
					!usedEdgeIds(edgeId)
			}
	}

	def advance(fromVertex: Int, toVertex: Int, edgeId: Int): DominoState = {
		DominoState(
			inputDominoes,
			toVertex :: vertexStack,
			adjacency,
			usedEdgeIds + edgeId,
			Domino(fromVertex, toVertex) :: partialPath
		)
	}

	def backtrack(newVertexStack: List[Int]): DominoState = {
		DominoState(
			inputDominoes,
			newVertexStack,
			adjacency,
			usedEdgeIds,
			partialPath
		)
	}
}

object DominoState {
	def initial(
		           inputDominoes: List[Domino],
		           adjacency: Map[Int, List[AdjacentEdge]]
	           ): DominoState = {
		DominoState(
			inputDominoes,
			vertexStack = List(
				inputDominoes
					.head
					.a
			),
			adjacency,
			Set
				.empty,
			Nil
		)
	}
}