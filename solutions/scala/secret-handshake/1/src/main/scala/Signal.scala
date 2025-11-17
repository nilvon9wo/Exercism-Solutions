import scala.language.experimental.macros

sealed trait Signal {
  val binaryString: String
  val text        : String

  def value: Int = Integer.parseInt(binaryString, 2)
}

object Signal {
  private val allSignals: Set[Signal] = Set(Wink, DoubleBlink, CloseYourEyes, Jump, Reverse)

  def fromValues(values: List[Int]): List[Signal] =
    values.sorted.flatMap(value => allSignals.find(_.value == value))

  case object Reverse
    extends Signal {
    val binaryString = "10000"
    val text         = "Reverse the order of the operations in the secret handshake."
  }

  private case object Wink
    extends Signal {
    val binaryString = "00001"
    val text         = "wink"
  }

  private case object DoubleBlink
    extends Signal {
    val binaryString = "00010"
    val text         = "double blink"
  }

  private case object CloseYourEyes
    extends Signal {
    val binaryString = "00100"
    val text         = "close your eyes"
  }

  private case object Jump
    extends Signal {
    val binaryString = "01000"
    val text         = "jump"
  }
}
