case class Deque[T](var deque: List[T] = Nil) {
  def pop(): Option[T] =
    deque match {
      case head :: tail =>
        deque = tail
        Some(head)
      case _            =>
        None
    }

  def push(value: T): Deque[T] = {
    deque = value :: deque
    this
  }

  def shift(): Option[T] =
    deque.reverse match {
      case head :: tail =>
        deque = tail.reverse
        Some(head)
      case _            =>
        None
    }

  def unshift(value: T): Deque[T] = {
    deque = deque :+ value
    this
  }
}


