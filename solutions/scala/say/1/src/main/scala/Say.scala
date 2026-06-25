import EnglishWords.{ones, scales, tens}
import scala.annotation.tailrec

object Say:

    def inEnglish(number: Long): Option[String] =
        if number < 0 || number > 999999999999L
        then None
        else Some(convertNumberToWords(number))

    private def convertNumberToWords(number: Long): String =
        if number == 0
        then "zero"
        else buildFullNumberWords(number)

    private def buildFullNumberWords(number: Long): String =
        val chunks = splitIntoThousands(number)
        val totalChunks = chunks.length
        chunks.zipWithIndex
              .map { case (chunk, index) =>
                  formatChunkWithScale(totalChunks, chunk, index)
              }
              .filter(_.nonEmpty)
              .mkString(" ")

    private def formatChunkWithScale(
                                        totalChunks: Int,
                                        chunk: Int,
                                        index: Int
                                    ): String =
        val scaleIndex = totalChunks - index - 1
        val chunkWords = convertBelowThousand(chunk)

        if chunkWords.isEmpty
        then ""
        else attachScale(scaleIndex, chunkWords)

    private def attachScale(scaleIndex: Int, words: String): String =
        val scale = EnglishWords.scaleWord(scaleIndex)
        if scale.isEmpty
        then words
        else s"$words $scale"

    private def splitIntoThousands(number: Long): List[Int] =
        @tailrec
        def loop(x: Long, accumulator: List[Int]): List[Int] =
            if x == 0
            then accumulator
            else loop(x / 1000, (x % 1000).toInt :: accumulator)

        loop(number, Nil)

    private def convertBelowThousand(number: Int): String =
        number match
            case 0 => ""

            case x if x < 20 =>
                ones(x)

            case x if x < 100 =>
                val tensPart = (x / 10) * 10
                val onesPart = x % 10

                if onesPart == 0
                then tens(tensPart)
                else s"${tens(tensPart)}-${ones(onesPart)}"

            case x =>
                val hundreds = x / 100
                val remainder = x % 100

                if remainder == 0
                then s"${ones(hundreds)} hundred"
                else s"${ones(hundreds)} hundred ${convertBelowThousand(remainder)}"