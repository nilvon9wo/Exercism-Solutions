package parsers

import models.SgfInput

import scala.util.{Failure, Success, Try}

object SgfKeyParser {
  def parse(input: SgfInput): Try[String] = {
    val keyResult = input.takeUntil(SgfSymbols.optionStart, "Key")
    keyResult match {
      case Success(key) if key.exists(_.isLower) =>
        Failure(new IllegalArgumentException(s"Key $key contains disallowed lowercase."))
      case _                                     =>
        keyResult
    }
  }
}
