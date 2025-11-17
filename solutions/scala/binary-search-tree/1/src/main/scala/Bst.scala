case class Bst[A](value: A, left: Option[Bst[A]], right: Option[Bst[A]]) {

  def insert(newValue: A): Bst[A] =
    if (newValue.toString <= value.toString) {
      Bst(value, updateSubTree(left, newValue), right)
    }
    else {
      Bst(value, left, updateSubTree(right, newValue))
    }

  def toList: List[A] = toList(left) ::: List(value) ::: toList(right)

  private def toList(subTree: Option[Bst[A]]): List[A] =
    subTree.map(_.toList)
           .getOrElse(List())

  private def updateSubTree(subTree: Option[Bst[A]], newValue: A): Option[Bst[A]] = {
    val updatedTree = subTree.map(_.insert(newValue))
    Some(updatedTree.getOrElse(Bst(newValue)))
  }
}

object Bst {
  def fromList[A](list: List[A]): Bst[A] = {
    val bst = Bst(list.head)
    list.tail.foldLeft(bst)(insertNextValues)
  }

  def apply[A](
                value: A,
                left: Option[Bst[A]] = None,
                right: Option[Bst[A]] = None
              ): Bst[A] =
    new Bst(value, left, right)

  private def insertNextValues[A](bst: Bst[A], value: A): Bst[A] =
    bst.insert(value)

  def toList[A](bst: Bst[A]): List[A] = bst.toList
}
