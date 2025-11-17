import WordCount.{removeSingleQuotes, wordPattern}

import java.util.regex.Pattern

case class WordCount(words: String) {
  lazy val countWords: Map[String, Int] =
    wordsList
      .map(_.toLowerCase)
      .groupBy(identity)
      .view.mapValues(_.length)
      .toMap

  private lazy val wordsList =
    words.split(wordPattern)
         .map(removeSingleQuotes)
         .filter(_.nonEmpty)
}

object WordCount {
  private[this] val singleQuote = "'"
  private       val wordPattern = s"[^\\w${Pattern.quote(singleQuote)}]+"
  private[this] val singleQuotePattern = s"^$singleQuote|$singleQuote$$"

  private def removeSingleQuotes(words: String): String = {
    words.replaceAll(singleQuotePattern, "")
  }
}
