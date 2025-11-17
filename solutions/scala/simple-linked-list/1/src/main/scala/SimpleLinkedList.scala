trait SimpleLinkedList[T] {
  def isEmpty: Boolean
  def value: T
  def add(item: T): SimpleLinkedList[T]
  def next: SimpleLinkedList[T]
  def reverse: SimpleLinkedList[T]
  def toSeq: Seq[T]
}

object SimpleLinkedList {
  def apply[T](ts: T*): SimpleLinkedList[T] = fromSeq[T](ts.toSeq)
  def fromSeq[T](seq: Seq[T]): SimpleLinkedList[T] = new LinkedList[T](seq)
}
