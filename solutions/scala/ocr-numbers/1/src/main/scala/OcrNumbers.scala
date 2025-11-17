case class Digit(top: String, middle: String, bottom: String, blank: String)

object OcrNumbers {
  val digitHeight: Int = Digital.digitHeight
  val digitWidth: Int = Digital.digitWidth
  val error: String = Digital.error

  def convert(digital: List[String]): String = {
    if (
      this.hasBadNumberOfRows(digital) ||
        this.hasBadNumberOfColumns(digital)
    ) {
      error
    }
    else {
      val digits = this.toNumerals(digital)
      if (digits.isDefined) {
        digits.get
      }
      else {
        error
      }
    }
  }

  private def toNumerals(rows: List[String]) = {
    val digits = rows.grouped(digitHeight)
      .toList
      .map(this.separateDigits)

    Digital.toNumerals(digits)
  }

  private def separateDigits(fourRows: List[String]): List[Digit] = {
    fourRows.map(_.grouped(digitWidth).toList)
      .transpose
      .map(this.toNumeral)
  }

  private def toNumeral(parts: List[String]): Digit = {
    val top :: middle :: bottom :: blank :: Nil = parts
    Digit(top, middle, bottom, blank)
  }


  private def hasBadNumberOfRows(digital: List[String]): Boolean =
    digital.length % digitHeight != 0

  private def hasBadNumberOfColumns(digital: List[String]): Boolean =
    digital.forall(_.length % digitWidth != 0)
}

object Digital {
  val digitHeight = 4
  val digitWidth = 3
  val error = "?"

  private val digit_0 = Digit(
    " _ ",
    "| |",
    "|_|",
    "   "
  )
  private val digit_1 = Digit(
    "   ",
    "  |",
    "  |",
    "   "
  )
  private val digit_2 = Digit(
    " _ ",
    " _|",
    "|_ ",
    "   "
  )
  private val digit_3 = Digit(
    " _ ",
    " _|",
    " _|",
    "   "
  )
  private val digit_4 = Digit(
    "   ",
    "|_|",
    "  |",
    "   "
  )
  private val digit_5 = Digit(
    " _ ",
    "|_ ",
    " _|",
    "   "
  )
  private val digit_6 = Digit(
    " _ ",
    "|_ ",
    "|_|",
    "   "
  )
  private val digit_7 = Digit(
    " _ ",
    "  |",
    "  |",
    "   "
  )
  private val digit_8 = Digit(
    " _ ",
    "|_|",
    "|_|",
    "   "
  )
  private val digit_9 = Digit(
    " _ ",
    "|_|",
    " _|",
    "   "
  )

  private val numeralByDigital: Map[Digit, String] = Map(
    digit_0 → "0",
    digit_1 → "1",
    digit_2 → "2",
    digit_3 → "3",
    digit_4 → "4",
    digit_5 → "5",
    digit_6 → "6",
    digit_7 → "7",
    digit_8 → "8",
    digit_9 → "9"
  )

  def toNumerals(digits: List[List[Digit]]): Option[String] =
    this.convert(digits, ",", toPeriod)

  private def toPeriod(digits: List[Digit]): Option[String] =
    this.convert(digits, "", toNumeral)

  private def toNumeral(digit: Digit): Option[String] = {
    if (this.hasCorrectShape(digit)) {
      Some(numeralByDigital.getOrElse(digit, error))
    }
    else {
      None
    }
  }

  private def hasCorrectShape(digit: Digit): Boolean =
    digit.top.length == digitWidth &&
      digit.middle.length == digitWidth &&
      digit.bottom.length == digitWidth &&
      digit.blank == "   "

  def convert[A](
                  digits: List[A],
                  separator: String,
                  convertFunction: A ⇒ Option[String]
                ): Option[String] = {
    val numerals = digits.map(convertFunction)
    if (numerals.forall(_.isDefined)) {
      Some(numerals.map(_.get.toString).mkString(separator))
    }
    else {
      None
    }
  }
}