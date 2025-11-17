package parsers

import models.{SgfInput, SgfNode, SgfNodeFamily}

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object SgfNodeFamilyParser {
  private val optionEnding: Set[Char] = Set(SgfSymbols.groupStart, SgfSymbols.groupEnd, SgfSymbols.propertySeparator)

  def parse(input: SgfInput): Try[models.SgfNodeFamily] = {
    input.current match {
      case Success(currentCharacter) if currentCharacter != SgfSymbols.propertySeparator
             ⇒ Failure(new IllegalArgumentException("Each nodeFamily must begin with a separator."))
      case _ ⇒
        val parentResult = parseParent(input)
        parentResult match {
          case Failure(exception) ⇒ Failure(exception)
          case Success(parent) ⇒ parseChildren(input) match {
            case Failure(exception) ⇒ Failure(exception)
            case Success(children)  ⇒
              if (children.isEmpty) {
                Success(new SgfNodeFamily(parent))
              }
              else {
                Success(new SgfNodeFamily(parent, children))
              }
          }
        }
    }
  }

  private def parseParent(input: SgfInput): Try[SgfNode] = {
    if (!input.moveToNext()) {
      Failure(new IllegalArgumentException("Unexpected parent ending."))
    }
    else {
      parseParentRecord(input, Map.empty)
    }
  }

  @tailrec
  private def parseParentRecord(input: SgfInput, parent: SgfNode): Try[SgfNode] = {
    if (isEndOfOptions(input)) {
      Success(parent)
    }
    else {
      SgfKeyedOptionsParser.parse(input) match {
        case Success(keyedOptions) =>
          val updatedParent = parent + (keyedOptions.key -> keyedOptions.options)
          parseParentRecord(input, updatedParent)
        case Failure(exception)    =>
          Failure(exception)
      }
    }
  }

  @tailrec
  private def parseChildren(input: SgfInput, children: List[models.SgfGroup] = Nil): Try[List[models.SgfGroup]] = {
    input.current match {
      case Success(currentCharacter) if currentCharacter == SgfSymbols.groupStart =>
        SgfGroupParser.parse(input) match {
          case Success(childrenResult) =>
            if (input.moveToNext()) {
              parseChildren(input, children :+ childrenResult)
            }
            else {
              Failure(new IllegalArgumentException("Child Group ended unexpectedly."))
            }
          case Failure(exception)      =>
            Failure(exception)
        }
      case _                                                                      =>
        Success(children)
    }
  }

  private def isEndOfOptions(input: SgfInput): Boolean =
    input.current match {
      case Success(currentCharacter) if optionEnding.contains(currentCharacter) ⇒ true
      case _                                                                    ⇒ false
    }
}
