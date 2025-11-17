import scala.annotation.tailrec

object Strain {
  @tailrec
  def keep[T](sequence: Seq[T], filterIn: T => Boolean, result: Seq[T] = Nil): Seq[T] =
    sequence match {
      case Nil                            => result
      case head :: tail if filterIn(head) => keep(tail, filterIn, result :+ head)
      case _ :: tail                      => keep(tail, filterIn, result)
    }

  def discard[T](sequence: Seq[T], filterOut: T => Boolean): Seq[T] =
    keep(sequence, (x: T) => !filterOut(x))
}
