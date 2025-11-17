import Sgf.SgfNode

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.parsing.combinator.RegexParsers
import scala.util.{Failure, Success, Try}

class SgfInput(inputString: String) {
  private val originalValue: String = inputString.trim()
  private val inputLength  : Int    = originalValue.length
  private var currentIndex : Int    = -1

  @tailrec
  final def takeUntil(
                       terminator: Char,
                       description: String, stringBuilder:
                       StringBuilder = new StringBuilder(),
                       escapable: Boolean = SgfInput.notEscapable
                     )
  : Try[String] = {
    current match {
      case Success(char) if char != terminator                                    =>
        moveToNext()
        stringBuilder.append(char)
        takeUntil(terminator, description, stringBuilder, escapable)
      case _ if escapable && stringBuilder.nonEmpty && stringBuilder.last == '\\' =>
        stringBuilder.setLength(stringBuilder.length - 1)
        moveToNext()
        stringBuilder.append(terminator)
        takeUntil(terminator, description, stringBuilder, escapable)
      case _                                                                      =>
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

class SgfGroup(nodes: List[SgfNodeFamily]) {
  def toTree: SgfTreeInternal = {
    val parentNodeFamily: SgfNodeFamily = nodes.head
    if (parentNodeFamily.children.nonEmpty) {
      parentNodeFamily.toTree
    }
    else {
      val children: List[SgfTreeInternal] = nodes
        .tail
        .map(node => node.toTree)
      new SgfTreeInternal(parentNodeFamily.parent, children)
    }
  }
}

class SgfNodeFamily(val parent: SgfNode, val children: List[SgfGroup] = List.empty) {
  def toTree: SgfTreeInternal = {
    if (children.isEmpty) {
      new SgfTreeInternal(parent)
    }
    else {
      new SgfTreeInternal(parent, children.map(_.toTree))
    }
  }
}

class SgfTreeInternal(val data: SgfNode, val children: List[SgfTreeInternal]) {
  def this(data: SgfNode) = {
    this(data, List.empty)
  }
}

class SgfKeyedOptions(val key: String, val options: List[String])

object Sgf
  extends RegexParsers {
  type Tree[A] = Node[A]
  type SgfNode = Map[String, List[String]]
  private type SgfTree = Tree[SgfNode]
  private type Forest[A] = List[Tree[A]]

  def parseSgf(inputString: String): Option[SgfTree] = {
    Option(inputString)
      .flatMap {
                 str => {
                   val input = new SgfInput(str)
                   input.moveToNext()

                   SgfGroupParser.parse(input) match {
                     case util.Success(group) ⇒
                       Some(SgfTreeConverter.convertToSgfTree(group.toTree))
                     case _                   ⇒ None
                   }
                 }
               }
  }

  case class Node[A](rootLabel: A, subForest: Forest[A] = List())
}

object SgfInput {
  val escapable = true
  private val notEscapable = false
}

object SgfSymbols {
  val groupStart       : Char = '('
  val groupEnd         : Char = ')'
  val optionStart      : Char = '['
  val optionEnd        : Char = ']'
  val propertySeparator: Char = ';'
}


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


object SgfNodeFamilyParser {
  private val optionEnding: Set[Char] = Set(SgfSymbols.groupStart, SgfSymbols.groupEnd, SgfSymbols.propertySeparator)

  def parse(input: SgfInput): Try[SgfNodeFamily] = {
    input.current match {
      case Success(currentCharacter) if currentCharacter != SgfSymbols.propertySeparator
             ⇒ Failure(new IllegalArgumentException("Each nodeFamily must begin with a separator."))
      case _ ⇒
        val parentResult = parseParent(input)
        parentResult match {
          case Failure(exception) ⇒ Failure(exception)
          case Success(parent)    ⇒ parseChildren(input) match {
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
  private def parseChildren(input: SgfInput, children: List[SgfGroup] = Nil): Try[List[SgfGroup]] = {
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

object SgfKeyedOptionsParser {
  def parse(input: SgfInput): Try[SgfKeyedOptions] =
    SgfKeyParser.parse(input) match {
      case Success(key)       =>
        parseOptions(input, key)
      case Failure(exception) =>
        Failure(exception)
    }

  @tailrec
  private def parseOptions(input: SgfInput, key: String, options: ListBuffer[String] = new ListBuffer[String]())
  : Try[SgfKeyedOptions] = {
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
      case Success(value)     =>
        Success(cleanOptionValue(value))
      case failure@Failure(_) => failure
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

import Sgf.{Node, Tree}

object SgfTreeConverter {
  def convertToSgfTree(sgfTree: SgfTreeInternal): Tree[SgfNode] = {
    val rootNode = convertNode(sgfTree)
    Sgf
      .Node(
        rootNode
          .rootLabel, rootNode
          .subForest)
  }

  private def convertNode(node: SgfTreeInternal): Node[SgfNode] = {
    val convertedData                     = node.data.view.mapValues(_.toList)
                                                .toMap
    val childrenNodes: Seq[Node[SgfNode]] = node.children.map(convertNode)
    Sgf.Node(convertedData, childrenNodes.toList)
  }
}
