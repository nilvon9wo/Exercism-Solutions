

object CollatzConjecture {

  def steps(value: Int): Option[Int] = {
    if (value <= 0) {
      return None
    }
    else if (value == 1) {
      return Some(0)
    }

    val nextStepValue = if (value % 2 == 0) {
      value / 2
    }
                        else {
                          3 * value + 1
                        }

    steps(nextStepValue) match {
      case Some(result) => Some(result + 1)
      case _            => None
    }
  }
}
