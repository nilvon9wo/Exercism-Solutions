object Bob {

  def response(statement: String): String = {
    val cleanStatement = statement.trim
    if (cleanStatement == "") {
      "Fine. Be that way!"
    }
    else {
      val wasYelled           = isYelled(cleanStatement)
      val wasQuestion         = isQuestion(cleanStatement)
      val wasYelledQuestioned = wasYelled && wasQuestion
      cleanStatement match {
        case _ if wasYelledQuestioned => "Calm down, I know what I'm doing!"
        case _ if wasYelled           => "Whoa, chill out!"
        case _ if wasQuestion         => "Sure."
        case _                        => "Whatever."
      }
    }
  }

  private def isYelled(statement: String): Boolean =
    statement.exists(_.isLetter) &&
    statement == statement.toUpperCase

  private def isQuestion(statement: String): Boolean =
    statement.last == '?'
}
