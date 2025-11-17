object FlattenArray {
  def flatten(items: List[Any], accumulated: List[Any] = Nil): List[Any] =
    if (items.isEmpty) {
      accumulated
    }
    else {
      val head :: tail = items
      accumulated ::: this.flattenHead(head) ::: this.flatten(tail)
    }

  private def flattenHead(value: Any): List[Any] =
    value match {
      case null =>
        Nil

      case innerList: List[Any] =>
        this.flatten(innerList)

      case simple =>
        List(simple)
    }
}
