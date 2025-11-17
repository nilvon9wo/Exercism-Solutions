import models.SgfInput
import parsers.SgfGroupParser

import scala.util.parsing.combinator.RegexParsers
import scala.util.{Success ⇒ TrySuccess}

object Sgf
  extends RegexParsers {
  type Tree[A] = Node[A]
  type SgfTree = Tree[SgfNode]
  type SgfNode = models.SgfNode
  private type Forest[A] = List[Tree[A]]

  def parseSgf(inputString: String): Option[SgfTree] = {
    Option(inputString)
      .flatMap {
                 str =>
                   val input = new SgfInput(str)
                   input.moveToNext()

                   SgfGroupParser.parse(input) match {
                     case TrySuccess(group) =>
                       val sgfTree       = group.toTree
                       val convertedTree = SgfTreeConverter.convertToSgfTree(sgfTree)
                       Some(convertedTree)
                     case _                 =>
                       None
                   }
               }
  }

  case class Node[A](rootLabel: A, subForest: Forest[A] = List())
}

