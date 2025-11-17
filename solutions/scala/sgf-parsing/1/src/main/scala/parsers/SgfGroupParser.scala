package parsers

import models.{SgfGroup, SgfInput, SgfNodeFamily}

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

object SgfGroupParser {
  def parse(input: SgfInput): Try[SgfGroup] = input.current match {
    case Success(currentCharacter) if currentCharacter != SgfSymbols.groupStart =>
      Failure(new IllegalArgumentException("Input is missing group."))
    case _ if !input.moveToNext()                                               =>
      Failure(new IllegalArgumentException("Group is missing parent node."))
    case _                                                                      =>
      parseNodes(input)
  }

  private def parseNodes(input: SgfInput): Try[SgfGroup] = {
    parseNodes(input, ListBuffer[SgfNodeFamily]()) match {
      case Success(nodesBuffer) if nodesBuffer.nonEmpty =>
        Success(new SgfGroup(nodesBuffer.toList))
      case _                                            =>
        Failure(new IllegalArgumentException("Input has no nodes."))
    }
  }

  @scala.annotation.tailrec
  private def parseNodes(input: SgfInput, buffer: ListBuffer[SgfNodeFamily]): Try[ListBuffer[SgfNodeFamily]] = {
    input.current match {
      case Success(c) if c == SgfSymbols.groupEnd =>
        Success(buffer)
      case _                                      =>
        val node = SgfNodeFamilyParser.parse(input)
        node match {
          case Success(n)  =>
            parseNodes(input, buffer += n)
          case Failure(ex) =>
            Failure(ex)
        }
    }
  }
}

