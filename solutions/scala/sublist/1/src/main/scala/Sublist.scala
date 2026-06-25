import scala.annotation.tailrec

object Sublist {
    sealed trait Result
    case object Equal extends Result
    case object Sublist extends Result
    case object Superlist extends Result
    case object Unequal extends Result

    def sublist[A](a: List[A], b: List[A]): Result =
        (a, b) match
            case _ if a == b => Equal
            case _ if isSublist(a, b) => Sublist
            case _ if isSublist(b, a) => Superlist
            case _ => Unequal

    private def isSublist[A](small: List[A], big: List[A]): Boolean =
        small match
            case Nil => true
            case _ if big.length < small.length => false
            case _ => scan(big, small)

    @tailrec
    private def scan[A](big: List[A], small: List[A]): Boolean =
        big match
            case _ if startsWith(big, small) => true
            case _ :: tail => scan(tail, small)
            case Nil => false

    @tailrec
    private def startsWith[A](big: List[A], small: List[A]): Boolean =
        (big, small) match
            case (_, Nil) => true
            case (bigHead :: bigTail, smallHead :: smallTail) =>
                (bigHead == smallHead)
                    && startsWith(bigTail, smallTail)
            case _ => false
}