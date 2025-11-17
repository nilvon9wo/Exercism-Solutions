case class SecretHandshake()

object SecretHandshake {
  def commands(value: Int): List[String] =
    commands(binaryComponents(value))

  private def commands(binaryComponents: List[Int]): List[String] = {
    var signals = Signal.fromValues(binaryComponents)
    if (signals.contains(Signal.Reverse)) {
      signals = signals.filter(signal => signal != Signal.Reverse)
                       .reverse
    }

    signals.map(signal => signal.text)
  }

  private def binaryComponents(n: Int): List[Int] = {
    if (n == 0) {
      Nil
    }
    else if ((n & 1) == 1) {
      1 :: binaryComponents(n >> 1).map(_ * 2)
    }
         else {
           binaryComponents(n >> 1).map(_ * 2)
         }
  }
}
