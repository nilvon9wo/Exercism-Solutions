import java.text.DecimalFormat
import scala.collection.mutable

case class Robot() {
  def reset(): Unit = {
    _name = None
  }

  private var _name: Option[String] = None

  def name: String = if (_name.isDefined) {
    _name.get
  } else {
    val newName: String = RobotNameRegistry.getName
    _name = Some(newName)
    newName
  }
}

object RobotNameRegistry {
  val decimalFormat: DecimalFormat = new DecimalFormat("000")
  var namesToRecycle: mutable.Set[String] = mutable.Set()

  def getName: String = {
    this.generateName()
  }

  var firstLetterCounter = 0
  var secondLetterCounter = 0
  var numberCounter = 0

  private def generateName(): String = {
    if (this.firstLetterCounter == 26) {
      this.secondLetterCounter = secondLetterCounter + 1
      this.numberCounter = 0
    }
    if (this.numberCounter == 1000) {
      this.firstLetterCounter = firstLetterCounter + 1
      this.secondLetterCounter = 0
      this.numberCounter = 0
    }
    if (this.firstLetterCounter == 27) {
      throw new NamesExhaustedException("No names left")
    }

    val name = this.convert(this.firstLetterCounter) +
      this.convert(this.secondLetterCounter) +
      this.decimalFormat.format(this.numberCounter)
    this.numberCounter = this.numberCounter + 1
    name
  }

  private def convert(value: Int): String = {
    val letter = if (value < 26) {
      ""
    } else {
      convert(value / 26 - 1)
    }
    letter + (65 + value % 26).toChar
  }
}

class NamesExhaustedException(message: String) extends Exception