object Hamming {
  def distance(dnaStrandOne: String, dnaStrandTwo: String): Option[Int]
  = if (dnaStrandOne.length != dnaStrandTwo.length) {
    None
  }
    else {
      Some(
        dnaStrandOne.zip(dnaStrandTwo)
                    .count {
                             case (character1, character2) => character1 != character2
                           }
        )
    }
}
