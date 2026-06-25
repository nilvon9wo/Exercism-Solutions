import scala.annotation.tailrec

object Dominoes {
    type Domino = (Int, Int)

    def chain(dominoes: List[Domino]): Option[List[Domino]] = {
        dominoes match {
            case Nil =>
                Some(Nil)

            case _ =>
                val forcedStart :: remainingDominoes = prioritizeSelfLoops(dominoes)
                solveDominoChain(remainingDominoes, List(forcedStart))
                    .filter(_.head._1 == forcedStart._2)
        }
    }

    private def prioritizeSelfLoops(dominoes: List[Domino]): List[Domino] = {
        val (selfLoops, normalDominoes) = dominoes.partition {
            case (left, right) =>
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
            val requiredConnector = builtChain.head._1
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
        if (domino._1 == stone) {
            Option(domino.swap)
        }
        else if (domino._2 == stone) {
            Option(domino)
        }
             else {
                 None
             }
}