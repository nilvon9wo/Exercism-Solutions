package parsers

import models.{SgfInput, SgfKeyedOptions}

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}


object SgfKeyedOptionsParser {
  def parse(input: SgfInput): Try[SgfKeyedOptions] =
      SgfKeyParser.parse(input) match {
      case Success(key)       =>
        parseOptions(input, key)
      case Failure(exception) =>
        Failure(exception)
    }

  @tailrec
  private def parseOptions(input: SgfInput, key: String, options: ListBuffer[String] = new ListBuffer[String]()): Try[SgfKeyedOptions] = {
    input.current match {
      case Success(SgfSymbols.optionStart) =>
        SgfOptionParser.parse(input) match {
          case Success(option)    =>
            options += option
            input.moveToNext()
            parseOptions(input, key, options)
          case Failure(exception) =>
            Failure(exception)
        }
      case _                               =>
        Success(new SgfKeyedOptions(key, options.toList))
    }
  }
}
