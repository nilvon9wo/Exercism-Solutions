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

  @scala.annotation.tailrec
  def isPaired(string: String = "", openers: List[Char] = Nil): Boolean
  = this.evaluateFirstCharacter(string) match {
    case CharacterRole.None =>
      openers.isEmpty

    case CharacterRole.Opener =>
      this.isPaired(string.substring(1), string.charAt(0) :: openers)

    case CharacterRole.Closer =>
      if (openers.nonEmpty && isMatchingHead(string, openers)) {
        isPaired(string.substring(1), openers.tail)
      }
      else {
        false
      }

    case CharacterRole.Other =>
      this.isPaired(string.substring(1), openers)
  }

  private def evaluateFirstCharacter(string: String): CharacterRole
  = if (string.isBlank) {
    CharacterRole.None
  } else {
    evaluateFirstCharacter(string.charAt(0))
  }

  private def evaluateFirstCharacter(character: Character): CharacterRole
  = Cond(Seq(
    ( { () => this.all_openers.contains(character) }, { () => CharacterRole.Opener }),

    ( { () => this.all_closers.contains(character) }, { () => CharacterRole.Closer }),

    ( { () => true }, { () => CharacterRole.Other })
  ))

  private def isMatchingHead(string: String, openers: List[Char]): Boolean = {
    string.charAt(0) == this.closersByOpeners.getOrElse(openers.head, "")
  }
}

object CharacterRole extends Enumeration {
  type CharacterRole = Value
  val Opener, Closer, Other, None = Value
}

object Cond {
  def apply(clauses: Seq[(() => Boolean, () => CharacterRole)]): CharacterRole = {
    clauses find (_._1()) map (_._2()) getOrElse CharacterRole.None
  }
}
