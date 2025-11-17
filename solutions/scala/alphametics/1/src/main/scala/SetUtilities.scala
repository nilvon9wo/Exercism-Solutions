object SetUtilities {
  implicit class RichHashSet(noZeroSet: Set[Int]) {
    def buildZeroMask(size: Int): List[Boolean] =
      (0 until size)
        .map(character => !noZeroSet.contains(character))
        .toList
  }
}
