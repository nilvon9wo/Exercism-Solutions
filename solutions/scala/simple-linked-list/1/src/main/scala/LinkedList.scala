class LinkedList[T](seq: Seq[T]) extends SimpleLinkedList[T] {
  private val list = seq.toList
  def isEmpty: Boolean = list.isEmpty
  def value: T = list.head
  def add(item: T):SimpleLinkedList[T] = new LinkedList[T](list :+ item)
  def next = new LinkedList(list.tail)
  def reverse = new LinkedList(list.reverse)
  def toSeq: Seq[T] = list
}
