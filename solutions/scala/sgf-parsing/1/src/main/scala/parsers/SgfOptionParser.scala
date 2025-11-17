package parsers

import models.SgfInput

import scala.util.{Failure, Success, Try}

object SgfOptionParser {
  def parse(input: SgfInput): Try[String] = {
    input.current match {
      case Success(currentChar) if currentChar == SgfSymbols.optionStart =>
        if (input.moveToNext()) {
          takeFrom(input)
        }
        else {
          Failure(new IllegalArgumentException("Option is missing contents."))
        }

      case _ =>
        Failure(new IllegalArgumentException("Input is missing option."))
    }
  }

  private def takeFrom(input: SgfInput) = {
    input.takeUntil(SgfSymbols.optionEnd, "Option", escapable = SgfInput.escapable) match {
      case Success(value) =>
        Success(cleanOptionValue(value))
      case failure @ Failure(_) => failure
    }
  }

  private def cleanOptionValue(value: String): String = {
    value
      .replace("\\\\", "\\") // Step 1: Replace every double slash with a single slash
      .replace("\t", " ") // Step 2: Remove all the \t
      .replace("\\\n", "") // Step 3: Remove the \n if there is an extra slash before it
      .replace("\n", " ") // Step 4: Replace any remaining \n with a space
  }
}
