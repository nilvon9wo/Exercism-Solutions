

object ListUtilities {

  def createCombinations[TElementType](values: List[TElementType])(digits: Int): Iterator[List[TElementType]] =
    if (digits == values.length) {
      new IterativeHeapPermutator(values)
    }
    else {
      IterableUtilities.toCombinations(values)(digits)
                       .iterator
                       .flatMap(value => new IterativeHeapPermutator(value))
    }
}
