import scala.annotation.tailrec

object Dominoes {

    def chain(input: List[(Int, Int)]): Option[List[(Int, Int)]] = {
        val dominoes = input.map(Domino.from)

        val result = dominoes match {
            case Nil =>
                Some(Nil)

            case _ =>
                val forcedStart :: remainingDominoes = prioritizeSelfLoops(dominoes)
                solveDominoChain(remainingDominoes, List(forcedStart))
                    .filter(_.head.left == forcedStart.right)
        }

        result.map(_.map(_.toTuple))
    }

    private def prioritizeSelfLoops(dominoes: List[Domino]): List[Domino] = {
        val (selfLoops, normalDominoes) = dominoes.partition {
            case Domino(left, right) =>
                left == right
        }

        selfLoops ++ normalDominoes
    }

    @tailrec
    private def solveDominoChain(remainingDominoes: List[Domino], builtChain: List[Domino]): Option[List[Domino]] = {
        if (remainingDominoes.isEmpty) {
            Option(builtChain)
        }
        else {
            val requiredConnector = builtChain.head.left
            remainingDominoes.span(isNotConnectable(requiredConnector, _)) match {
                case (_, Nil) =>
                    None

                case (nonMatchingDominoes, candidateDomino :: restDominoes) =>
                    solveDominoChain(
                        nonMatchingDominoes ++ restDominoes,
                        connect(requiredConnector, candidateDomino).get +: builtChain
                    )
            }
        }
    }

    private def isNotConnectable(stone: Int, domino: Domino): Boolean =
        connect(stone, domino).isEmpty

    private def connect(stone: Int, domino: Domino): Option[Domino] =
        if (domino.left == stone) {
            Option(domino.swap)
        }
        else if (domino.right == stone) {
            Option(domino)
        }
             else {
                 None
             }
}