import CharacterRole.CharacterRole

import scala.collection.immutable.HashMap

object MatchingBrackets {
  val closersByOpeners: HashMap[Char, Char] = HashMap(
    '[' -> ']',
    '{' -> '}',
    '(' -> ')',
  )
  val all_openers: Set[Char] = closersByOpeners.keySet
  val all_closers: Set[Char] = closersByOpeners.values.toSet

  def isPaired(string: String, openers: List[Char] = Nil): Boolean = {
    if (string.isBlank) {
      openers.isEmpty
    }
    else {
      this.determineRole(string.charAt(0)) match {
        case CharacterRole.Opener => this.handleOpener(string, openers)
        case CharacterRole.Closer => this.handleCloser(string, openers)
        case CharacterRole.Other => this.handleOther(string, openers)
      }
    }
  }

  private def handleOpener(string: String, openers: List[Char]): Boolean
  = isPaired(string.substring(1), string.charAt(0) :: openers)

  private def handleCloser(string: String, openers: List[Char]): Boolean = {
    if (openers.isEmpty || !isMatchingHead(string, openers)) {
      false
    }
    else {
      isPaired(string.substring(1), openers.tail)
    }
  }

  private def handleOther(string: String, openers: List[Char]): Boolean
  = isPaired(string.substring(1), openers)

  private def determineRole(character: Char): CharacterRole = {
    if (this.all_openers.contains(character)) {
      CharacterRole.Opener
    }
    else if (this.all_closers.contains(character)) {
      CharacterRole.Closer
    }
    else {
      CharacterRole.Other
    }
  }

  private def isMatchingHead(string: String, openers: List[Char]): Boolean = {
    string.charAt(0) == this.closersByOpeners.getOrElse(openers.head, "")
  }
}

object CharacterRole extends Enumeration {
  type CharacterRole = Value
  val Opener, Closer, Other = Value
}
