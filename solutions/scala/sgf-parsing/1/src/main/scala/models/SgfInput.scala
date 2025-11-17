package models

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object SgfInput {
  val escapable = true
  val notEscapable = false
}

class SgfInput(inputString: String) {
  private val originalValue: String = inputString.trim()
  private val inputLength  : Int    = originalValue.length
  private var currentIndex : Int    = -1

  @tailrec
  final def takeUntil(
                       terminator: Char,
                       description: String, stringBuilder:
                       StringBuilder = new StringBuilder(),
                       escapable : Boolean = SgfInput.notEscapable
                     )
  : Try[String] = {
    current match {
      case Success(char) if char != terminator =>
        moveToNext()
        stringBuilder.append(char)
        takeUntil(terminator, description, stringBuilder, escapable)
      case _ if escapable && stringBuilder.nonEmpty && stringBuilder.last == '\\' =>
        stringBuilder.setLength(stringBuilder.length - 1)
        moveToNext()
        stringBuilder.append(terminator)
        takeUntil(terminator, description, stringBuilder, escapable)
      case _                                   =>
        val value = stringBuilder.toString
        if (value.trim.isEmpty) {
          Failure(new IllegalArgumentException(s"$description is missing value."))
        }
        else {
          Success(value)
        }
    }
  }

  def current: Try[Char] = {
    if (hasPositiveIndex && hasNotPassedEnd) {
      Success(originalValue.charAt(currentIndex))
    }
    else {
      Failure(new IllegalStateException("Parser passed the final character."))
    }
  }

  def moveToNext(): Boolean = {
    currentIndex += 1
    hasNotPassedEnd
  }

  private def hasPositiveIndex: Boolean = {
    currentIndex >= 0
  }

  private def hasNotPassedEnd: Boolean = {
    currentIndex < inputLength
  }

}
