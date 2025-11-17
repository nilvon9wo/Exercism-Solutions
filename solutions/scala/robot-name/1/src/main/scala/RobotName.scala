import java.text.DecimalFormat

import scala.collection.mutable
import scala.util.Random

case class Robot(decimalFormat: DecimalFormat = new DecimalFormat("000")) {
  def reset(): Unit = {
    if (_name.isDefined) {
      RobotCache.dropName(_name.get)
    }
    _name = None
  }

  private var _name: Option[String] = None

  def name: String = if (_name.isDefined) {
    _name.get
  } else {
    var newName: String = ""
    do {
      newName = prefix() + number()
    } while (RobotCache.isTaken(newName))
    RobotCache.addName(newName)
    _name = Some(newName)
    newName
  }

  private def prefix(): String = (randomChar + randomChar)
    .toUpperCase

  private def randomChar(): String = Random.alphanumeric
    .filter(_.isLetter)
    .head
    .toString

  private def number(): String = {
    val random = Random.nextInt(999)
    decimalFormat.format(random)
  }
}

object RobotCache {
  var robotNames: mutable.Set[String] = mutable.Set()

  def addName(name: String): Unit = {
    if (this.robotNames.contains(name)) {
      throw new UsedNameException(s"$name is repeated")
    }
    this.robotNames = this.robotNames + name
  }

  def isTaken(name: String): Boolean =  {
    this.robotNames.contains(name)
  }

  def dropName(name: String): Unit = {
    this.robotNames = this.robotNames - name
  }

}

class UsedNameException(message: String) extends Exception