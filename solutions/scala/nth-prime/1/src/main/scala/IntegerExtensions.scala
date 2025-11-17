object IntegerExtensions {
  implicit class IntExtensions(value: Int) {
    private lazy val squareRootOfValue: Int = Math.sqrt(value).toInt
    def hasFactor(i: Int): Boolean = value % i == 0
    def isEven: Boolean = value % 2 == 0
    def isPrime: Boolean = {
      if (value.isEven) {
        false
      } else {
        !(3 to squareRootOfValue by 2)
          .exists(value.hasFactor)
      }
    }
  }
}
