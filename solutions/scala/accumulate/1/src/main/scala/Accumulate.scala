class Accumulate {
  def accumulate[A, B](
                        function: A => B,
                        list: List[A],
                        accumulated: List[B] = List[B]()
                      ): List[B] = {
    if (list.isEmpty) {
      accumulated.reverse
    }
    else {
      this.accumulate(function, list.tail, function(list.head) :: accumulated)
    }
  }
}
