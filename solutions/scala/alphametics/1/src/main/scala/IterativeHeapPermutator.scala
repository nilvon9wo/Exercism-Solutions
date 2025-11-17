import scala.collection.mutable.ListBuffer

class IterativeHeapPermutator[TElementType](values: Seq[TElementType])
  extends Iterator[List[TElementType]] {
  private val count                                  = values.length
  private val indexArray                             = Array.fill(count)(0)
  private val valuesBuffer: ListBuffer[TElementType] = ListBuffer(values: _*)
  private var currentIndex                           = 0

  override def hasNext(): Boolean = currentIndex < values.length

  override def next(): List[TElementType] = {
    var result: List[TElementType] = valuesBuffer.toList
    if (indexArray(currentIndex) < currentIndex) {
      swap(valuesBuffer, swapIndex, currentIndex)
      result = valuesBuffer.toList
      indexArray(currentIndex) += 1
      currentIndex = 0
    }
    else {
      indexArray(currentIndex) = 0
      currentIndex = currentIndex + 1
    }

    result
  }

  private def swapIndex =
    if ((currentIndex & 1) == 0) {
      0
    }
    else {
      indexArray(currentIndex)
    }

  private def swap(buffer: ListBuffer[TElementType], i: Int, j: Int): Unit = {
    val temp = buffer(i)
    buffer(i) = buffer(j)
    buffer(j) = temp
  }
}

object IterativeHeapPermutator {
  def apply[TElementType](values: List[TElementType]): IterativeHeapPermutator[TElementType] =
    new IterativeHeapPermutator(values)
}
