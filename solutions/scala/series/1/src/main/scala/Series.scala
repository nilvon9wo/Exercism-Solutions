object Series {
  def slices(size: Int, string: String): List[List[Int]] =
    if (size == 0 || size > string.length) {
      Nil
    }
    else {
      this.slices(size, string, string.length)
    }

  private def slices(size: Int, string: String, stringLength: Int): List[List[Int]] =
    this.toIntegerListList(
      (0 to stringLength)
        .map(sliceString(size, string))
    )
      .filter(_.size == size)

  private def sliceString(size: Int, string: String)(position: Int) =
    string.slice(position, position + size)

  private def toIntegerListList(collection: IndexedSeq[String]): List[List[Int]] =
    collection
      .map(_.split(""))
      .map(toIntegerList)
      .toList

  private def toIntegerList(strings: Array[String]): List[Int] = {
    strings
      .filterNot(_ == "")
      .map(_.toInt)
      .toList
  }
}